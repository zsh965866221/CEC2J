package net.zsh_o.cec4j;

import net.zsh_o.cec4j.server.PipeServer;

import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class Heartbeat {
    private Peer peer;
    private Timer timer;
    private long period;

    private PipeServer pipeServer;

    public Heartbeat(long period, Peer peer) {
        this.pipeServer=peer.pipeServer;
        this.period = period;
        this.peer = peer;
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Iterator iter=peer.getGroupList().entrySet().iterator();
                while(iter.hasNext()){
                    Map.Entry entry=(Map.Entry)iter.next();
                    Group group=(Group) entry.getValue();
                    Iterator iter1=group.getPeerList().entrySet().iterator();
                    while(iter1.hasNext()){
                        Map.Entry entry1=(Map.Entry)iter1.next();
                        RemotePeer remotePeer=(RemotePeer)entry1.getValue();
                        remotePeer.redLife();
                        if(remotePeer.getLife()<=0){
                            group.getPeerList().remove(remotePeer.toString());//在组群列表中删除已经死亡的远程对等体
                        }
                    }
                }

                iter=peer.getRecivePipeList().entrySet().iterator();
                while(iter.hasNext()){
                    Map.Entry entry=(Map.Entry)iter.next();
                    RemotePeer remotePeer=(RemotePeer) entry.getKey();
                    Pipe pipe=(Pipe)entry.getValue();
                    pipe.redLife();
                    if(pipe.getLife()<=0){
                        peer.getRecivePipeList().remove(remotePeer);//管道异常断开,应该触发管道监听异常断开事件
                        pipeServer.getPipeListener().normalDisconnect(remotePeer);
                        peer.pipeServer.getPipeTasks().get(pipe).finash();//关掉此管道监听的线程
                        peer.pipeServer.getPipeTasks().remove(pipe);

                    }
                }
            }
        },1000,period);
    }
}
