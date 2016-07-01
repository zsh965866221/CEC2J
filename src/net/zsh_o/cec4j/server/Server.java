package net.zsh_o.cec4j.server;

import net.zsh_o.cec4j.listener.Listener;

/**
 * Created by zsh96 on 2016/4/11.
 */
public interface Server {
    void registeListener(Listener listener);
}
