import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class World extends JFrame{
	
	private Game game;
	private BombClient bc;
	private BombServer bs;
   	public World(String type){
		initWorld(type);
   	}

  	private void initWorld(String type) {
		if (type.equals("Client")) {
			System.out.println("Skapar en Client");
			this.bc = new BombClient();
			this.add(this.bc);
		} else if (type.equals("Server")) {
			System.out.println("Skapar en Server");
			this.bs = new BombServer();
			this.add(this.bs);
		} else {
			this.game = new Game();
			this.add(this.game);
		}
		this.pack();
		this.setResizable(false);
		this.setTitle("Bomberman");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void run(String type) {
		if(type.equals("Client")){
			this.bc.run();
		}
		else if(type.equals("Server")){
			this.bs.run();
		}
		else{
			this.game.run();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Dettta är args[0]" + args[0]);
		World frame = new World(args[0]);
		frame.setVisible(true);	
		frame.run(args[0]);
	}
}