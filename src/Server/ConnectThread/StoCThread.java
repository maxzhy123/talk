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

    private boolean loop = true;

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
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                ObjectOutputStream oos;
                switch (message.getMesType()){
                    case MessageType.MESSAGE_COMM_MES:
                        oos = new ObjectOutputStream(ClientThreadManage.get(message.getGetter()).getSocket().getOutputStream());
                        oos.writeObject(message);
                        break;
                    case MessageType.MESSAGE_TO_ALL_MES:
                        HashMap<String, StoCThread> threadHashMap = ClientThreadManage.getThreadHashMap();

                        Iterator<String> iterator = threadHashMap.keySet().iterator();
                        while (iterator.hasNext()){
                            String userId = iterator.next();
                            if(userId.equals(message.getSender())) continue;
                            oos = new ObjectOutputStream(ClientThreadManage.get(userId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                        break;
                    case MessageType.MESSAGE_FILE_MES:
                        oos = new ObjectOutputStream(ClientThreadManage.get(message.getGetter()).getSocket().getOutputStream());
                        oos.writeObject(message);
                        break;
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND:
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
                        loop = false;
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
