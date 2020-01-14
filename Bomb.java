import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

public class Bomb{


    private static final int STANDARD_EXPLOSION_SIZE = 64;

    private int x;
    private int y;
    private int width;
    private int height;
    

    private long startTime;
    private long explodeTime = 3000;

    private int explosionAmp;
    private boolean explode = false;

    public Bomb(Dimension size){
        this.width = (int)size.getWidth();
        this.height = (int)size.getHeight();
        this.explosionAmp = 1;
        this.startTime = System.currentTimeMillis();
    }
    public Bomb(Dimension size, int power){
        this(size);
        this.explosionAmp = power + 1;
    }
    
    /*//Om bombens sprängning ändrats
    public Bomb(int x, int y, int expAmp){
        this(x,y, tmp);
        this.explosionAmp = expAmp;
    }*/

    //Getters
    public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public int getExplosionAmp() {
		return this.explosionAmp;
    }
    
    //Setters
    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean willExplode(long currTime){
        if((currTime - this.startTime) >= this.explodeTime){
            return true;
        }
        return false;
    }

    public Explosion explode(){
        this.explode = true;
        return new Explosion(this.x, this.y, this.width, this.height, STANDARD_EXPLOSION_SIZE * this.explosionAmp);

    }

    public void paint(Graphics2D g){
        g.setColor(Color.green);
        g.fillOval(this.x, this.y, this.width, this.height);
        /*if(this.explode){
            int explotionSize = STANDARD_EXPLOSION_SIZE * this.explosionAmp;

            g.setColor(Color.red);
            g.fillRect(this.x, this.y, this.width, explotionSize);
            g.fillRect(this.x, this.y - explotionSize, this.width, explotionSize);
            g.fillRect(this.x, this.y, explotionSize, this.height);
            g.fillRect(this.x - explotionSize, this.y, explotionSize, this.height);
        }*/
    }
	
}