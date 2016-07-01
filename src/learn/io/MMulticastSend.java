package learn.io;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by zsh96 on 2016/4/9.
 */
public class MMulticastSend {
    final String GROUPADDRESS="224.0.0.2";
    final int PORT=9000;
    public void run() throws IOException {
        InetAddress groupAddress=InetAddress.getByName(GROUPADDRESS);
        MulticastSocket mss=new MulticastSocket(PORT);
        mss.joinGroup(groupAddress);
        Scanner scanner=new Scanner(System.in);
        String ss;
        while (true){
            ss=scanner.nextLine();
            if(ss.equals("q"))  break;
            byte[] buffer=ss.getBytes();
            DatagramPacket packet=new DatagramPacket(buffer,buffer.length,groupAddress,PORT);//需要指定端口和组播地址
            mss.send(packet);
        }
        mss.leaveGroup(groupAddress);
        mss.close();
    }
    public static void main(String[] args){
        MMulticastSend mm=new MMulticastSend();
        try {
            mm.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
