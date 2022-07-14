package chat.service;

import chat.common.Message;
import chat.common.MessageType;
import chat.utils.DateUtils;
import chat.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class AnnounceToAllUsers implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("请输入服务器要推送的消息（输入exit退出推送）：");
            String news = Utility.readString(1000);
            if (news.equals("exit"))
                break;

            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(DateUtils.getDateTime());
            message.setMessageType(MessageType.MESSAGE_TO_ALL);

            // 遍历群发公告
            HashMap<String, ServerConnectClientThread> mp = ManageServerConnectClientThread.getMp();
            for (HashMap.Entry<String, ServerConnectClientThread> entry : mp.entrySet()) {
                try {
                    message.setReceiver(entry.getKey());
                    ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
