package Server.service;

import Server.ConnectThread.StoCThread;
import Server.ConnectThread.ClientThreadManage;
import common.Message;
import common.MessageType;
import common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TalkService {
    private ServerSocket serverSocket= null;
    static HashMap<String, String> userTable = new HashMap<>();
    static {
        userTable.put("mamoz", "mamoz");
        userTable.put("lbz", "123456");
        userTable.put("keniass", "021018");
    }

    private boolean checkUser(String userId, String passwd) {
        String pwd = userTable.get(userId);
        //过关的验证方式
        if(pwd == null) {
            return  false;
        }
        if(!pwd.equals(passwd)) {
            return false;
        }
        return  true;
    }
    public TalkService() {
        try {
            serverSocket = new ServerSocket(9905);
            while (true){
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                if(checkUser(user.getUserId(), user.getPasswd())){
                    System.out.println(user.getUserId() + "已登录");
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    StoCThread stoCThread = new StoCThread(socket, user.getUserId());
                    stoCThread.start();
                    ClientThreadManage.add(user.getUserId(), stoCThread);
                } else {
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    oos.close();
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
