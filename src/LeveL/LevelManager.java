package LeveL;

import UtillMethod.LoadSave;
import main.Gameclass;

import java.awt.*;
import java.awt.image.BufferedImage;


public class LevelManager {
    private Gameclass Game;
    private BufferedImage[] gamelevelMap = new BufferedImage[18450];
    private Level Map;

    private void importMapSprite(String file,BufferedImage[] Gamelevel) {
        BufferedImage img = LoadSave.LoadSprite(file);
        int idx;
        for (int i=0;i<img.getHeight()/Gameclass.TILE_DEFAULT_SIZE;i++){
            for (int j=0;j<img.getWidth()/Gameclass.TILE_DEFAULT_SIZE;j++){
                idx = i*(img.getWidth()/Gameclass.TILE_DEFAULT_SIZE) + j;
                Gamelevel[idx] = img.getSubimage(j*Gameclass.TILE_DEFAULT_SIZE,i*Gameclass.TILE_DEFAULT_SIZE,Gameclass.TILE_DEFAULT_SIZE,Gameclass.TILE_DEFAULT_SIZE);
            }
        }
    }

    public LevelManager(Gameclass G,String file){
        this.Game = G;
        Map = new Level(LoadSave.GettingMapdata(file));
        importMapSprite(file, this.gamelevelMap);
    }

    public void Drawmap(Graphics G, int xlvlOffset){
        int idx;
        for (int i=20;i< Map.getLvlData().length;i++){
            for (int j=(int)Math.floor((xlvlOffset)/Gameclass.TILE_SIZE);j<Gameclass.TILE_IN_WIDTH + (int)Math.floor(xlvlOffset/Gameclass.TILE_SIZE);j++){
                idx = Map.getSpriteIndex(i,j);
                if (idx!=0) {
                    G.drawImage(gamelevelMap[idx], j * (int) (Gameclass.TILE_SIZE) - (int)(xlvlOffset), i * (int) (Gameclass.TILE_SIZE) - (int)(20*Gameclass.TILE_SIZE), (int) (Gameclass.TILE_SIZE), (int) (Gameclass.TILE_SIZE), null);
                }
            }
        }
    }
    public Level getCurrentLevel(){
        return Map;
    }
}
