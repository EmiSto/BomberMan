package TestServerClient;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
    private int portNumber = 4446;
    private int serverPort = 4445;
    private String groupIP = "228.5.6.7";
    private String serverIP = "192.168.1.106";

    public void connectTCP() {

        try {
            Socket client = new Socket("192.168.56.1", this.portNumber);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String fromServer = in.readLine();
            System.out.println("From servern: " + fromServer);
            /*while((fromServer = in.readLine()) != null){
                System.out.println("From servern: " + fromServer);
            }*/
            out.println("Vad vill du??");

            Scanner sc = new Scanner(System.in);
            String response  = "";
            while(!response.equals("close")){
                response = sc.nextLine();
                out.println(response);
                
            }
            client.close();
            sc.close();
            
            
            

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void connectUDP(String name){
        try{
            MulticastSocket client = new MulticastSocket(this.portNumber);
            InetAddress group = InetAddress.getByName(this.groupIP);

            client.joinGroup(group);
            Thread listener = new Thread(new ListenThread(client));
            listener.start();
            byte[] out = new byte[1024];
            DatagramPacket sendPacket;

            Scanner sc = new Scanner(System.in);
            String response  = "";
            while(true){
            response = name + ": " + sc.nextLine();
            out = response.getBytes();
            sendPacket = new DatagramPacket(
                out, out.length, InetAddress.getByName(this.serverIP), this.serverPort);
            client.send(sendPacket);

            
            }
            //sc.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        Client bc = new Client();
        //bc.connectTCP();
        bc.connectUDP(args[0]);
    }
}

class ListenThread extends Thread{
    private MulticastSocket client;
    byte[] in = new byte[1024];
    private DatagramPacket receivePacket =  new DatagramPacket(in, in.length);
    public ListenThread(MulticastSocket c){
        this.client = c;
    }
    public void run() {
       
        while (true) {
            try{
            client.receive(receivePacket);
            String s = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(s);
            }catch(IOException e){
                System.out.println("Exiting now...");
            }
        }
    }
}