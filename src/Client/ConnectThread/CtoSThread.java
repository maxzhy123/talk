package Client.ConnectThread;

import common.*;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class CtoSThread extends Thread{
    private Socket socket;
    public CtoSThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println("客户端线程，等待从读取从服务器端发送的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                switch (message.getMesType()){
                    case MessageType.MESSAGE_COMM_MES:
                        System.out.println("\n" + message.getSender() + " 对 " + message.getGetter() + " 说: " + message.getContent());
                        break;
                    case MessageType.MESSAGE_TO_ALL_MES:
                        System.out.println("\n" + message.getSender() + " 对所有人说: " + message.getContent());
                        break;
                    case MessageType.MESSAGE_FILE_MES:
                        System.out.println("\n" + message.getSender() + " 给 " + message.getGetter() + " 发文件: " + message.getSrc() + " 到我的电脑的目录 " + message.getDest());
                        FileOutputStream fos = new FileOutputStream(message.getDest(), true);
                        fos.write(message.getFileBytes());
                        fos.close();
                        System.out.println("\n 保存文件成功~");
                        break;
                    case MessageType.MESSAGE_RET_ONLINE_FRIEND:
                        String[] onlineUsers = message.getContent().split(" ");
                        System.out.println("\n=======当前在线用户列表========");
                        for (int i = 0; i < onlineUsers.length; i++) {
                            System.out.println("用户: " + onlineUsers[i]);
                        }
                        break;
                    default:
                        System.out.println("未知信息类型");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
