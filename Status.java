import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

public class Status{

    private Image symbol;
    private String name;
    
    public Status(Image sym, String n){
        this.symbol = sym;
        this.name = n;
    }

    public String getName(){
        return this.name;
    }

    public Image getSybmol(){
        return this.symbol;
    }
}