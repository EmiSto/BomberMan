package TestServerClient;

import java.net.*;
import java.io.*;

public class BombServer {
    private int portNumber = 4446;
    private int serverPort = 4445;

    public void listenSocketTCP() {

        try {
            ServerSocket server = new ServerSocket(this.portNumber);

            int nrClients = 2;
            Thread []threads = new Thread[nrClients];
            for(int i = 0; i < nrClients; i++){
                Socket client = server.accept();
                System.out.println("Connection!");
                threads[i] = new Thread(new ClientHandleTCP(client));
            }
            for(Thread t : threads){
                t.start();
            }
            server.close();
 
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void listenSocketUDP(){
        try {
            DatagramSocket server = new DatagramSocket(this.serverPort);

            InetAddress group = InetAddress.getByName("228.5.6.7");
            byte[] recv = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(recv, recv.length);
            DatagramPacket sendPacket;
            
            byte[] message = new byte[1024];
            while (true) {
                
                server.receive(receivePacket);

                String s = new String(receivePacket.getData(), 0, receivePacket.getLength());
                message = s.getBytes();
                //String s = "nu kommer ett meddelande fron servern";
                //message = s.getBytes();
                sendPacket = new DatagramPacket(message, message.length, group, this.portNumber);
                server.send(sendPacket);
                System.out.println("Paket skickat");
                try{
                Thread.sleep(1000);
                }catch(InterruptedException ie){
                    System.out.println(ie);
                }
            }
            //server.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        BombServer bs = new BombServer();
        //bs.listenSocketTCP();
        bs.listenSocketUDP();
    }

    class ClientHandleTCP extends Thread{
        private Socket client;

        public ClientHandleTCP(Socket c){
            this.client = c;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println("Nu skickar jag ett meddelande till mister client");
                String fromClient;
                while ((fromClient = in.readLine()) != null) {
                    System.out.println("From client: " + fromClient);
                }

                while (!fromClient.equals("close")) {
                    fromClient = in.readLine();
                    System.out.println("From client: " + fromClient);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    class ClientHandleUDP extends Thread{

    }
}