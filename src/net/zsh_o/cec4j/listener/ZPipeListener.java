package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.test.ui.Main;
import net.zsh_o.cec4j.test.ui.RecivePanel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * Created by zsh96 on 2016/4/12.
 */
public class ZPipeListener  implements PipeListener{
    @Override
    public void connect(RemotePeer peer) {
        System.out.println("收到管道连接--"+peer.toString());

    }

    @Override
    public void normalDisconnect(RemotePeer peer) {

    }

    @Override
    public void abnormalDisconnect(RemotePeer peer) {

    }

    @Override
    public void receive(RemotePeer peer, String message) {
        System.out.println("收到管道信息--"+peer.toString()+":"+message);
        RecivePanel panel= Main.MAIN.getPanel(peer);
        try {
            Document doc= DocumentHelper.parseText(message);
            message=(String)doc.getRootElement().getData();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if(panel!=null){
            panel.setText("["+peer.toString()+"]:"+message);
        }else{
            panel=new RecivePanel(Main.MAIN.peer,peer);
            panel.setText("["+peer.toString()+"]:"+message);
            Main.MAIN.tabbedPane.addTab("个人:"+peer.toString(),null,panel);
        }
    }
}
