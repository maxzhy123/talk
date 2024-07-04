package Server.ConnectThread;

import common.Message;
import common.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class StoCThread extends Thread{
    private Socket socket;
    ;
    private String userId;

    private boolean loop = true; //线程存活标志

    public StoCThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (loop){
            try {
                //一直监听消息
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                ObjectOutputStream oos;
                switch (message.getMesType()){
                    case MessageType.MESSAGE_COMM_MES:
                        //根据message.getGetter()转发即可
                        oos = new ObjectOutputStream(ClientThreadManage.get(message.getGetter()).getSocket().getOutputStream());
                        oos.writeObject(message);
                        break;
                    case MessageType.MESSAGE_TO_ALL_MES:
                        //获取目前登录的用户Map，遍历依次发出
                        HashMap<String, StoCThread> threadHashMap = ClientThreadManage.getThreadHashMap();

                        Iterator<String> iterator = threadHashMap.keySet().iterator();
                        while (iterator.hasNext()){
                            String userId = iterator.next();
                            if(userId.equals(message.getSender())) continue; //不给自己发
                            oos = new ObjectOutputStream(ClientThreadManage.get(userId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                        break;
                    case MessageType.MESSAGE_FILE_MES:
                        //根据message.getGetter()转发即可
                        oos = new ObjectOutputStream(ClientThreadManage.get(message.getGetter()).getSocket().getOutputStream());
                        oos.writeObject(message);
                        break;
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND:
                        //通过ClientThreadManage.getOnlineUser();获取在线用户表，并发出
                        String onlineUser = ClientThreadManage.getOnlineUser();
                        Message onlineUserMes = new Message();
                        onlineUserMes.setContent(onlineUser);
                        onlineUserMes.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                        onlineUserMes.setGetter(message.getSender());
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(onlineUserMes);
                        break;
                    case MessageType.MESSAGE_CLIENT_EXIT:
                        System.out.println(message.getSender() + " 退出");
                        ClientThreadManage.remove(message.getSender());
                        socket.close();
                        loop = false; //修改while标志
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
