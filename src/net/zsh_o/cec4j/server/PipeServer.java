package net.zsh_o.cec4j.server;

import net.zsh_o.cec4j.listener.Listener;
import net.zsh_o.cec4j.listener.PipeListener;
import net.zsh_o.cec4j.Message;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.Pipe;
import net.zsh_o.cec4j.RemotePeer;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class PipeServer implements Server {
    private Peer peer;
    HashMap<Pipe,PipeTask> pipeTasks;

    private PipeListener pipeListener;

    public PipeServer(Peer peer) {
        System.out.println("初始化PipeServer");
        this.peer = peer;
        pipeTasks=new HashMap<>();
    }
    public void start(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Socket socket;
                    try {
//                        System.out.println("管道监听开始");
                        socket=peer.getServerSocket().accept();//建立pipe
                        DataInputStream inputStream=new DataInputStream(socket.getInputStream());
                        String sport=inputStream.readUTF();
//                        System.out.println("接收到管道");
                        String speer=socket.getInetAddress().getHostAddress()+":"+sport;
                        if(!peer.getDefaultGroup().getPeerList().containsKey(speer)){//过滤掉不属于此对等网络的请求
                            continue;
                        }
                        RemotePeer remotePeer=peer.getDefaultGroup().getPeerList().get(speer);
//                        if(peer.getPipeList().containsKey(remotePeer)){//滤过已存在的管道
//                            continue;
//                        }
                        Pipe pipe=new Pipe(peer,socket,10,Integer.valueOf(sport));
                        pipeListener.connect(pipe.getPeer());
                        DataInputStream input =new DataInputStream(socket.getInputStream());
                        PipeTask pipeTask=new PipeTask(pipeListener){
                            @Override
                            public void run() {
                                while(!this.finash){
//                                    if(socket.isClosed()){
//                                        pipeListener.normalDisconnect(pipe.getPeer());
//                                        peer.station.delete(pipe.toString());
//                                    }else{
                                        try {
                                            if(socket.isOutputShutdown()||socket.isClosed()){
                                                finash=true;
                                                break;
                                            }
                                            String sxml=input.readUTF();
                                            Message message=new Message(DocumentHelper.parseText(sxml));
                                            pipeListener.receive(pipe.getPeer(),sxml);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (DocumentException e) {
                                            e.printStackTrace();
                                        }
//                                    }
                                }
                            }
                        };
                        Thread pipeThread=new Thread(pipeTask);
                        pipeThread.start();
                        pipeTasks.put(pipe,pipeTask);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public HashMap<Pipe, PipeTask> getPipeTasks() {
        return pipeTasks;
    }

    @Override
    public void registeListener(Listener listener) {
        if(listener instanceof PipeListener){
            pipeListener=(PipeListener)listener;
        }
    }

    public PipeListener getPipeListener() {
        return pipeListener;
    }
}
