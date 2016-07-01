package net.zsh_o.cec4j.listener;

import net.zsh_o.cec4j.RemotePeer;

/**
 * Created by zsh96 on 2016/4/11.
 */
public interface PipeListener extends Listener {
    void connect(RemotePeer peer);
    void normalDisconnect(RemotePeer peer);
    void abnormalDisconnect(RemotePeer peer);
}
