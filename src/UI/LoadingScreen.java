package UI;

import GameState.Playing;
import UtillMethod.LoadSave;
import main.Gameclass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class LoadingScreen {
    private Playing playing;
    private BufferedImage bottomText;
    private BufferedImage[] loadingIndicator = new BufferedImage[100];
    private int inc = 0;
    private int xCenter = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2);
    private int yCenter = Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT/2;
    private Color bg;
    private boolean onLoad = true;
    private Timer timer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inc<100) {
                inc += 1;
            } else {
                timer.stop();
                onLoad = false;
            }
        }
    });

    public LoadingScreen(Playing play){
        this.playing = play;
        BufferedImage img = LoadSave.LoadSprite(LoadSave.MenuSprite);
        for (int i=0;i<100;i++) {
            loadingIndicator[i] = img.getSubimage((256 * 2 / 100) * i, 256 * 6, (256 * 2) / 100, 256);
        }
        bottomText = img.getSubimage(0,256*7,256*2,256);
    }

    public void drawLoadingScreen(Graphics g){
        bg = new Color(0,0,0,250);
        g.setColor(bg);
        g.fillRect(0,0, Gameclass.GAME_WIDTH,Gameclass.GAME_HEIGHT);

        g.drawImage(bottomText,(int)(xCenter*2*0.85)- (int)(100 * Gameclass.SCALE),(int)(yCenter*2*0.85) - (int)(100/2 * Gameclass.SCALE),(int)(100*2 * Gameclass.SCALE),(int)(100 * Gameclass.SCALE),null);
        for (int i = 0;i<inc;i++) {
            g.drawImage(loadingIndicator[i], (int)((xCenter*2*0.85) -((128.0*2)* Gameclass.SCALE)+(((128.0 * 2 / 100)* Gameclass.SCALE) * i)), (int)(yCenter*2*0.9)-(int) (128.0/2 * Gameclass.SCALE), (int) ((128.0 * 2 / 100)* Gameclass.SCALE+1), (int) (128.0 * Gameclass.SCALE), null);
        }
        timer.start();
    }

    public boolean isOnLoad() {
        return onLoad;
    }

    public void setOnLoad(boolean onLoad) {
        this.onLoad = onLoad;
        if (this.onLoad){
            inc = 0;
        }
    }
}
