package pruebas;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class DatagramSocketClient {
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;
    Scanner sc = new Scanner(System.in);
    boolean victoria = false;
    int intentos;

    public void init(String host, int port) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
        intentos=0;
    }

    public void runClient() throws IOException{
        byte [] receivedData = new byte[1024];
        byte [] sendingData = ByteBuffer.allocate(4).putInt(0).array();

        //a l'inici
        sendingData = getFirstRequest();
        //el servidor atén el port indefinidament
        do{
            DatagramPacket packet = new DatagramPacket(sendingData,
                    sendingData.length,
                    serverIP,
                    serverPort);
            //enviament de la resposta
            socket.send(packet);

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);
            //espera de les dades
            socket.receive(packet);
            //processament de les dades rebudes i obtenció de la resposta
            System.out.println(serverResponse(packet.getData()));
            if (!victoria) sendingData = getDataToRequest(packet.getData(), packet.getLength());
        }while(mustContinue(victoria));
    }

    private byte[] getDataToRequest(byte[] data, int length) {
        //procés diferent per cada aplicació
        System.out.print("Introduce Un numero: ");
        int n = sc.nextInt();
        return ByteBuffer.allocate(4).putInt(n).array();
    }

    private byte[] getFirstRequest() {
        //procés diferent per cada aplicació
        System.out.print("Introduce un numero: ");
        int n = sc.nextInt();
        return ByteBuffer.allocate(4).putInt(n).array();
    }
    private String serverResponse(byte[] data){
        int n = ByteBuffer.wrap(data).getInt();
        intentos++;
        if(n==0){victoria=true;return "Correcte";}
        else if(n==1) return "Més petit";
        else return "Més gran";
    }

    private boolean mustContinue(boolean victoria) {
        if (victoria){
            System.out.println("Has hecho "+ intentos+" intentos");
            return false;
        }else return true;
    }

    public static void main(String[] args) {
        DatagramSocketClient client = new DatagramSocketClient();
        try {
            client.init("localhost",5555);
            client.runClient();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
