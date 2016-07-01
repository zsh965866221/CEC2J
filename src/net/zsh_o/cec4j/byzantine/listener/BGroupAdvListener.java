package net.zsh_o.cec4j.byzantine.listener;

import net.zsh_o.cec4j.Group;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.RemotePeer;
import net.zsh_o.cec4j.byzantine.ui.Main;
import net.zsh_o.cec4j.listener.GroupAdvListener;

/**
 * Created by zsh96 on 2016/4/17.
 */
public class BGroupAdvListener extends GroupAdvListener {
    public BGroupAdvListener(Peer peer) {
        super(peer);
    }

    @Override
    public void receive(RemotePeer peer, Group group, String message) {

    }
}
