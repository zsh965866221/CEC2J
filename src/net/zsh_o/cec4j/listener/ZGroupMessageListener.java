package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.Group;
import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.test.ui.Main;
import net.zsh_o.cec4j.test.ui.RecivePanel;

/**
 * Created by zsh96 on 2016/4/12.
 */
public class ZGroupMessageListener extends GroupMessageListener {
    @Override
    public void receive(RemotePeer peer, Group group, String message) {
        System.out.println("收到组群信息--"+group.toString()+":"+message);
        RecivePanel panel= Main.MAIN.getPanel(group);
        if(panel!=null){
            panel.setText("["+peer.toString()+"]:"+message);
        }else{
            panel=new RecivePanel(Main.MAIN.peer,group);
            panel.setText("["+peer.toString()+"]:"+message);
            Main.MAIN.tabbedPane.addTab("组群:"+group.toString(),null,panel);
        }
    }
}
