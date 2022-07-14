package chat.service;

import chat.common.Message;
import chat.common.MessageType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 不断接收服务端传来的消息
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 由于要一直在后台保持通信，因此使用while循环
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message) ois.readObject();   // 若服务端没有发送数据，则程序会阻塞在这里

                // 判断msg类型，做响应处理
                if (msg.getMessageType().equals(MessageType.MESSAGE_RETURN_OLINE_FRIEND)) {
                    String[] users = msg.getContent().split(",");
                    System.out.println("\n当前在线用户列表：");
                    for (int i = 0; i < users.length; i++) {
                        System.out.println("用户" + (i + 1) + "：" + users[i]);
                    }
                }
                // 接收消息
                else if (msg.getMessageType().equals(MessageType.MESSAGE_COMMON_MESSAGE) ||
                        msg.getMessageType().equals(MessageType.MESSAGE_TO_ALL)) {
                    System.out.println(msg.getSender() + "\t" + msg.getSendTime() + "\n\t" + msg.getContent());
                }
                // 接收文件
                else if (msg.getMessageType().equals(MessageType.MESSAGE_FILE)) {
                    System.out.println(msg.getSender() + "给" + msg.getReceiver() + "发送了文件：" + msg.getFileSrc() + "，保存到：" + msg.getFileDes());

                    FileOutputStream fos = new FileOutputStream(msg.getFileDes());
                    fos.write(msg.getFileBytes(), 0, msg.getFileLen());
                    fos.close();
                    System.out.println("保存文件成功");
                } else {
                    System.out.println("不处理其他类型的消息");
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
