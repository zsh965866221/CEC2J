package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.*;
import net.zsh_o.cec4j.test.ui.Main;

/**
 * Created by zsh96 on 2016/4/12.
 */
public class ZGroupAdvListener extends GroupAdvListener {
    public ZGroupAdvListener(Peer peer) {
        super(peer);
    }

    @Override
    public void receive(RemotePeer peer, Group group, String message) {
//        System.out.println("获得组群通告-"+peer.getLife()+"-"+group.toString()+":"+peer.toString()+":"+message);
        if(!peer.equals(this.peer.myself)&&!this.peer.getDefaultGroup().containsPeer(peer)){
            System.out.println("收到新peer:"+peer.toString());
            Main.MAIN.initTreeModel();//刷新组群树
        }
    }
}
