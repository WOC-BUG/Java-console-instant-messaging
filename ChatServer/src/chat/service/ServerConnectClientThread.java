package chat.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import chat.common.Message;
import chat.common.MessageType;
import chat.utils.DateUtils;

public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userID;

    public ServerConnectClientThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }


    public void run() {
        while (true) {
            System.out.println("服务端和<" + this.userID + ">保持通讯，读取数据...");

            try {
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                Message msg = (Message) ois.readObject();

                // 返回在线用户列表
                if (msg.getMessageType().equals(MessageType.MESSAGE_RETURN_OLINE_FRIEND)) {
                    System.out.println(msg.getSender() + "需要在线用户列表：");
                    msg.setContent(ManageServerConnectClientThread.getOnlineUsers());
                    msg.setReceiver(msg.getSender());
                    msg.setSender(InetAddress.getLocalHost().toString());

                    // 这里的oos不能放在if外部创建
                    // 因为在用户发送普通消息时，用不上它
                    // 就会导致创建多个ObjectOutputStream共用一个socket中获取的输出流
                    ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                    oos.writeObject(msg);
                }
                // 普通用户消息
                else if (msg.getMessageType().equals(MessageType.MESSAGE_COMMON_MESSAGE)) {
                    System.out.println(msg.getSender() + "转发消息给" + msg.getReceiver());
                    ServerConnectClientThread thread = ManageServerConnectClientThread.getThread(msg.getReceiver());
                    if (thread == null) {
                        returnMessageToSender(msg);
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(msg);   // 转发信息
                    }

                }
                // 群发消息
                else if (msg.getMessageType().equals(MessageType.MESSAGE_TO_ALL)) {
                    System.out.println(msg.getSender() + "群发消息给所有人");

                    // 遍历HashMap，群发消息
                    HashMap<String, ServerConnectClientThread> mp = ManageServerConnectClientThread.getMp();
                    for (Map.Entry<String, ServerConnectClientThread> entry : mp.entrySet()) {
                        if (entry.getKey().equals(userID))
                            continue;
                        System.out.println(msg.getSender() + "转发消息给" + entry.getKey());
                        ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getSocket().getOutputStream());
                        oos.writeObject(msg);   // 转发信息
                    }
                } else if (msg.getMessageType().equals(MessageType.MESSAGE_FILE)) {
                    System.out.println("<" + msg.getSender() + "> 转发文件给 <" + msg.getReceiver() + ">");
                    ServerConnectClientThread thread = ManageServerConnectClientThread.getThread(msg.getReceiver());

                    if (thread == null) {   // 如果对方不在线就存入留言
                        returnMessageToSender(msg);
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(msg);
                    }
                }
                // 退出系统
                else if (msg.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println("用户<" + msg.getSender() + ">退出系统！");
                    ManageServerConnectClientThread.removeThread(msg.getSender());
                    this.socket.close();
                    return;
                } else {
                    System.out.println("不处理其他类型的消息");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void returnMessageToSender(Message msg) throws IOException {
        // 保存留言
        ManageServerConnectClientThread.addConcurrentMap(msg.getReceiver(), msg);

        // 回复发送方
        Message ans = new Message();
        ans.setContent("当前用户不在线，留言将在对方登录后发送！");
        ans.setReceiver(msg.getSender());
        ans.setSender("系统");
        ans.setSendTime(DateUtils.getDateTime());
        ans.setMessageType(MessageType.MESSAGE_COMMON_MESSAGE);

        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        oos.writeObject(ans);
        System.out.println("<" + msg.getSender() + "> 发送给 <" + msg.getReceiver() + ">的消息已转入留言......");
    }

    public Socket getSocket() {
        return socket;
    }
}
