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

public class GameOver {
    private Playing playing;
    private BufferedImage gameover,bottomText;
    private int inc = 0;
    private int xCenter = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2);
    private int yCenter = Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT/2;
    private Color bg;
    private Timer timer = new Timer(20, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inc<235) {
                inc += 3;
            }
        }
    });

    public GameOver(Playing play){
        this.playing = play;
        BufferedImage img = LoadSave.LoadSprite(LoadSave.MenuSprite);
        gameover = img.getSubimage(0,0,256*2,256);
        bottomText = img.getSubimage(0,256,256*2,256);
    }

    public void drawGameover(Graphics g){
        bg = new Color(0,0,0,inc);
        g.setColor(bg);
        g.fillRect(0,0, Gameclass.GAME_WIDTH,Gameclass.GAME_HEIGHT);

        g.drawImage(gameover,xCenter- (int)(256 * Gameclass.SCALE),yCenter - (int)(256/2 * Gameclass.SCALE),(int)(256*2 * Gameclass.SCALE),(int)(256 * Gameclass.SCALE),null);
        timer.start();
        if (inc>=235){
            timer.stop();
            g.drawImage(bottomText,xCenter- (int)(64 * Gameclass.SCALE),(int)(yCenter*2*0.9) - (int)(64/2 * Gameclass.SCALE),(int)(64*2 * Gameclass.SCALE),(int)(64 * Gameclass.SCALE),null);
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE -> {
                timer.stop();
                inc = 0;
                playing.ResetGame();
                GameState.state = GameState.MENU;
            }
            case KeyEvent.VK_ENTER -> {
                timer.stop();
                inc = 0;
                playing.ResetGame();
                GameState.state = GameState.PLAYING;
            }
        }
    }
}
