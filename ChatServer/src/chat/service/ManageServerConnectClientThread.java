package chat.service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> mp = new HashMap();

    public ManageServerConnectClientThread() {
    }

    public static void addMap(String id, ServerConnectClientThread thread) {
        mp.put(id, thread);
    }

    public static void removeThread(String id) {
        mp.remove(id);
    }

    public static ServerConnectClientThread getThread(String id) {
        return (ServerConnectClientThread)mp.get(id);
    }

    public static String getOnlineUsers() {
        StringBuilder list = new StringBuilder();
        Iterator var1 = mp.keySet().iterator();

        while(var1.hasNext()) {
            String id = (String)var1.next();
            list.append(id).append(",");
        }

        return list.toString();
    }

    public static HashMap<String, ServerConnectClientThread> getMp() {
        return mp;
    }
}

