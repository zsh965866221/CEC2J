package net.zsh_o.cec4j;

import java.net.InetAddress;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class RemotePeer {
    private InetAddress address;
    private int port;//应该是tcp的端口

    private int life;
    private int maxlife;

    public RemotePeer(InetAddress address, int port,int maxlife) {
        this.address = address;
        this.port = port;

        life=this.maxlife=maxlife;
    }

    public int getLife() {
        return life;
    }

    public int getMaxlife() {
        return maxlife;
    }

    @Override
    public String toString() {
        return address.getHostAddress()+":"+port;
    }

    @Override
    public boolean equals(Object o) {
        if(((RemotePeer)o).address.getHostAddress().equals(this.address.getHostAddress())&&((RemotePeer)o).port==this.port){
            return true;
        }else{
            return false;
        }
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int redLife(){
        return --life;
    }
    public void recoveryLife(){
        life=maxlife;
    }
}