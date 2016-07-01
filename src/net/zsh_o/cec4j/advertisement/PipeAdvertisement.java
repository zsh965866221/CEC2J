package net.zsh_o.cec4j.advertisement;

import net.zsh_o.cec4j.Message;
import net.zsh_o.cec4j.Peer;
import net.zsh_o.cec4j.Pipe;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.util.Timer;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class PipeAdvertisement extends Advertisement {
    private Pipe pipe;

    public PipeAdvertisement(Peer peer,long period, Pipe pipe) throws DocumentException {
        this.timer=new Timer();
        this.pipe = pipe;
        this.peer=peer;
        this.period=period;
        this.name=pipe.toString();
        this.message=new Message(DocumentHelper.parseText("<cec4j><port>"+peer.getServerPort()+"</port><advertisement><pipe>"+ pipe.toString()+"</pipe></advertisement></cec4j>"));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
