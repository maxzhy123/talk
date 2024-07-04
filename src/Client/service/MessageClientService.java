package Client.service;

import Client.ConnectThread.ThreadManage;
import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MessageClientService {
    public void sendMessageToAll(String content, String senderId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到message对象

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(ThreadManage.put(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToOne(String content, String senderId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到message对象

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(ThreadManage.put(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
