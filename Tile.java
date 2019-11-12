import java.awt.Image;

public class Tile{

    private Image image;
    private Bomb bomb;

    public Tile(Image img){
        this.image = img;
        this.bomb = null;
    }

    public boolean addBomb(Bomb b){
        if(this.bomb != null){
            this.bomb = b;
            return true;
        }
        else{
            return false;
        }

    }
    public void removeImage(){
        this.image = null;
    }
    public void setImage(Image img){
        this.image = img;
    }
}