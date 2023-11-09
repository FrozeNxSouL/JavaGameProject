package UI;

import Audio.AudioPlayer;
import GameState.GameState;
import GameState.Playing;
import UtillMethod.LoadSave;
import main.Gameclass;
import static UI.Setting.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PauseScreen {
    private Playing playing;
    private BufferedImage BG,selectionBar,topic,counterInterval,BGM,SFX,invincibleMode,slideBar;
    private BufferedImage[] button = new BufferedImage[2];
    private int xCenter = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2);
    private int yCenter = Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT/2;
    private int currentChoice=0;
    private int inc = 230;
    private int xPosition = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2) - (int)(100*Gameclass.SCALE);
    private int yPosition = (int)(Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT * 0.01);

    private int slideBarOffsetX = (int)(30*Gameclass.SCALE);
    private int slideBarOffsetY = (int)(145*Gameclass.SCALE);
    private int slideBarInc = (int) (12*Gameclass.SCALE);
    private Color bg = new Color(0,0,0,inc);
    private Timer timer = new Timer(20, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inc<230) {
                inc += 5;
            }
        }
    });

    public PauseScreen(Playing play){
        this.playing = play;
        BufferedImage img = LoadSave.LoadSprite(LoadSave.MenuSprite);
        BG = img.getSubimage(256*4,0,256*4,256*2);
        selectionBar = img.getSubimage(0,256*6,256*2,256);
        counterInterval = img.getSubimage(256*2,0,256*2,128);
        SFX = img.getSubimage(256*2,128,256*2,128);
        BGM = img.getSubimage(256*2,128*2,256*2,128);
        invincibleMode = img.getSubimage(256*2,128*3,256,128);
        button[0] = img.getSubimage(256*3,128*3,64,64);
        button[1] = img.getSubimage(256*3 + 128,128*3,64,64);
        topic = img.getSubimage(0,256 * 3,256*2,256);
        slideBar = img.getSubimage(0,256*5,256*2,256);
    }


    public void drawPauseScreen(Graphics g){
        g.setColor(bg);
        g.fillRect(0,0, Gameclass.GAME_WIDTH,Gameclass.GAME_HEIGHT);
        g.drawImage(BG, xCenter - (int) (256 * Gameclass.SCALE), yCenter - (int) (256/2 * Gameclass.SCALE), (int) (256 * 2 * Gameclass.SCALE), (int) ((256) * Gameclass.SCALE), null);
        g.drawImage(invincibleMode,xPosition,yPosition + (int)(120*Gameclass.SCALE),(int)(100*2*Gameclass.SCALE*0.5),(int)(100*Gameclass.SCALE*0.5),null);
        if (invincibleChoice){
            g.drawImage(button[0], xPosition + (int) (30 * Gameclass.SCALE), yPosition + (int) (125 * Gameclass.SCALE) + (int) (30 * Gameclass.SCALE*0.5), (int) (50 * Gameclass.SCALE*0.5), (int) (50 * Gameclass.SCALE*0.5), null);
        } else {
            g.drawImage(button[1], xPosition + (int) (30 * Gameclass.SCALE), yPosition + (int) (125 * Gameclass.SCALE) + (int) (30 * Gameclass.SCALE*0.5), (int) (50 * Gameclass.SCALE*0.5), (int) (50 * Gameclass.SCALE*0.5), null);
        }
        g.drawImage(counterInterval,xPosition,yPosition + (int)(120*Gameclass.SCALE) + (int)(100*Gameclass.SCALE*0.5),(int)(200*2*Gameclass.SCALE*0.5),(int)(100*Gameclass.SCALE*0.5),null);
        g.drawImage(slideBar,xPosition + slideBarOffsetX + (int)(slideBarInc*counterChoice),yPosition + slideBarOffsetY + (int)(100*Gameclass.SCALE*0.5),(int)(200*2*Gameclass.SCALE*0.3),(int)(100*Gameclass.SCALE*0.3),null);

        g.drawImage(SFX,xPosition,yPosition + (int)(120*Gameclass.SCALE) + (int)(100*2*Gameclass.SCALE*0.5),(int)(200*2*Gameclass.SCALE*0.5),(int)(100*Gameclass.SCALE*0.5),null);
        g.drawImage(slideBar,xPosition + slideBarOffsetX + (int)(slideBarInc*SFXChoice),yPosition + slideBarOffsetY + (int)(100*2*Gameclass.SCALE*0.5),(int)(200*2*Gameclass.SCALE*0.3),(int)(100*Gameclass.SCALE*0.3),null);

        g.drawImage(selectionBar,xPosition,yPosition + (int)(120*Gameclass.SCALE)+ (int)(100*currentChoice*Gameclass.SCALE*0.5) - (int)(100*Gameclass.SCALE*0.5),(int)(200*2*Gameclass.SCALE*0.5),(int)(100*Gameclass.SCALE),null);
        g.drawImage(selectionBar,xPosition,yPosition + (int)(120*Gameclass.SCALE)+ (int)(100*currentChoice*Gameclass.SCALE*0.5),(int)(200*2*Gameclass.SCALE*0.5),(int)(100*Gameclass.SCALE),null);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE -> {
                currentChoice = 0;
                AudioPlayer.playEffect(AudioPlayer.SELECT);
                playing.setPausing(false);
            }
            case KeyEvent.VK_DOWN,KeyEvent.VK_S -> {
                currentChoice++;
                AudioPlayer.playEffect(AudioPlayer.SELECT);
                if (currentChoice==3) {
                    currentChoice=0;
                }
            }
            case KeyEvent.VK_UP,KeyEvent.VK_W -> {
                currentChoice--;
                AudioPlayer.playEffect(AudioPlayer.SELECT);
                if (currentChoice==-1) {
                    currentChoice=2;
                }
            }
            case KeyEvent.VK_D,KeyEvent.VK_RIGHT-> {
                if (currentChoice==1) {
                    counterChoice = Math.max(Math.min(counterChoice+1,10),1);
                    AudioPlayer.playEffect(AudioPlayer.SELECT);
                } else if (currentChoice==2) {
                    SFXChoice = Math.max(Math.min(SFXChoice+1,10),1);
                    AudioPlayer.playEffect(AudioPlayer.SELECT);
                }
            }
            case KeyEvent.VK_A,KeyEvent.VK_LEFT-> {
                if (currentChoice==1) {
                    counterChoice = Math.min(Math.max(counterChoice-1,1),10);
                    AudioPlayer.playEffect(AudioPlayer.SELECT);
                } else if (currentChoice==2) {
                    SFXChoice = Math.min(Math.max(SFXChoice-1,1),10);
                    AudioPlayer.playEffect(AudioPlayer.SELECT);
                }
            }
            default -> {
                if (currentChoice==0){
                    invincibleChoice = !invincibleChoice;
                    AudioPlayer.playEffect(AudioPlayer.ENTER);
                }
            }
        }
    }
}
