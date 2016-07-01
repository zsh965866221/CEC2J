package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.Pipe;
import net.zsh_o.cec4j.RemotePeer;

/**
 * Created by zsh96 on 2016/4/11.
 */
public abstract class PipeAdvListener implements AdvListener {
    void refreshLife(Pipe pipe){
        pipe.recoveryLife();
    }

    public abstract void receive(RemotePeer peer,Pipe pipe, String message);
    @Override
    public void receive(RemotePeer peer, String message) {

    }
}
