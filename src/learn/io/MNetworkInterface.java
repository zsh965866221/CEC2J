package learn.io;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zsh96 on 2016/4/13.
 */
public class MNetworkInterface {
    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> nis =NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()){
            System.out.println(nis.nextElement());
        }
    }
}
