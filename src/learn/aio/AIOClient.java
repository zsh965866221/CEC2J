package learn.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;

/**
 * Created by zsh96 on 2016/4/5.
 */
public class AIOClient {
    public static void main(String[] args) throws IOException {
        AsynchronousSocketChannel client=AsynchronousSocketChannel.open();
        client.connect(new InetSocketAddress("localhost",9000));
        String ss;
        Scanner scanner=new Scanner(System.in);
        while (true){
            ss=scanner.nextLine();
            client.write(ByteBuffer.wrap(ss.getBytes()));
        }
    }
}
