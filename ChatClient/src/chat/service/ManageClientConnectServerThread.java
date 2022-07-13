package chat.service;

import java.util.HashMap;

/**
 * 管理客户端连接到服务端线程的类
 */
public class ManageClientConnectServerThread {
    private static HashMap<String, ClientConnectServerThread> mp = new HashMap<>();

    public static void addMap(String userID, ClientConnectServerThread thread) {
        mp.put(userID, thread);
    }

    public static ClientConnectServerThread getThread(String userID) {
        return mp.get(userID);
    }
}
