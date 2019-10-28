import java.awt.Dimension;
import java.awt.Graphics2D;

public class Player{

    //Positionen
    private int x;
    private int y;
    //Storleken
    private int width = 20;
    private int height = 20;

    private int speed = 10;
    private Game game;

    public Player(Game g){
        this.game = g;
        Dimension d = g.getPreferredSize();
        this.x = (int)d.getWidth() / 2;
        this.y = (int)d.getHeight() / 2;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void paint(Graphics2D g){
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    public void moveRight(){
        this.x += this.speed;
    }
    public void moveLeft(){
        this.x -= this.speed;
    }
    public void moveUp(){
        this.y -= this.speed;
    }
    public void moveDown(){
        this.y += this.speed;
    }
}