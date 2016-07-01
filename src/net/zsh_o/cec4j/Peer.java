package net.zsh_o.cec4j;

import net.zsh_o.cec4j.advertisement.GroupAdvertisement;
import net.zsh_o.cec4j.server.AdvServer;
import net.zsh_o.cec4j.server.PipeServer;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class Peer {
    static final String DEFAULTGROUPUUID="56b8a1f1-7529-48db-81fe-1535741bba9c";
    static final String DEFAULTGROUPANME="DefaultGroup";
    private int serverPort;
    private InetAddress multicastAddress;
    private int multicastPort;
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private HashMap<String,Group> groupList;
    private HashMap<RemotePeer,Pipe> sendPipeList;
    private HashMap<RemotePeer,Pipe> recivePipeList;

    public RemotePeer myself;

    public Heartbeat heartbeat;
    public Station station;
    public AdvServer advServer;
    public PipeServer pipeServer;

    public Peer(int serverPort, InetAddress multicastAddress, int multicastPort) throws IOException {
        this.serverPort = serverPort;
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        init();
    }
    private void init() throws IOException {
        System.out.println("初始化对等体");
        groupList=new HashMap<>();
        sendPipeList=new HashMap<>();
        recivePipeList=new HashMap<>();
        serverSocket=new ServerSocket(serverPort);
        multicastSocket=new MulticastSocket(multicastPort);
        multicastSocket.joinGroup(multicastAddress);


        advServer=new AdvServer(this);
        pipeServer=new PipeServer(this);
        heartbeat=new Heartbeat(3000,this);//四秒触发一次心跳;
        station=new Station(this);

        myself=new RemotePeer(InetAddress.getLocalHost(),serverPort,3);

        try {
            initDefaultGroup();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private void initDefaultGroup() throws DocumentException {
        System.out.println("初始化组群");
        Group defaultGroup=new Group(UUID.fromString(DEFAULTGROUPUUID),DEFAULTGROUPANME);
        defaultGroup.setJoined(true);
        groupList.put(defaultGroup.getUuid().toString(),defaultGroup);
        station.insert(new GroupAdvertisement(this,defaultGroup,4000));
        defaultGroup.insertPeer(myself);
    }
    public void createGroup(String name) throws DocumentException {
        System.out.println("创建组群"+name);
        Group group=new Group(UUID.randomUUID(),name);
        group.setJoined(true);
        groupList.put(group.toString(),group);
        station.insert(new GroupAdvertisement(this,group,4000));
        group.insertPeer(myself);
    }
    public void insertGroup(String name) throws DocumentException {
        Group group=new Group(UUID.randomUUID(),name);
        groupList.put(group.toString(),group);
        station.insert(new GroupAdvertisement(this,group,4000));
    }

    public void joinGroup(Group group){
        System.out.println("加入组群"+group.toString());
        group.setJoined(true);
        try {
            station.insert(new GroupAdvertisement(this,group,4000));
            group.insertPeer(myself);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void leaveGroup(Group group){
        System.out.println("离开组群"+group.toString());
        group.setJoined(false);
        station.delete(group.getUuid().toString());
        group.deletePeer(myself);
    }

    public HashMap<RemotePeer, Pipe> getRecivePipeList() {
        return recivePipeList;
    }

    public RemotePeer getRemotePeer(String peer){
        return groupList.get(DEFAULTGROUPUUID).findPeer(peer);
    }

    public int getServerPort() {
        return serverPort;
    }

    public InetAddress getMulticastAddress() {
        return multicastAddress;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public MulticastSocket getMulticastSocket() {
        return multicastSocket;
    }

    public HashMap<String, Group> getGroupList() {
        return groupList;
    }

    public void sendGroupMessage(Group group,Message message){
        String ss=message.getDocument().asXML();
        try {
            multicastSocket.send(new DatagramPacket(ss.getBytes(),ss.getBytes().length,multicastAddress,multicastPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<RemotePeer, Pipe> getSendPipeList() {
        return sendPipeList;
    }

    public Group getDefaultGroup(){
        return groupList.get(DEFAULTGROUPUUID);
    }
}
