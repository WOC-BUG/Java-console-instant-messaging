package chat.service;

import chat.common.Message;
import chat.common.MessageType;
import chat.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 用户登录、验证、注册等功能
 */
public class UserClientService {

    private User u = new User();
    Socket socket;

    /**
     * 验证用户名、密码是够合法
     *
     * @param name
     * @param password
     */
    public boolean checkUser(String name, String password) {
        boolean flag = false;
        u.setUserId(name);
        u.setPassword(password);

        // 连接服务端，发送用户对象
        try {
            socket = new Socket(InetAddress.getByName("192.168.0.116"), 9999);

            // 发送对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            // 读取返回信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();
            String type = msg.getMessageType();
            if (type.equals(MessageType.MESSAGE_LOGIN_SUCCESS)) { // 登录成功
                flag = true;
                // 启动一个用于通讯的线程，让线程持有一个socket
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                ManageClientConnectServerThread.addMap(name, clientConnectServerThread);
            } else {    // 登录失败就关闭socket
                socket.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 向服务器请求在线用户列表
     */
    public void getOnlineFriendList() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_RETURN_OLINE_FRIEND);
        message.setSender(u.getUserId());

        // 获取当前线程的socket对应的ObjectOutputStream
        try {
//            ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(u.getUserId());
//            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送退出系统的消息
     * 即断开某个用户的socket连接
     */
    public void logout() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println("退出系统......");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
