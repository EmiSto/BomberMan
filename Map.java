import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Map{

    private Image[][] tiles;
    private int tileWidth;
    private int tileHeight;
    private int nrTilesW;
    private int nrTilesH;

    //width och height är hur mågna rutor på bredden resp höjden
    public Map(int width, int height, int tw, int th){
        this.tiles = new Image[width][height];
        this.nrTilesW = width;
        this.nrTilesH = height;
        this.tileWidth = tw;
        this.tileHeight = th;
    }

    public Image getTile(int x, int y){
        int tileW = x / this.tileWidth;
		int tileH = y / this.tileHeight;
        
        return this.tiles[tileW][tileH];
    }
    public Rectangle getTileBounds(int x, int y){
        int tileW = x / this.tileWidth;
        int tileH = y / this.tileHeight;
        if(this.tiles[tileW][tileH] != null){
            Rectangle r = new Rectangle(tileW * this.tileWidth, tileH * this.tileHeight, this.tileWidth, this.tileHeight);
            return r;
        }
        return null;
    }

    public void insertBomb(Bomb b, int x, int y){
        int tileW = x / this.tileWidth;
        int tileH = y / this.tileHeight;
        
        b.setPos(tileW * this.tileWidth, tileH * this.tileHeight);
    }

    public void paint(Graphics2D g){
        for(int i = 0; i<tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                if(tiles[i][j] != null){
                    g.drawImage(tiles[i][j], i*tileWidth, j*tileHeight, (i+1)*tileWidth, (j+1)*tileHeight, 0, 0, 20, 20,null);
                    //g.drawImage(tiles[i][j], i*tileWidth, j*tileHeight, null);
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
            String[] lines = new String[this.nrTilesH];
            int counter = 0;
            //Fyller en array med alla rader i textfilen
            while((line = reader.readLine()) != null){
                lines[counter] = line;
                counter++;
            }

            //Fyller mappen med images
            for(int i = 0; i < this.nrTilesH; i++){
                for(int j = 0; j < this.nrTilesW; j++){
                    char ch = lines[j].charAt(i);
                    Image icon;
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
                    this.tiles[i][j] = icon;
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