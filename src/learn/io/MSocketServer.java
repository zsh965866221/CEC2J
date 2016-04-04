package learn.io;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zsh96 on 2016/4/4.
 */
public class MSocketServer {
    ServerSocket serverSocket;

    public static void main(String[] args){
        MSocketServer server=new MSocketServer();
        try {
            server.serverSocket=new ServerSocket(9001);
            server.service();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void service(){
        while(true){
            Socket socket =null;
            try {
                socket=serverSocket.accept();
                new Thread(new runner(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class runner implements Runnable{
        Socket client;

        public runner(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            DataInputStream input;
            DataOutputStream output;
            try {
                input=new DataInputStream(client.getInputStream());
                output=new DataOutputStream(client.getOutputStream());

                String msgs=input.readUTF();
                output.writeUTF("Recive: "+msgs+"  \r\n ...");
                System.out.println("Recive: "+msgs);
                msgs=input.readUTF();
                output.writeUTF("Recive: "+msgs+"  \r\n ...");
                System.out.println("Recive: "+msgs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
