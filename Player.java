import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Player{

    //Positionen
    private int x;
    private int y;
    //Storleken
    private int width;
    private int height;

    private int speed = 10;
    private Game game;

    //Statusar 
    private int power = 0;

    //Gjorde denna konstruktor för att klienten inte skulle klaga
    public Player(Dimension size){
        this.x = 32;
        this.y = 32;
        this.width = (int)size.getWidth();
        this.height = (int)size.getHeight();
    }
    public Player(Game g, Dimension size){
        this(size);
        Dimension d = g.getPreferredSize();
        this.x = (int)d.getWidth() / 2;
        this.y = (int)d.getHeight() / 2;
        this.game = g;
    }
    public Player(int startX, int startY, Dimension size){
        this(size);
        this.x = startX;
        this.y = startY;
    }


    //Getters
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getXCenter(){
        return (this.width + this.x*2) / 2; 
    }
    public int getYCenter(){
        return (this.height + this.y*2) / 2;
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
    public Rectangle getBounds(){
        Rectangle r = new Rectangle(this.x, this.y, this.width, this.height);
        return r;
    }

    //Setters
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setSpeed(int s){
        this.speed = s;
    }
    
    //dir [0] är 1, 0 eller -1 för hur player rör sig i x-led
    //returnerar en rectangle där player hamnar om den rör sig nu
    public Rectangle willMove(int[] dir){
        
            Rectangle r = new Rectangle(this.x + (dir[0] * this.speed), this.y + (dir[1] * this.speed), this.width, this.height);
            return r;    
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
    public void move(int[] dir){
        this.x += dir[0] * this.speed;
        this.y += dir[1] * this.speed;
    }

    public Bomb plantBomb(){
        //Checka status etc
        Bomb b = new Bomb(new Dimension(this.width, this.height), this.power);
        return b;
    }

    public void receiveStatus(Status s){
        if(s.getName() == "Power"){
            this.power++;
        }
    }

    public void paint(Graphics2D g){
        ImageIcon ii = new ImageIcon("Icons/Player/p1.png");
        Image playerImage = ii.getImage();
        //g.drawImage(playerImage, this.x, this.y, 40, 40, 0, 0, 20, 20,null);
        g.fillRect(this.x, this.y, this.width, this.height);
    }


}