import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

public class Explosion{

    private Rectangle[] subExplosions = new Rectangle[4];

    private int width;
    private int height;
    private int expSize;
    private long lifeTime = 1000;
    private long startTime;
    private int originX;
    private int originY;

    public Explosion(int x, int y, int bombWidth, int bombHeight, int expSize){

        this.expSize = expSize;
        this.width = bombWidth;
        this.height = bombHeight;
        //Mitten av exposionen
        this.originX = (x + x + bombWidth) / 2;
        this.originY = (y + y + bombHeight) / 2;
        

        this.subExplosions[0] = new Rectangle(x, y - expSize, this.width, expSize);
        this.subExplosions[1] = new Rectangle(x + bombWidth, y, expSize, this.height);
        this.subExplosions[2] = new Rectangle(x, y + bombHeight, this.width, expSize);
        this.subExplosions[3] = new Rectangle(x - expSize, y, expSize, this.height);
        
        this.startTime = System.currentTimeMillis();
    }

    public int getOriginX(){
        return this.originX;
    }
    public int getOriginY(){
        return this.originY;
    }
    public int getExpSize(){
        return expSize;
    }

    public void paint(Graphics2D g){

        g.setColor(Color.red);
        for(int i = 0; i < this.subExplosions.length; i++){
            Rectangle tmp = this.subExplosions[i];
            g.fillRect((int)tmp.getX(), (int)tmp.getY(), (int)tmp.getWidth(), (int)tmp.getHeight());
        }
    }

    //Target x and y position with target width and height
    public boolean collide(int x, int y, int w, int h){

        Rectangle target = new Rectangle(x,y,w,h);
        for(int i = 0; i < this.subExplosions.length; i++){
            if(this.subExplosions[i].intersects(target)){
                return true;
            }
        }
        return false;
    }

    //Checkar om explosionen är klar att tas bort
    public boolean isDone(long currTime){
        if(currTime - this.startTime >= this.lifeTime){
            return true;
        }
        return false;
    }
}