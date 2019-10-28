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


	
    //--------------------------


    //KANSKE KAN GÖRA OM ALLT I EXPLOSIONS TILL REKTANGLAR MED AWT RECTANGLE


    //------------------------------

    int x = 0;
	int y = 0;
	Player player;
	ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	public Game(){
		super();
		this.setPreferredSize(new Dimension(500,500));
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
		player.paint(g2d);

		//Paint Bombs
		for (int i = 0; i < this.bombs.size(); i++){
			this.bombs.get(i).paint(g2d);
		}

		//Paint Explosions
		for(Explosion e : this.explosions){
			e.paint(g2d);
		}
		
	}

	public void playerAction(char action){
		if(action == 'a'){
			player.moveLeft();
			repaint();
		}
		if(action == 'd'){
			player.moveRight();
			repaint();
		}
		if(action == 'w'){
			player.moveUp();
			repaint();
		}
		if(action == 's'){
			player.moveDown();
			repaint();
		}
		else if(action == ' '){
			Bomb b = new Bomb(player.getX(), player.getY());
			this.bombs.add(b);
			System.out.println("DROP BOMB");
		}
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