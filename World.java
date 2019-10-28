import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class World extends JFrame{
	
	Game game;
   	public World(){
		initWorld();
   	}

   	private void initWorld(){
	   	this.game = new Game();
		this.add(this.game);
		this.pack();
	   	this.setResizable(false);
	   	this.setTitle("Bomberman");
	   	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	public void run(){
		this.game.run();
	}

	public static void main(String[] args) throws InterruptedException {
		World frame = new World();
		frame.setVisible(true);	
		frame.run();
	}
}