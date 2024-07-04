package Client.service;

import Client.ConnectThread.*;
import common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {
    private User user = null;
    private Socket socket;


    public boolean checkUser(String userId, String pwd) {
        user = new User(userId, pwd);
        try {
            //将user对象发送给服务端校验
            socket = new Socket(InetAddress.getLocalHost(), 9905);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            //读取返回的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){
                //登录成功则启动线程维持socket
                CtoSThread ctosThread = new CtoSThread(socket);
                ctosThread.start();
                ThreadManage.add(userId, ctosThread);
                return true;
            }else{
                socket.close();
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //获取在线用户列表
    public void getOnlineList() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());
        try {
            CtoSThread ct = ThreadManage.put(user.getUserId());
            ObjectOutputStream oos = new ObjectOutputStream(ct.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());//一定要指定我是哪个客户端id

        //发送message
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ThreadManage.put(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


