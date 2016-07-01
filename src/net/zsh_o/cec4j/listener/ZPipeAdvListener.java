package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.Pipe;
import net.zsh_o.cec4j.RemotePeer;

/**
 * Created by zsh96 on 2016/4/12.
 */
public class ZPipeAdvListener extends PipeAdvListener {

    @Override
    public void receive(RemotePeer peer, Pipe pipe, String message) {
//        System.out.println("获得管道通告-"+pipe.getLife()+"-"+peer.toString()+":"+message);
    }
}
