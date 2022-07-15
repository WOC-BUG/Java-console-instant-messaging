package chat.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import chat.common.Message;
import chat.common.MessageType;
import chat.common.User;

public class QQService {
    private ServerSocket serverSocket;
    private static ConcurrentHashMap<String, User> validUser = new ConcurrentHashMap();

    static {
        validUser.put("昆", new User("昆", "123456"));
        validUser.put("夜", new User("夜", "654321"));
        validUser.put("蕾哈尔", new User("蕾哈尔", "000000"));
    }

    public QQService() {
        try {
            this.serverSocket = new ServerSocket(9999);
            System.out.println("服务器在9999端口监听......");
            new Thread(new AnnounceToAllUsers()).start();

            while (true) {
                Socket socket = this.serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                Message msg = new Message();

                if (this.checkUser(user.getUserId(), user.getPassword())) {
                    System.out.println("用户<" + user.getUserId() + ">登录成功！");
                    msg.setMessageType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    oos.writeObject(msg);

                    // 加入线程
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    ManageServerConnectClientThread.addMap(user.getUserId(), serverConnectClientThread);

                    // 发送缓存的留言
                    Vector<Message> messages = ManageServerConnectClientThread.getMessages(user.getUserId());
                    if (messages != null) {
                        for (Message message : messages) {
                            ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                            oos2.writeObject(message);
                        }
                        ManageServerConnectClientThread.clearMessages(user.getUserId());
                        System.out.println("用户<" + user.getUserId() + ">的留言已发送！");
                    }
                } else {
                    System.out.println("用户<" + user.getUserId() + ">登录失败！");
                    msg.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(msg);
                    socket.close();
                }
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            try {
                this.serverSocket.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }
        }

    }

    public boolean checkUser(String userID, String password) {
        User user = (User) validUser.get(userID);
        if (user == null) {
            return false;
        } else {
            return user.getPassword().equals(password);
        }
    }
}
