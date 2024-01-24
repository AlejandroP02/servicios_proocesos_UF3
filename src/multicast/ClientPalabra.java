package multicast;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ClientPalabra {
    /* Client afegit al grup multicast SrvVelocitats.java que representa un velocímetre */

    private boolean continueRunning = true;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    NetworkInterface netIf;
    InetSocketAddress group;
    Map<String, Integer> map = new HashMap<>();


    public ClientPalabra(int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        //netIf = NetworkInterface.getByName("enp1s0");
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp,portValue);
    }

    public void runClient() throws IOException{
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];

        socket.joinGroup(group,netIf);
        System.out.printf("Connectat a %s:%d%n",group.getAddress(),group.getPort());

        while(continueRunning){
            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);
            try{
                socket.receive(packet);
                continueRunning = getData(packet.getData(), packet.getLength());
            }catch(SocketTimeoutException e){
                System.out.println("S'ha perdut la connexió amb el servidor.");
                continueRunning = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.leaveGroup(group,netIf);
        socket.close();
    }

    protected boolean getData(byte[] data, int lenght) {
        boolean ret=true;

        String msg = new String(data,0,lenght);

        map.putIfAbsent(msg, 0);

        map.replace(msg, map.get(msg)+1);
        System.out.println(msg+": "+ map.get(msg));


        //if (v==1) ret=false;

        return ret;
    }

    public static void main(String[] args) throws IOException {
        ClientPalabra cvel = new ClientPalabra(5557, "224.0.11.119");
        cvel.runClient();
        System.out.println("Parat!");

    }

}
