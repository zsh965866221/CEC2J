package net.zsh_o.cec4j.byzantine;

import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.RemotePeer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class PeerCommand{
    String command;
    RemotePeer remotePeer;
    public HashMap<RemotePeer,PeerCommand> peerTreeSet;

    public String finalCommand;


    public PeerCommand(String command, RemotePeer remotePeer) {
        this.command = command;
        this.remotePeer = remotePeer;
        peerTreeSet=new HashMap<>();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public RemotePeer getRemotePeer() {
        return remotePeer;
    }

    public void setRemotePeer(RemotePeer remotePeer) {
        this.remotePeer = remotePeer;
    }

    @Override
    public String toString() {
        if(finalCommand==null){
            return remotePeer.toString()+"--"+command;
        }else{
            return remotePeer.toString()+"--"+command+"==>"+finalCommand;
        }
    }
}
