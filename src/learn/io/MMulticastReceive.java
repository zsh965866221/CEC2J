package learn.io;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zsh96 on 2016/4/9.
 */
public class MMulticastReceive {
    final String GROUPADDRESS="224.0.0.2";
    final int PORT=9000;
     boolean cc=true;
    Timer timer;
    public void run() throws IOException {
        InetAddress groupAddress=InetAddress.getByName(GROUPADDRESS);
        MulticastSocket msr=new MulticastSocket(PORT);
        msr.joinGroup(groupAddress);
        byte[] buffer=new byte[1024];
        while(cc){
            DatagramPacket dp=new DatagramPacket(buffer,buffer.length);
            msr.receive(dp);
            System.out.println(dp.getAddress().getHostAddress()+":"+dp.getPort()+": "+new String(dp.getData()));
        }
        msr.leaveGroup(groupAddress);
        msr.close();
    }
    public static void main(String[] args){
        MMulticastReceive mm=new MMulticastReceive();
        try {
            mm.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
