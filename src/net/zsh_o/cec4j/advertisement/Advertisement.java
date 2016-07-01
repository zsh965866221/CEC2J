package net.zsh_o.cec4j.advertisement;

import net.zsh_o.cec4j.Message;
import net.zsh_o.cec4j.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zsh96 on 2016/4/11.
 */
public abstract class Advertisement {
    public Peer peer;
    public  Timer timer;
    public long period;
    public Message message;

    public String name;

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String sxml=message.getDocument().asXML();
                byte[] buffer=sxml.getBytes();
                DatagramPacket packet=new DatagramPacket(buffer,buffer.length,peer.getMulticastAddress(),peer.getMulticastPort());
                try {
                    peer.getMulticastSocket().send(packet,(byte)100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },1000,period);
    }

    public void cancel() {
        timer.cancel();
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return name;
    }
}
