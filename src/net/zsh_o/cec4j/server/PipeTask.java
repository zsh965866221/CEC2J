package net.zsh_o.cec4j.server;

import net.zsh_o.cec4j.listener.PipeListener;

/**
 * Created by zsh96 on 2016/4/11.
 */
public abstract class PipeTask implements Runnable {
    boolean finash=false;
    PipeListener pipeListener;

    public PipeTask(PipeListener pipeListener) {
        this.pipeListener = pipeListener;
    }
    public  void finash(){
        finash=true;
    }
}
