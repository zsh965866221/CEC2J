package learn.io;

import java.util.*;

/**
 * Created by zsh96 on 2016/4/6.
 */
public class MTimerTask {
    public static void main(String[] args){
        Timer timer=new Timer();
        timer.schedule(new MTask("task 1"),1000,2000);
        timer.schedule(new MTask("task 2"),0,1000);
        UUID uuid= UUID.fromString("56b8a1f1-7529-48db-81fe-1535741bba9c");
        UUID uuid1=UUID.fromString("56b8a1f1-7529-48db-81fe-1535741bba9c");
        int a=2;
        boolean k=uuid.equals(uuid1);
        a++;
        HashSet<String> hs=new HashSet<>();
        TreeSet<String> ts=new TreeSet<>();
        HashMap<String,String> hm=new HashMap<>();
        Iterator iter=hm.entrySet().iterator();//HashMap的遍历
        while(iter.hasNext()){
            Map.Entry entry=(Map.Entry)iter.next();
            Object key=entry.getKey();
            Object value=entry.getValue();
        }
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
