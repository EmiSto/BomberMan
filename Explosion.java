import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

public class Explosion{

    //--------------------------


    //KANSKE KAN GÖRA OM ALLT I EXPLOSIONS TILL REKTANGLAR MED AWT RECTANGLE


    //------------------------------

    private Dimension[] coords = new Dimension[4];
    private int width = 20;
    private int height = 20;
    private int expSize;
    private long lifeTime = 1000;
    private long startTime;

    public Explosion(int x, int y, int bombWidth, int bombHeight, int expSize){

        this.expSize = expSize;
        //Första explosionen (upp)
        this.coords[0] = new Dimension(x , y - expSize);
        //Andra explosionen (till höger)
        this.coords[1] = new Dimension(x + bombWidth, y);
        //Tredje (ner)
        this.coords[2] = new Dimension(x , y + bombHeight);
        //Fjärde (vänster)
        this.coords[3] = new Dimension(x - expSize, y);

        
        this.startTime = System.currentTimeMillis();
    }

    public void paint(Graphics2D g){
        int x,y;

        g.setColor(Color.red);
        for(int i = 0; i < this.coords.length; i++){
            
            x = (int)this.coords[i].getWidth();
            y = (int)this.coords[i].getHeight();
            if(i%2 == 0){
                g.fillRect(x, y, this.width, this.expSize);
            }
            else{
                g.fillRect(x, y, this.expSize, this.height);
            }
        }
    }

    //Target x and y position with target width and height
    public boolean collide(int x, int y, int w, int h){
        int xExpl, yExpl;
        for(int i = 0; i<coords.length; i++){
            xExpl = (int)coords[i].getWidth();
            yExpl = (int)coords[i].getHeight();
            if(i%2 == 0){
                if(xExpl >= (x + w) && (xExpl + this.width) >= x){
                    return true;
                }
                if(yExpl >= (y + h) && (yExpl + this.expSize) >= y){
                    return true;
                }
            }
            else{
                if(xExpl >= (x + w) && (xExpl + this.expSize) >= x){
                    return true;
                }
                if(yExpl >= (y + h) && (yExpl + this.height) >= y){
                    return true;
                }
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