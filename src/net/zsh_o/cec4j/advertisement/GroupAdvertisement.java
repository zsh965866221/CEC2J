package net.zsh_o.cec4j.advertisement;

import net.zsh_o.cec4j.Group;
import net.zsh_o.cec4j.Message;
import net.zsh_o.cec4j.Peer;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.util.Timer;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class GroupAdvertisement extends Advertisement {
    private Group group;

    public GroupAdvertisement(Peer peer,Group group,long period) throws DocumentException {
        this.timer=new Timer();
        this.group = group;
        this.period=period;
        this.peer=peer;
        this.name=group.getUuid().toString();
        this.message=new Message(DocumentHelper.parseText("<cec4j><port>"+peer.getServerPort()+"</port><advertisement><group>"+this.name+"</group></advertisement></cec4j>"));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
