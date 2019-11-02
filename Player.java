import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

import javax.swing.ImageIcon;

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

    //Getters
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public int getSpeed(){
        return this.speed;
    }

    //Setters
    public void setSpeed(int s){
        this.speed = s;
    }

    public void paint(Graphics2D g){
        ImageIcon ii = new ImageIcon("Icons/Player/p1.png");
        Image playerImage = ii.getImage();
        //g.drawImage(playerImage, this.x, this.y, 40, 40, 0, 0, 20, 20,null);
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