package net.zsh_o.cec4j;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class Group {
    private boolean joined=false;
    private UUID uuid;
    private String name;
    private HashMap<String,RemotePeer> peerList;

    public Group(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        peerList=new HashMap<>();
    }
    public boolean containsPeer(RemotePeer peer){
        if(peerList.containsKey(peer.toString())){
            return true;
        }else{
            return false;
        }
    }
    public void insertPeer(RemotePeer peer){
        if(!containsPeer(peer)){
            peerList.put(peer.toString(),peer);
        }
    }
    public void deletePeer(RemotePeer peer){
        if(containsPeer(peer)){
            peerList.remove(peer.toString());
        }
    }
    public RemotePeer findPeer(String peer){
        return peerList.get(peer);
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return uuid != null ? uuid.equals(group.uuid) : group.uuid == null;

    }

    public HashMap<String, RemotePeer> getPeerList() {
        return peerList;
    }
}
