package learn.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by zsh96 on 2016/4/4.
 */
public class MClient {
    public static void main(String[] args){
        Socket socket=null;
        try {
            socket=new Socket("127.0.0.1",9001);
            OutputStream out=socket.getOutputStream();
            DataOutputStream doc=new DataOutputStream(out);
            DataInputStream in=new DataInputStream(socket.getInputStream());

            Scanner scanner=new Scanner(System.in);

            String ss="aa";
            while(!ss.equals("")){
                ss=scanner.nextLine();
                doc.writeUTF(ss);
                String res=in.readUTF();
                System.out.println(res);
            }
            doc.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
