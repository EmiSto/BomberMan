import java.awt.Graphics2D;
import java.awt.Color;

public class Bomb{


    //--------------------------


    //KANSKE KAN GÖRA OM ALLT I EXPLOSIONS TILL REKTANGLAR MED AWT RECTANGLE


    //------------------------------


    private static final int STANDARD_EXPLOSION_SIZE = 60;

    private int x;
    private int y;
    private int width = 20;
    private int height = 20;
    

    private long startTime;
    private long explodeTime = 3000;

    private int explosionAmp;
    private boolean explode = false;

    public Bomb(int x, int y){
        this.x = x;
        this.y = y;
        this.explosionAmp = 1;
        this.startTime = System.currentTimeMillis();
    }
    
    //Om bombens sprängning ändrats
    public Bomb(int x, int y, int expAmp){
        this(x,y);
        this.explosionAmp = expAmp;
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