package chat.service;

import chat.common.Message;
import chat.common.MessageType;
import chat.utils.DateUtils;

import java.io.*;

/**
 * 文件传输服务
 */
public class FileService {
    public void sendFileToOther(String src, String des, String userID, String receiverID) {
        Message message = new Message(userID, receiverID, null, MessageType.MESSAGE_FILE, DateUtils.getDateTime());
        message.setFileSrc(src);
        message.setFileDes(des);

        // 读取文件
        File file = new File(src);
        int len = (int) file.length();
        byte[] fileBytes = new byte[len];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src);
            fis.read(fileBytes);

            message.setFileBytes(fileBytes);
            message.setFileLen(fileBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n" + userID + "给" + receiverID + "发送文件：" + src + "，到对方的目录：" + des);

        // 发送包含文件的Message
        try {
            ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(userID);
            ObjectOutputStream bos = new ObjectOutputStream(thread.getSocket().getOutputStream());
            bos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
