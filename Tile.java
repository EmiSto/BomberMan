import java.awt.Image;

public class Tile{

    private Image image;
    private Bomb bomb;
    private boolean player;
    private Status status;

    public Tile(){
        this.image = null;
        this.bomb = null;
        this.player = false;
    }
    public Tile(Image img){
        this();
        if(img != null){
            this.image = img;
        }
    }
    public Image getImage(){
        return this.image;
    }
    public Status getStatus(){
        return this.status;
    }
    public void setImage(Image img){
        this.image = img;
    }

    public boolean addBomb(Bomb b){
        if(this.bomb == null){
            this.bomb = b;
            return true;
        }
        else{
            return false;
        }
    }
    public void addStatus(Status s){
        this.status = s;
    }
    public void addPlayer(){
        this.player = true;
    }
    public void removePlayer(){
        this.player = false;
    }
    public void removeImage(){
        this.image = null;
    }
    public void removeBomb(){
        this.bomb = null;
    }
    public void removeStatus(){
        this.status = null;
    }

    //Tittar om tile går att gå på
    public boolean obstructed(){
        if(this.image != null){
            return true;
        }
        if(this.bomb != null && this.player == false){
            return true;
        }
        return false;
    }
}