package Server.ConnectThread;

import common.Message;
import common.MessageType;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket;
    ;
    private String userId;

    public ClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                switch (message.getMesType()){
                    case MessageType.MESSAGE_RET_ONLINE_FRIEND:
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
