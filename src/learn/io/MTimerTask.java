package learn.io;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zsh96 on 2016/4/6.
 */
public class MTimerTask {
    public static void main(String[] args){
        Timer timer=new Timer();
        timer.schedule(new MTask("task 1"),1000,2000);
        timer.schedule(new MTask("task 2"),0,1000);
    }
}
class MTask extends TimerTask{
    String ss;
    @Override
    public void run() {
        System.out.println(ss);
    }

    public MTask(String ss) {
        this.ss = ss;
    }
    public String getSs(){
        return ss;
    }
}
