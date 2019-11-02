import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;


public class Game extends JPanel{
	
	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 800;
	//Hur många rutor som ska finnas på bredden och höjden
	private static final int TILE_AMOUNT_W = 25;
	private static final int TILE_AMOUNT_H = 25;


    private int x = 0;
	private int y = 0;
	private Player player;
	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private Map gameMap;
	//Storleken på en ruta
	private int tileWidth;
	private int tileHeight;

	public Game(){
		super();

		//Räknar ut hur stor en ruta måste vara för att få plats med 25 på planen
		this.tileWidth = GAME_WIDTH / TILE_AMOUNT_W;
		this.tileHeight = GAME_HEIGHT / TILE_AMOUNT_H;
		this.gameMap = new Map(TILE_AMOUNT_W, TILE_AMOUNT_H, this.tileWidth, this.tileHeight);
		gameMap.loadMap("Maps/map1.txt");

		this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
		this.player = new Player(this);

		KeyBoardListener key = new KeyBoardListener();
		this.addKeyListener(key);

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

	private void playerAction(char action){
		int pX = player.getX();
		int pY = player.getY();
		int pSpeed = player.getSpeed();


		if(action == 'a' && collide(pX - pSpeed, pY) == false){
			player.moveLeft();
			repaint();
		}
		if(action == 'd' && collide(pX + pSpeed, pY) == false){
			player.moveRight();
			repaint();
		}
		if(action == 'w' && collide(pX, pY - pSpeed) == false){
			player.moveUp();
			repaint();
		}
		if(action == 's' && collide(pX, pY - pSpeed) == false){
			player.moveDown();
			repaint();
		}
		else if(action == ' '){
			Bomb b = new Bomb(player.getX(), player.getY());
			this.bombs.add(b);
			System.out.println("DROP BOMB");
		}
	}

	//Tittar om x,y kolliderar med något i mappen
	private boolean collide(int x, int y){
		int tileW = x / this.tileWidth;
		int tileH = y / this.tileHeight;
		if(gameMap.getTile(tileW,tileH) != null || x > GAME_WIDTH-this.tileWidth || x < 0 || y > GAME_HEIGHT-this.tileHeight || y < 0){
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
				this.explosions.add(temp.explode());
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
		while(true){
			bombAction();
			removeExplosions();
			repaint();
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

	private class KeyBoardListener extends KeyAdapter{

		public KeyBoardListener(){}
		
		@Override
		public void keyPressed(KeyEvent e){
			char action = e.getKeyChar();
			playerAction(action);
		}
		
	}
}