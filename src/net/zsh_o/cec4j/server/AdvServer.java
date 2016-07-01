package net.zsh_o.cec4j.server;

import net.zsh_o.cec4j.*;
import net.zsh_o.cec4j.listener.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

/**
 * Created by zsh96 on 2016/4/11.
 * 先注册监听器,然后开始
 */
public class AdvServer implements Server {
    private Peer peer;
    private ArrayList<Thread> advListenerThreads;

    GroupAdvListener groupAdvListener;
    ServiceAdvListener serviceAdvListener;
    PipeAdvListener pipeAdvListener;
    GroupMessageListener groupMessageListener;

    private byte[] buffer;
    @Override
    public void registeListener(Listener listener) {
        if(listener instanceof GroupAdvListener){
            groupAdvListener=(GroupAdvListener) listener;
        }else if(listener instanceof  ServiceAdvListener){
            serviceAdvListener=(ServiceAdvListener)listener;
        }else if(listener instanceof PipeAdvListener){
            pipeAdvListener=(PipeAdvListener)listener;
        }else if(listener instanceof GroupMessageListener){
            groupMessageListener=(GroupMessageListener)listener;
        }
    }

    public AdvServer(Peer peer) {
        this.peer = peer;
        buffer=new byte[10240];
        advListenerThreads=new ArrayList<>();

    }

    public void start(){
        split();
    }

    private void split(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    DatagramPacket dp=new DatagramPacket(buffer,buffer.length);
                    try {
                        peer.getMulticastSocket().receive(dp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String sxml=new String(dp.getData());
                    /*
                    * 添加字符串处理切出xml字符内容
                    * */
                    int k=sxml.indexOf("</cec4j>");
                    sxml=sxml.substring(0,k+8);

                    Document doc=null;
                    try {
                        doc= DocumentHelper.parseText(sxml);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    if(doc!=null){
                        Element root= doc.getRootElement();
                        Element portEle=root.element("port");

                        int port= Integer.valueOf((String)portEle.getData());
                        String speer=dp.getAddress().getHostAddress()+":"+port;

                        RemotePeer remotepeer=peer.getRemotePeer(speer);
                        if(remotepeer==null){
                            remotepeer=new RemotePeer(dp.getAddress(),port,3);
                        }


                        Element advEle=root.element("advertisement");
                        Element messageEle=root.element("message");
                        if(advEle!=null){
                            Element groupEle=advEle.element("group");
                            Element serviceEle=advEle.element("service");
                            Element pipeEle=advEle.element("pipe");
                            if(groupEle!=null){
                                String uuid=(String)groupEle.getData();
                                //查看是否有此组群
                                if(peer.getGroupList().containsKey(uuid)){//有此组群
                                    Group tg=peer.getGroupList().get(uuid);
                                    if(tg.getPeerList().containsKey(remotepeer.toString())){//查看此组群是否有此对等体
                                        //刷新生命值
                                        remotepeer.recoveryLife();
                                    }else{//不存在
                                        tg.insertPeer(remotepeer);
                                    }
                                    groupAdvListener.receive(remotepeer,tg,uuid);
                                }else{
                                    try {
                                        peer.insertGroup(uuid);
                                    } catch (DocumentException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }else if(serviceEle!=null){
                                serviceAdvListener.receive(remotepeer,(String)serviceEle.getData());
                            }else if(pipeEle!=null){
                                if(peer.getRecivePipeList().containsKey(remotepeer)){
                                    Pipe pipe=peer.getRecivePipeList().get(remotepeer);
                                    pipeAdvListener.receive(remotepeer,pipe,(String)pipeEle.getData());
                                    pipe.recoveryLife();//管道列表中有此管道刷新管道生命值
                                }
                            }
                        }else if(messageEle!=null){//收到所在组群的消息,处理
                            String uuid=(String)messageEle.element("group").getData();
                            if(peer.getGroupList().containsKey(uuid)){
                                Group group=peer.getGroupList().get(uuid);
                                if(group.isJoined()){
                                    groupMessageListener.receive(remotepeer,group,(String)messageEle.element("data").getData());
                                }
                            }

                        }
                    }

                }
            }
        });
        thread.start();
        advListenerThreads.add(thread);

    }
}
