import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.net.*;
import java.io.*;

public class BombServer extends JPanel{
    
    private int portNumber = 4446;
    private int serverPort = 4445;
    private String groupIP = "228.5.6.7";
	private String serverIP = "192.168.1.106";
	private DatagramSocket server;

    private static final int GAME_WIDTH = 720;
	private static final int GAME_HEIGHT = 720;
	//Hur många rutor som ska finnas på bredden och höjden
	//Detta ger rutor som är 32*32
	private static final int TILE_AMOUNT_W = 12;
	private static final int TILE_AMOUNT_H = 12;
	//Directions
	private static final int[] MOVE_UP = new int[]{0,-1};
	private static final int[] MOVE_RIGHT = new int[]{1,0};
	private static final int[] MOVE_DOWN = new int[]{0,1};
	private static final int[] MOVE_LEFT = new int[]{-1,0};




    private int x = 0;
	private int y = 0;
	private Player player;
	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private Map gameMap;
	//Storleken på en ruta
	private int tileWidth;
	private int tileHeight;

    public BombServer(){
		super();

		//Räknar ut hur stor en ruta måste vara för att få plats med 25 på planen
		this.tileWidth = GAME_WIDTH / TILE_AMOUNT_W;
		this.tileHeight = GAME_HEIGHT / TILE_AMOUNT_H;
		this.gameMap = new Map(TILE_AMOUNT_W, TILE_AMOUNT_H, this.tileWidth, this.tileHeight);
		gameMap.loadMap("Maps/map2.txt");
		gameMap.spawnStatus();

		this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
		this.player = new Player(GAME_WIDTH/2, GAME_HEIGHT/2, new Dimension(this.tileWidth - 4, this.tileHeight - 4));

		this.setFocusable(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		//För att få smooth saker
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gameMap.paint(g2d);
		player.paint(g2d);

		//Paint Bombs
		for (Bomb b : this.bombs){
			b.paint(g2d);
		}

		//Paint Explosions
		for(Explosion e : this.explosions){
			e.paint(g2d);
		}
		
	}
	protected void packetHandler(String packet){
		Scanner scan = new Scanner(packet);
		//Checka OP code 
		//om det är move
		playerAction((char)packet.charAt(0));
		//om det är bomb
	}

	//SEND FUNCTIONS
	//Skickar msg
	private void sendMessage(String msg){
		byte[] out = msg.getBytes();
		try {
			DatagramPacket response = new DatagramPacket(out, out.length, InetAddress.getByName(groupIP),
					this.portNumber);
			server.send(response);
			System.out.println("Response sent");
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	// TODO: ta emot ID som argument
	private void sendPlayerPosition(){
		String x = Integer.toString(this.player.getX());
		String y = Integer.toString(this.player.getY());

		String msg = "move" + "," + x + "," + y;
		sendMessage(msg);
	}
	private void sendBomb(Bomb b){
		String x = Integer.toString(b.getX());
		String y = Integer.toString(b.getY());
		String width = Integer.toString(b.getWidth());
		String height = Integer.toString(b.getHeight());
		String explosionAmp = Integer.toString(b.getExplosionAmp());

		String msg = 
		"bomb" + "," + x + "," + y + "," + width + "," + height + "," + explosionAmp;
		sendMessage(msg);
	}
	private void playerAction(char action){
		if(action == 'a'){
			playerMove(MOVE_LEFT);
		}
		if(action == 'd'){
			playerMove(MOVE_RIGHT);
		}
		if(action == 'w'){
			playerMove(MOVE_UP);
		}
		if(action == 's'){
			playerMove(MOVE_DOWN);
		}
		else if(action == ' '){
			Bomb b = this.player.plantBomb();
			this.gameMap.insertBomb(b, this.player.getXCenter(), this.player.getYCenter());
			this.bombs.add(b);
			System.out.println("DROP BOMB");
			sendBomb(b);
		}
	}
	private void playerMove(int[] dir){
		if(playerCollide(dir) == false){
			this.gameMap.removePlayer(this.player.getBounds());
			player.move(dir);
			Status s = this.gameMap.addPlayer(this.player.getBounds());
			if (s != null){ 
				this.player.receiveStatus(s);
			}

			//Skicka spelarens nya position
			sendPlayerPosition();
		}
	}

	//Tittar om x,y kolliderar med något i mappen
	private boolean playerCollide(int[] dir){

		Rectangle playerRect = this.player.willMove(dir);
		int x = (int)playerRect.getX();
		int y = (int)playerRect.getY();

		// Utanför planen
		if (x > GAME_WIDTH - this.tileWidth || x < 0 || y > GAME_HEIGHT - this.tileHeight || y < 0) {
			return true;
		}

		//Övre högra 
		if (this.gameMap.getTile(x + this.player.getWidth(), y).obstructed()) {
			return true;
		}
		//Undre högra
		if (this.gameMap.getTile(x + this.player.getWidth(), y + this.player.getHeight()).obstructed()) {
			return true;
		}
		//Undre vänstra
		if (this.gameMap.getTile(x, y + this.player.getHeight()).obstructed()) {
			return true;
		}
		//Övre vänstra
		if(this.gameMap.getTile(x, y).obstructed()){
			return true;
		}
		return false;
	}
	//Tittar vilka bomber som ska sprängas
	private void bombAction(){
	
		long currTime = System.currentTimeMillis();
		for(int i = 0; i < this.bombs.size(); i++){
			Bomb temp = this.bombs.get(i);
			if(temp.willExplode(currTime)){
				Explosion e = temp.explode();
				this.explosions.add(e);
				this.gameMap.bombExplode(e);
				System.out.println("BOOOM");
				this.bombs.remove(i);
			}
			currTime = System.currentTimeMillis();
		}
	}

	private void removeExplosions(){

		long currTime = System.currentTimeMillis();
		for(int i = 0; i < this.explosions.size(); i++){
			Explosion temp = this.explosions.get(i);
			if(temp.isDone(currTime)){
				this.explosions.remove(i);
			}
			currTime = System.currentTimeMillis();
		}
	}

	private void checkCollision(){
		for(Explosion e : this.explosions){
			if(e.collide(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				System.out.println("OUCH! Player hit!");
			}
		}
	}

	//Game loop
	public void run(){

        try{
            server = new DatagramSocket(this.serverPort);
            InetAddress group = InetAddress.getByName(this.groupIP);
            Thread listener = new Thread(new ListenThreadServer(this,server));
            listener.start();
        }catch(IOException e){
            System.out.println("Något gick fel i att joina multicasten" + e);
        }

		while(true){
			bombAction();
			removeExplosions();
			//repaint();
			checkCollision();
			

			try{
				Thread.sleep(10);		
			}catch(InterruptedException e){
				String msg = String.format("Thread interrupted: %s", e.getMessage());

				JOptionPane.showMessageDialog(this, msg, "Error", 
					JOptionPane.ERROR_MESSAGE);
					
			}
			
		}
	}

}
class ListenThreadServer extends Thread{
    private DatagramSocket listenSocket;
    byte[] in = new byte[1024];
    private DatagramPacket receivePacket =  new DatagramPacket(in, in.length);
    private BombServer bombServer;
    public ListenThreadServer(BombServer bs, DatagramSocket ds){
		this.listenSocket = ds;
		this.bombServer = bs;
    }
    public void run() {
       
        while (true) {
            try{
			listenSocket.receive(receivePacket);
			System.out.println("Action received");
            String s = new String(receivePacket.getData(), 0, receivePacket.getLength());
			System.out.println(s);
			this.bombServer.packetHandler(s);
            }catch(IOException e){
                System.out.println("Exiting now...");
            }
        }
    }
}