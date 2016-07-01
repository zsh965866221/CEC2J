package learn.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by zsh96 on 2016/4/6.
 */
public class UDPServer {
    static final int ECHOMAX=255;
    static int port =9000;
    public static void main(String[] args) throws IOException {
        DatagramSocket socket=new DatagramSocket(port);
        DatagramPacket packet=new DatagramPacket(new byte[ECHOMAX],ECHOMAX);
        while(true){
            socket.receive(packet);
            System.out.println("client: "+packet.getAddress().getHostAddress()+":"+packet.getPort()+": "+new String(packet.getData()));
            socket.send(packet);
            packet.setLength(ECHOMAX);
        }
    }
}
