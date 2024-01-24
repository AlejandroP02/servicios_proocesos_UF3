package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SrvPalabras {
    /* Servidor Multicast que ens comunica la velocitat que porta d'un objecte */

    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    boolean continueRunning = true;
    Palabra simulator;

    public SrvPalabras(int portValue, String strIp) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        simulator = new Palabra();
    }

    public void runServer() throws IOException{
        DatagramPacket packet;
        byte [] sendingData;

        while(continueRunning){
            sendingData = simulator.getPalabra().getBytes();
            packet = new DatagramPacket(sendingData, sendingData.length,multicastIP, port);
            socket.send(packet);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }


        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        //Canvieu la X.X per un número per formar un IP.
        //Que no sigui la mateixa que la d'un altre company
        SrvPalabras srvVel = new SrvPalabras(5557, "224.0.11.119");
        srvVel.runServer();
        System.out.println("Parat!");

    }

}