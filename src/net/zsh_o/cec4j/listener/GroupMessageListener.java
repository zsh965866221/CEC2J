package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.Group;
import net.zsh_o.cec4j.RemotePeer;

/**
 * Created by zsh96 on 2016/4/11.
 */
public abstract class GroupMessageListener implements Listener {
    @Override
    public void receive(RemotePeer peer, String message) {

    }

    public abstract void receive(RemotePeer peer, Group group, String message);
}
