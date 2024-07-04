package Client.service;

import Client.ConnectThread.ThreadManage;
import common.Message;
import common.MessageType;

import java.io.*;

public class FileClientService {
    public void sendFileToOne(String src, String dest, String senderId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setContent(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        //需要将文件读取
        FileInputStream fis = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];

        try {
            fis = new FileInputStream(src);
            fis.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(ThreadManage.put(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
