import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

public class Status{

    private Image symbol;
    
    public Status(Image sym){
        this.symbol = sym;
    }

    public Image getSybmol(){
        return this.symbol;
    }
}