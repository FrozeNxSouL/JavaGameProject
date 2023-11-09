package UI;

import GameState.GameState;
import GameState.Playing;
import UtillMethod.LoadSave;
import main.Gameclass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameComplete {
    private Playing playing;
    private BufferedImage gameComplete,bottomText,bossKilled;
    private int xCenter = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2);
    private int yCenter = Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT/2;
    private int inc = 0;
    private Color bg;
    private Timer timer1 = new Timer(20, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inc<230) {
                inc += 2;
            }
        }
    });

    public GameComplete(Playing play){
        this.playing = play;
        BufferedImage img = LoadSave.LoadSprite(LoadSave.MenuSprite);
        gameComplete = img.getSubimage(256*2,256*3,256*2,256);
        bossKilled = img.getSubimage(256*2,256*2,256*2,256);
        bottomText = img.getSubimage(0,256,256*2,256);
    }

    public void drawGameComplete(Graphics g){
        bg = new Color(0,0,0,inc);
        g.setColor(bg);
        g.fillRect(0,0, Gameclass.GAME_WIDTH,Gameclass.GAME_HEIGHT);
        if (inc<230) {
            g.drawImage(bossKilled, xCenter - (int) (256 * Gameclass.SCALE), yCenter - (int) (256 / 2 * Gameclass.SCALE), (int) (256 * 2 * Gameclass.SCALE), (int) (256 * Gameclass.SCALE), null);
        }
        timer1.start();
        if (inc>=230){
            timer1.stop();
            g.drawImage(gameComplete, xCenter - (int) (256 * Gameclass.SCALE), yCenter - (int) (256 / 2 * Gameclass.SCALE), (int) (256 * 2 * Gameclass.SCALE), (int) (256 * Gameclass.SCALE), null);
            g.drawImage(bottomText,xCenter- (int)(64 * Gameclass.SCALE),(int)(yCenter*2*0.9) - (int)(64/2 * Gameclass.SCALE),(int)(64*2 * Gameclass.SCALE),(int)(64 * Gameclass.SCALE),null);
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE -> {
                timer1.stop();
                inc = 0;
                playing.ResetGame();
                GameState.state = GameState.MENU;
            }
            case KeyEvent.VK_ENTER -> {
                timer1.stop();
                inc = 0;
                playing.ResetGame();
                GameState.state = GameState.PLAYING;
            }
        }
    }
}
