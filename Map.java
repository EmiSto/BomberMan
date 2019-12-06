import java.math.*;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Map{

    private Tile[][] tiles;
    private int tileWidth;
    private int tileHeight;
    private int nrTilesW;
    private int nrTilesH;
    private int nrStatuses;
    private ArrayList<Status> status;

    //width och height är hur mågna rutor på bredden resp höjden
    public Map(int width, int height, int tw, int th){
        this.tiles = new Tile[width][height];
        this.nrTilesW = width;
        this.nrTilesH = height;
        this.tileWidth = tw;
        this.tileHeight = th;

        this.status = new ArrayList<Status>();
    }

    public Tile getTile(int x, int y){
        int tileW = x / this.tileWidth;
		int tileH = y / this.tileHeight;
        
        return this.tiles[tileW][tileH];
    }
    /*public Rectangle getTileBounds(int x, int y){
        int tileW = x / this.tileWidth;
        int tileH = y / this.tileHeight;
        if(this.tiles[tileW][tileH] != null){
            Rectangle r = new Rectangle(tileW * this.tileWidth, tileH * this.tileHeight, this.tileWidth, this.tileHeight);
            return r;
        }
        return null;
    }*/

    public void insertBomb(Bomb b, int x, int y){
        int tileW = x / this.tileWidth;
        int tileH = y / this.tileHeight;
        
        b.setPos(tileW * this.tileWidth, tileH * this.tileHeight);
        this.tiles[tileW][tileH].addBomb(b);
    }
    public void spawnStatus(){
        this.tiles[3][1].addStatus(this.status.get(0));
    }
    public void addPlayer(Rectangle player){
        int x1 = (int)player.getX();
        int x2 = (int)(player.getX() + player.getWidth());
        int y1 = (int)player.getY();
        int y2 = (int)(player.getY() + player.getHeight());
        

        Tile t1 = tileTranslate(x1, y1);
        Tile t2 = tileTranslate(x2, y2);
        Tile t3 = tileTranslate(x1, y2);
        Tile t4 = tileTranslate(x2, y1);

        t1.addPlayer();
        t2.addPlayer();
        t3.addPlayer();
        t4.addPlayer();
    }

    //
    public void removePlayer(Rectangle player){
        int x1 = (int)player.getX();
        int x2 = (int)(player.getX() + player.getWidth());
        int y1 = (int)player.getY();
        int y2 = (int)(player.getY() + player.getHeight());
        

        Tile t1 = tileTranslate(x1, y1);
        Tile t2 = tileTranslate(x2, y2);
        Tile t3 = tileTranslate(x1, y2);
        Tile t4 = tileTranslate(x2, y1);

        t1.removePlayer();
        t2.removePlayer();
        t3.removePlayer();
        t4.removePlayer();

    }
    private Tile tileTranslate(int x, int y){
        int tileW = x / this.tileWidth;
		int tileH = y / this.tileHeight;
        
        return this.tiles[tileW][tileH];
    }

    //Ser till att påverka rutorna som blir träffade av exposionen 
    public void bombExplode(Explosion e){
        int x = e.getOriginX();
        int y = e.getOriginY();
        int expSize = e.getExpSize();
        //Räkna ut hur många tiles som blir påverkade
        int affectedTiles = expSize / this.tileWidth;
        //Tile 0
        int tileW = x / this.tileWidth;
        int tileH = y / this.tileHeight;
        this.tiles[tileW][tileH].removeImage();
        this.tiles[tileW][tileH].removeBomb();

        for (int i = 0; i < 4; i++) {
            // 4 för att det är fyra explosioner
            //Sin och cos gör att varje explosion tas en i taget i en cirkel
            for (int j = 1; j <= affectedTiles; j++) {
                int widthOffset = (int) Math.cos((Math.PI / 2) * i);
                int heightOffset = (int) Math.sin((Math.PI / 2) * i);
                int adjustedW = tileW + (widthOffset * j);
                int adjustedH = tileH + (heightOffset * j);

                if (adjustedW < this.nrTilesW && adjustedW >= 0 && adjustedH < this.nrTilesH && adjustedH >= 0) {
                    this.tiles[adjustedW][adjustedH].removeImage();
                }
            }
        }

    }

    public void paint(Graphics2D g){
        for(int i = 0; i<tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                //Ritar miljön
                if(tiles[i][j].getImage() != null){
                    g.drawImage(tiles[i][j].getImage(), i*tileWidth, j*tileHeight, (i+1)*tileWidth, (j+1)*tileHeight, 0, 0, 32, 32,null);
                }
                //Ritar statusar
                Status s = tiles[i][j].getStatus();
                if(s != null){
                    //Udda siffror här för det är så stor den är (106 x 94)
                    g.drawImage(s.getSybmol(), i*tileWidth, j*tileHeight, (i+1)*tileWidth, (j+1)*tileHeight, 0, 0, 106, 94,null);
                }
            }
        }
    }

    //Läser av mappen och fyller tiles med images
    //Returnar true om det lyckades, false annars
    public boolean loadMap(String mapFile){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(mapFile));
            String line;
            String[] lines = new String[this.nrTilesH + 1];
            int counter = 0;
            //Fyller en array med alla rader i textfilen
            while((line = reader.readLine()) != null){
                lines[counter] = line;
                counter++;
            }
            //Hämtar och skapar listan med statusar
            this.nrStatuses = Character.getNumericValue(lines[0].charAt(0));
            Image icon;
            for(int i = 0; i < this.nrStatuses; i++){
                ImageIcon ii = new ImageIcon("Icons/Map/Power.png");
                icon = ii.getImage();
                Status newS = new Status(icon);
                this.status.add(newS);
            }
            //Fyller mappen med images
            for(int i = 0; i < this.nrTilesH; i++){
                for(int j = 0; j < this.nrTilesW; j++){
                    char ch = lines[i+1].charAt(j);
                    //System.out.println("Första karaktären " + ch);
                    //Image icon;
                    if(ch == 'S'){
                        ImageIcon ii = new ImageIcon("Icons/Map/Stone.png");
                        icon = ii.getImage();
                    }
                    else if(ch == 'T'){
                        ImageIcon ii = new ImageIcon("Icons/Map/Wood.png");
                        icon = ii.getImage();
                    }
                    else if(ch == ' '){
                        icon = null;
                    }
                    else{
                        icon = null;
                    }
                    this.tiles[j][i] = new Tile(icon);
                }
            }
            reader.close();
            return true;
        }catch(IOException e){
            System.out.println("Jag kan inte den här mappen :( \n " + e);
            return false;
        }
    }
}