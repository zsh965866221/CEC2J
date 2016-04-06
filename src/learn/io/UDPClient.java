package learn.io;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * Created by zsh96 on 2016/4/6.
 */
public class UDPClient {
    static final int TIMEOUT=3000;
    static final int MAXTRIES=5;
    static final int PORT=9000;
    public static void main(String[] args) throws IOException {
        InetAddress serverAddress=InetAddress.getByName("localhost");
        byte[] bytesSend="send1".getBytes();
        DatagramSocket socket=new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);
        DatagramPacket sendPacket=new DatagramPacket(bytesSend,bytesSend.length,serverAddress,PORT);
        DatagramPacket receivePacket=new DatagramPacket(new byte[bytesSend.length],bytesSend.length);

        int tries=0;
        boolean reciveResponse=false;
        String ss;
        Scanner scanner=new Scanner(System.in);
        while(true){
            do{
                ss=scanner.nextLine();
                sendPacket.setData(ss.getBytes());
                socket.send(sendPacket);
                socket.receive(receivePacket);
                if(!receivePacket.getAddress().equals(serverAddress)){
                    System.out.println("收到未知源");
                    continue;
                }
                reciveResponse=true;
                tries+=1;
                System.out.println("time out,"+(MAXTRIES-tries)+" more tries...");
            }while(!reciveResponse&&(tries<MAXTRIES));
            if(reciveResponse){
                System.out.println("recieved: "+new String(receivePacket.getData()));
            }else{
                System.out.println("No response -- giving up.");
            }
        }

//        socket.close();
    }
}
