package chat.service;

import chat.common.Message;
import chat.common.MessageType;
import chat.utils.DateUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 提供和消息相关的方法
 */
public class MessageService {
    private Socket socket;

    /**
     * 给某个在线用户发送消息
     *
     * @param content
     * @param userID
     * @param receiverID
     */
    public void sendMessageToOther(String content, String userID, String receiverID) {
        Message message = new Message(userID, receiverID, content,
                MessageType.MESSAGE_COMMON_MESSAGE, DateUtils.getDateTime());

        sendMessage(message, content, userID, receiverID);

    }

    /**
     * 给所有人群发消息
     *
     * @param content
     * @param userID
     */
    public void sendMessageToOthers(String content, String userID) {
        Message message = new Message(userID, null, content,
                MessageType.MESSAGE_TO_ALL, DateUtils.getDateTime());

        sendMessage(message, content, userID, "所有人");
    }

    /**
     * 发送消息
     *
     * @param message
     * @param content
     * @param userID
     * @param receiverID
     */
    private void sendMessage(Message message, String content, String userID, String receiverID) {
        // 获取线程及对应的socket
        socket = ManageClientConnectServerThread.getThread(userID).getSocket();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println("<" + userID + "> 对 <" + receiverID + "> 说：" + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
