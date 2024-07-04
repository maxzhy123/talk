package Server.ConnectThread;

import java.util.HashMap;
import java.util.Set;

public class ClientThreadManage {
    //在线用户Map
    private static HashMap<String, StoCThread> threadHashMap = new HashMap<>();
    public static void add(String userId, StoCThread stoCThread) {
        threadHashMap.put(userId, stoCThread);
    }

    public static void remove(String userId) {
        threadHashMap.remove(userId);
    }

    public static StoCThread get(String userId) {
        return threadHashMap.get(userId);
    }

    public static HashMap<String, StoCThread> getThreadHashMap() {
        return threadHashMap;
    }

    public static String getOnlineUser(){
        Set<String> users = threadHashMap.keySet();
        return users.toString();
    }
}
