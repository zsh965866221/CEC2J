package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.Group;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.RemotePeer;

/**
 * Created by zsh96 on 2016/4/11.
 */
public abstract class GroupAdvListener implements AdvListener {
    public String uuid;
    public Peer peer;

    @Override
    public void receive(RemotePeer peer, String message) {

    }

    public GroupAdvListener(Peer peer) {
        this.peer = peer;
    }

    public abstract void receive(RemotePeer peer, Group group, String message);
    void joinGroup(Group group, RemotePeer peer){
        group.insertPeer(peer);
    }
    void refreshLife(RemotePeer peer){
        peer.recoveryLife();
    }
}
