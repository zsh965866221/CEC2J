package net.zsh_o.cec4j;

import net.zsh_o.cec4j.advertisement.Advertisement;

import java.util.HashMap;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class Station {
    private Peer peer;
    private HashMap<String,Advertisement> advertisementHashMap;

    public Station(Peer peer) {
        this.peer = peer;
        advertisementHashMap=new HashMap<>();
    }
    public void insert(Advertisement adv){
        adv.start();
        advertisementHashMap.put(adv.toString(),adv);
    }
    public void delete(String name){
        Advertisement adv=advertisementHashMap.get(name);
        adv.cancel();
        advertisementHashMap.remove(name);
    }
}
