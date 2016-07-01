package net.zsh_o.cec4j.byzantine;

import net.zsh_o.cec4j.Message;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.Pipe;
import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.byzantine.ui.Main;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import sun.security.x509.IPAddressName;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by zsh96 on 2016/4/17.
 */
public class Byzantine {
    Peer peer;
    public boolean isCommander;
    public boolean isTraitor;
    public String sendMessage;
    public int traitorNumber;//叛徒数量

    public PeerCommand commanderPeer;

    public HashMap<RemotePeer,PeerCommand> commandsTree;
    public Byzantine(Peer peer, boolean isCommander, boolean isTraitor,int traitorNumber) {
        this.peer = peer;
        this.isCommander = isCommander;
        this.isTraitor = isTraitor;
        this.traitorNumber=traitorNumber;

        commandsTree=new HashMap<>();

        JTextArea textArea=Main.MAIN.textArea;
        if(isCommander){//如果是将军向其他人发送指令
            textArea.setText(textArea.getText()+"\n"+"本节点为将军");
            Main.MAIN.frame.setTitle(Main.MAIN.frame.getTitle()+"--将军");
            //告送别人自己是将军--三秒之后告送别人自己是将军
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        sendMessage(getMessage("init"));
                        textArea.setText(textArea.getText()+"\n"+"开始通知其他副官...");

                        Thread.sleep(3000);
                        //6秒之后发送将军指令
                        sendCommanderCommand();
                        textArea.setText(textArea.getText()+"\n"+"开始发送将军指令...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        }else{
            textArea.setText(textArea.getText()+"\n"+"本节点为副官");
            Main.MAIN.frame.setTitle(Main.MAIN.frame.getTitle()+"--副官");
        }
        if(isTraitor){
            textArea.setText(textArea.getText()+"\n"+"本节点为叛徒");
            Main.MAIN.frame.setTitle(Main.MAIN.frame.getTitle()+"--叛徒");
        }

    }

    public void sendCommanderCommand(){//过三秒之后发送将军指令
        if(isTraitor){//司令是叛徒,发送随机消息
            Message message= getMessage(getTraitorCommand(0,5));
            sendTraitorMessage(message,0,5);
        }else{
            Message message=getMessage(sendMessage);
            sendMessage(message);
        }
    }
    public void initCommandsTree(RemotePeer commander){
        Object[] to= peer.getDefaultGroup().getPeerList().values().toArray();
        RemotePeer[] peers= new RemotePeer[to.length];
        for(int i=0;i<to.length;i++){
            peers[i]=(RemotePeer)to[i];
        }
        for(int i=0;i<peers.length;i++){
            if(peers[i].equals(peer.myself)||peers[i].equals(commander)){
                peers[i]=null;
            }
        }

        PeerCommand tpc=new PeerCommand("",commander);
        commandsTree.put(commander,tpc);

        _insert(tpc,peers,traitorNumber);
    }
    void _insert(PeerCommand pc,RemotePeer[] peers,int n){
        if(n>0){
            n--;
            for(int i=0;i<peers.length;i++){
                if(peers[i]==null)  continue;
                RemotePeer p=peers[i];
                PeerCommand tpc=new PeerCommand("",p);
                pc.peerTreeSet.put(p,tpc);
                peers[i]=null;
                _insert(tpc,peers,n);
                peers[i]=p;
            }
        }
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    /*
    * 向其他所有节点发送消息
    * */
    public void sendMessage(Message message){
        JTextArea textArea=Main.MAIN.textArea;
        Iterator iterator= peer.getDefaultGroup().getPeerList().entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry=(Map.Entry) iterator.next();
            RemotePeer remotePeer=(RemotePeer) entry.getValue();
            if(!remotePeer.equals(peer.myself)){
                if(peer.getSendPipeList().containsKey(remotePeer)){//存在管道
                    Pipe pipe=peer.getSendPipeList().get(remotePeer);
                    try {
                        pipe.sendMessage(message);
//                        textArea.setText(textArea.getText()+"\n"+"发送:["+remotePeer.toString()+"]: "+(String)message.getDocument().getRootElement().element("data").getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{//不存在,创建管道
                    try {
                        Pipe pipe=new Pipe(peer,remotePeer,5);
                        pipe.sendMessage(message);
//                        textArea.setText(textArea.getText()+"\n"+"发送:["+remotePeer.toString()+"]: "+(String)message.getDocument().getRootElement().element("data").getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    public void sendTraitorMessage(Message message,int a,int b){
        JTextArea textArea=Main.MAIN.textArea;
        Iterator iterator= peer.getDefaultGroup().getPeerList().entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry=(Map.Entry) iterator.next();
            RemotePeer remotePeer=(RemotePeer) entry.getValue();
            if(!remotePeer.equals(peer.myself)){
                Element root=message.getDocument().getRootElement();
                Element ed=root.element("data");
                ed.setText(getTraitorCommand(a,b));
                if(peer.getSendPipeList().containsKey(remotePeer)){//存在管道
                    Pipe pipe=peer.getSendPipeList().get(remotePeer);
                    try {
                        pipe.sendMessage(message);
//                        textArea.setText(textArea.getText()+"\n"+"发送:["+remotePeer.toString()+"]: "+(String)message.getDocument().getRootElement().element("data").getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{//不存在,创建管道
                    try {
                        Pipe pipe=new Pipe(peer,remotePeer,5);
                        pipe.sendMessage(message);
//                        textArea.setText(textArea.getText()+"\n"+"发送:["+remotePeer.toString()+"]: "+(String)message.getDocument().getRootElement().element("data").getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Message getMessage(String command){
        Document doc=DocumentHelper.createDocument();
        Element e=doc.addElement("cec4j");

        Element de=e.addElement("data");
        de.setText(command);

        return new Message(doc);
    }
    public String getTraitorCommand(int a,int b){
        Random random=new Random();
        return String.valueOf(a+random.nextInt(b-a+1));
    }

    String majority(String[] ls,String dc){//找出命令最多的一个,dc:默认命令
        String max=dc;int t=0;
        HashMap<String,Integer> hs=new HashMap<>();
        for(int i=0;i<ls.length;i++){
            if(hs.containsKey(ls[i])){
                int k=hs.get(ls[i]);
                hs.put(ls[i],k+1);
            }else{
                hs.put(ls[i],1);
            }
            if(hs.get(ls[i])>t){
                max=ls[i];
                t=hs.get(ls[i]);
            }
        }
        int k=0;
        for (int a :
                hs.values()) {
            if(a==t)    k++;
        }
        if(k>1)    return dc;//最大值个数大于一个则没有最大值,返回默认值
        else{
            return max;
        }
    }

    public void startRegress(){//开始回归计算-------采用后续遍历方法
        JTextArea textArea=Main.MAIN.textArea;
        textArea.setText(textArea.getText()+"\n"+"开始回归计算");
        _travel(commanderPeer);
        Main.MAIN.initTreeModel(commanderPeer.finalCommand);
        textArea.setText(textArea.getText()+"\n"+"最终指令: "+commanderPeer.finalCommand);
        //向将军发回最终消息
        try {
            Pipe pipe;
            if(peer.getSendPipeList().containsKey(commanderPeer.getRemotePeer())){
                pipe=peer.getSendPipeList().get(commanderPeer.getRemotePeer());
            }else{
                pipe=new Pipe(peer,commanderPeer.getRemotePeer(),5);
            }
            pipe.sendMessage(new Message(DocumentHelper.parseText("<cec4j><data> </data><final>"+commanderPeer.finalCommand+"</final></cec4j>")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    public void _travel(PeerCommand pc){
        if(!pc.peerTreeSet.isEmpty()){
            Iterator iterator=pc.peerTreeSet.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry entry= (Map.Entry) iterator.next();
                PeerCommand tpc= (PeerCommand) entry.getValue();
                _travel(tpc);

                String[] ss=new String[pc.peerTreeSet.size()+1];
                int t=0;
                Iterator iterator1=pc.peerTreeSet.entrySet().iterator();
                while(iterator1.hasNext()){
                    Map.Entry entry1= (Map.Entry) iterator1.next();
                    PeerCommand tpc1= (PeerCommand) entry1.getValue();
                    ss[t++]=tpc1.command;
                }
                ss[t]=pc.command;
                pc.finalCommand=majority(ss,"0");
            }
        }else{
            pc.finalCommand=pc.command;
        }

    }
}
