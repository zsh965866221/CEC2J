package net.zsh_o.cec4j.byzantine.listener;

import net.zsh_o.cec4j.Message;
import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.byzantine.PeerCommand;
import net.zsh_o.cec4j.byzantine.ui.Main;
import net.zsh_o.cec4j.listener.PipeListener;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zsh96 on 2016/4/17.
 */
public class BPipeListener implements PipeListener {
    @Override
    public void connect(RemotePeer peer) {

    }

    @Override
    public void normalDisconnect(RemotePeer peer) {

    }

    @Override
    public void abnormalDisconnect(RemotePeer peer) {

    }

    @Override
    public void receive(RemotePeer peer, String message) {
        try {
            JTextArea textArea= Main.MAIN.textArea;
            Document doc=DocumentHelper.parseText(message);
            Element root=doc.getRootElement();
            {//将军接收到副官最后传回的指令
                if(Main.MAIN.byzantine.isCommander){
                    Element finalE=root.element("final");
                    if(finalE!=null){
                        String finalcommand= (String) finalE.getData();
                        textArea.setText(textArea.getText()+"\n"+"最终消息:["+peer.toString()+"]: "+finalcommand);
                    }
                    return;
                }
            }
            String ss= (String) root.element("data").getData();
            List el=root.elements("peer"); //////////////////

            if(root.elements("peer").isEmpty()){//收到将军消息---将军先告送其他人自己是将军,然后发送消息,,,防止还没与初始化之前接到其他节点转发的将军的消息
                //以将军初始化组群
                Main.MAIN.byzantine.initCommandsTree(peer);
                PeerCommand cpc=Main.MAIN.byzantine.commandsTree.get(peer);
                Main.MAIN.byzantine.commanderPeer=cpc;
                if(ss.equals("init")){
                    textArea.setText(textArea.getText()+"\n"+peer.toString()+"是将军");
                    return;
                }
                cpc.setCommand(ss);
                textArea.setText(textArea.getText()+"\n"+"[将军--"+peer.toString()+"]: "+ss);
                Main.MAIN.initTreeModel(null);///缺少把将军消息转发出去/////////////////////////
                Element pe= root.addElement("peer");
                pe.setText(peer.toString());
                if(Main.MAIN.byzantine.isTraitor){
                    Main.MAIN.byzantine.sendTraitorMessage(new Message(doc),0,5);
                }else{
                    Main.MAIN.byzantine.sendMessage(new Message(doc));
                }

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //从现在开始计时,20秒之后开始回归算法
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        Main.MAIN.byzantine.startRegress();
                    }
                });
                thread.start();
            }else{//副官
                List pes=root.elements("peer");
                Iterator iterator=pes.iterator();

                PeerCommand cpc=Main.MAIN.byzantine.commanderPeer;
                PeerCommand pc=cpc;
                while(iterator.hasNext()){
                    Element pe= (Element) iterator.next();
                    String speer= (String) pe.getData();
                    if(Main.MAIN.peer.getDefaultGroup().getPeerList().containsKey(speer)){
                        RemotePeer remotePeer=Main.MAIN.peer.getDefaultGroup().getPeerList().get(speer);
                        if(remotePeer.equals(cpc.getRemotePeer()))  continue;//滤过将军本身
                        if(pc.peerTreeSet.containsKey(remotePeer)){
                            pc=pc.peerTreeSet.get(remotePeer);
                        }else{
                            return;
                        }
                    }else{
                        return;
                    }
                }

                if(!pc.peerTreeSet.isEmpty()){//转发

                    if(pc.peerTreeSet.containsKey(peer)) {
                        pc = pc.peerTreeSet.get(peer);
                        pc.setCommand(ss);

                        Element me = root.addElement("peer");
                        me.setText(peer.toString());//向指令中加入上级
                        if (Main.MAIN.byzantine.isTraitor) {
                            Main.MAIN.byzantine.sendTraitorMessage(new Message(doc), 0, 5);
                        } else {
                            Main.MAIN.byzantine.sendMessage(new Message(doc));
                        }
                    }
                }else{
                    pc.setCommand(ss);
                }
//                if(pc.peerTreeSet.containsKey(peer)){
//                    pc=pc.peerTreeSet.get(peer);
//                    pc.setCommand(ss);
//
////                    textArea.setText(textArea.getText()+"\n"+"[副官--"+peer.toString()+"]: "+ss);
////                    Main.MAIN.initTreeModel(null);
//                    if(!pc.peerTreeSet.isEmpty()){//转发
//                        Element me=root.addElement("peer");
//                        me.setText(peer.toString());//向指令中加入上级
//                        if(Main.MAIN.byzantine.isTraitor){
//                            Main.MAIN.byzantine.sendTraitorMessage(new Message(doc),0,5);
//                        }else{
//                            Main.MAIN.byzantine.sendMessage(new Message(doc));
//                        }
//                    }
//                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //处理消息,分割出第几层,把对应消息,写入byzantine中
    }

}
