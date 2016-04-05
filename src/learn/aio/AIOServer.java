package learn.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by zsh96 on 2016/4/5.
 */
public class AIOServer {
    int port =9000;
    AsynchronousServerSocketChannel server;
    AIOServer() throws IOException {
        server=AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
    }
    void startWithFuture() throws ExecutionException, InterruptedException, TimeoutException {
        Future<AsynchronousSocketChannel> future =server.accept();
        AsynchronousSocketChannel socket=future.get();
        ByteBuffer readBuf=ByteBuffer.allocate(1024);
        readBuf.clear();
        socket.read(readBuf).get(100, TimeUnit.SECONDS);
        readBuf.flip();
        System.out.println(Thread.currentThread().getName()+": recive: "+new String(readBuf.array()));
    }
    void startWithCompletionHandler(){
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            final ByteBuffer buffer=ByteBuffer.allocate(1024);
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println(Thread.currentThread().getName()+" start");
                buffer.clear();
                try {
                    result.read(buffer).get(100,TimeUnit.SECONDS);
                    buffer.flip();
                    System.out.println(Thread.currentThread().getName()+": receive: "+new String(buffer.array()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        result.close();
                        server.accept(null,this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println(Thread.currentThread().getName()+": End");
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed: "+exc);
            }
        });
        while(true){
            System.out.println("main thread");
            try {
                Thread.sleep(1000000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        new AIOServer().startWithCompletionHandler();
    }
}
