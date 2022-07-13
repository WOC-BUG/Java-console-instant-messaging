package chat.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import chat.common.Message;

public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userID;

    public ServerConnectClientThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    public void run() {
        while(true) {
            System.out.println("服务端和<" + this.userID + ">保持通讯，读取数据...");

            try {
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                Message msg = (Message)ois.readObject();
                if (msg.getMessageType().equals("5")) {
                    System.out.println(msg.getSender() + "需要在线用户列表：");
                    msg.setContent(ManageServerConnectClientThread.getOnlineUsers());
                    msg.setReceiver(msg.getSender());
                    msg.setSender(InetAddress.getLocalHost().toString());
                    oos.writeObject(msg);
                } else {
                    if (msg.getMessageType().equals("6")) {
                        System.out.println("用户<" + msg.getSender() + ">退出系统！");
                        ManageServerConnectClientThread.removeThread(msg.getSender());
                        this.socket.close();
                        return;
                    }

                    System.out.println("不处理其他类型的消息");
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }
}
