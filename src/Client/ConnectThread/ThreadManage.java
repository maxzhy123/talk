package Client.ConnectThread;

import java.util.HashMap;

public class ThreadManage {
    private static HashMap<String, CtoSThread> threadHashMap = new HashMap<>();
    public static void add(String userId, CtoSThread ctosThread) {
        threadHashMap.put(userId, ctosThread);
    }

    public static CtoSThread put(String userId) {
        return threadHashMap.get(userId);
    }
}
