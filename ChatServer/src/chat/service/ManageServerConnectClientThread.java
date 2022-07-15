package chat.service;

import chat.common.Message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> mp = new HashMap();

    private static ConcurrentHashMap<String, Vector<Message>> concurrentMap = new ConcurrentHashMap<>();

    public ManageServerConnectClientThread() {
    }

    public static void addMap(String id, ServerConnectClientThread thread) {
        mp.put(id, thread);
    }

    public static void addConcurrentMap(String id, Message message) {
        // 该句等同于： concurrentMap.computeIfAbsent(id, k -> new Vector<Message>());
        if (!concurrentMap.containsKey(id))
            concurrentMap.put(id, new Vector<Message>());

        concurrentMap.get(id).add(message);
    }

    public static void removeThread(String id) {
        mp.remove(id);
    }

    public static void clearMessages(String id) {
        concurrentMap.remove(id);
    }

    public static ServerConnectClientThread getThread(String id) {
        return mp.getOrDefault(id, null);
    }

    public static String getOnlineUsers() {
        StringBuilder list = new StringBuilder();
        Iterator var1 = mp.keySet().iterator();

        while (var1.hasNext()) {
            String id = (String) var1.next();
            list.append(id).append(",");
        }

        return list.toString();
    }

    public static HashMap<String, ServerConnectClientThread> getMp() {
        return mp;
    }

    public static ConcurrentHashMap<String, Vector<Message>> getConcurrentMap() {
        return concurrentMap;
    }

    public static Vector<Message> getMessages(String id) {
        if (!concurrentMap.containsKey(id))
            return null;
        return concurrentMap.get(id);
    }
}

