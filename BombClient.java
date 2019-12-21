import java.net.*;
import java.io.*;
import java.util.Scanner;

public class BombClient{
    private int portNumber = 4446;
    private int serverPort = 4445;
    private String serverIP = "228.5.6.7";

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

            InetAddress group = InetAddress.getByName(this.serverIP);
            client.joinGroup(group);
            byte[] out = new byte[1024];
            byte[] in = new byte[1024];
            DatagramPacket recievePacket =  new DatagramPacket(in, in.length);
            DatagramPacket sendPacket;

            Scanner sc = new Scanner(System.in);
            String response  = "";
            while(true){
            response = name + ": " + sc.nextLine();
            out = response.getBytes();
            sendPacket = new DatagramPacket(out, out.length, group, this.portNumber);
            client.send(sendPacket);

            client.receive(recievePacket);
            String s = new String(recievePacket.getData(), 0, recievePacket.getLength());
            System.out.println("From server: " + s);
            }
            //sc.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        BombClient bc = new BombClient();
        //bc.connectTCP();
        bc.connectUDP(args[0]);
    }
}