package net.zsh_o.cec4j;

import net.zsh_o.cec4j.advertisement.PipeAdvertisement;
import org.dom4j.DocumentException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by zsh96 on 2016/4/11.
 */
public class Pipe {
    private Peer mpeer;

    private RemotePeer peer;
    Socket socket;

    int life;
    int maxlife;

    DataOutputStream output;
    DataInputStream input;
    public Pipe(Peer mpeer,RemotePeer peer, int maxlife) throws IOException, DocumentException {//以peer初始化socket,用于申请连接
        this.mpeer=mpeer;
        this.peer = peer;
        this.life=this.maxlife = maxlife;
        socket=new Socket(peer.getAddress(),peer.getPort());
        DataOutputStream outputStream=new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF(String.valueOf(mpeer.getServerPort()));

        mpeer.getSendPipeList().put(peer,this);

        initStream();
    }

    public Pipe(Peer mpeer,Socket socket, int maxlife,int port) throws IOException, DocumentException {//用socket寻找peer,用于处理连接请求
        this.mpeer=mpeer;
        this.socket = socket;
        this.life=this.maxlife = maxlife;
        String speer=socket.getInetAddress().getHostAddress()+":"+port;

        RemotePeer peer =mpeer.getDefaultGroup().getPeerList().get(speer);
        this.peer=peer;

        mpeer.getRecivePipeList().put(peer,this);


        initStream();
    }

    private void initStream() throws IOException, DocumentException {
        output=new DataOutputStream(socket.getOutputStream());
        input=new DataInputStream(socket.getInputStream());

        //建立管道开始发送管道通告
        mpeer.station.insert(new PipeAdvertisement(mpeer,3000,this));
    }

    public void close() throws IOException {
        output.close();
        input.close();
        socket.close();
        mpeer.station.delete(peer.toString());

    }

    public void sendMessage(Message message) throws IOException {
//        if(socket.isOutputShutdown()){
            output.writeUTF(message.getDocument().asXML());
//        }
    }

    public RemotePeer getPeer() {
        return peer;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getLife() {
        return life;
    }
    public int redLife(){
        return --life;
    }
    public void recoveryLife(){
        life=maxlife;
    }

    @Override
    public String toString() {
        return peer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(((Pipe)o).peer.equals(peer))
            return true;
        else    return false;
    }
}
