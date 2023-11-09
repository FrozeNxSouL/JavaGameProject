package UI;

import Audio.AudioPlayer;
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

public class Setting {
    private Playing playing;
    private BufferedImage selectionBar,topic,counterInterval,BGM,SFX,invincibleMode,slideBar;
    private BufferedImage[] button = new BufferedImage[2];
    private int xCenter = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2);
    private int yCenter = Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT/2;
    private int currentChoice=0;
    public static int counterChoice = 5;
    public static int SFXChoice = 5;
    private int xPosition = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2) - (int)(200*Gameclass.SCALE);
    private int yPosition = -(int)(Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT * 0.1);

    private int slideBarOffsetX = (int)(47*Gameclass.SCALE);
    private int slideBarOffsetY = (int)(153*Gameclass.SCALE);
    private int slideBarInc = (int) (17*Gameclass.SCALE);

    private boolean inUse = false;
    public static boolean invincibleChoice = false;
    private Color bg = new Color(0,0,0,250);

    public Setting(Playing play){
        this.playing = play;
        BufferedImage img = LoadSave.LoadSprite(LoadSave.MenuSprite);
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

    public boolean isInUse() {
        return inUse;
    }

    public static void setAllSetting(int counter,int SFX,boolean invincible){
        counterChoice = counter;
        SFXChoice = SFX;
        invincibleChoice = invincible;
    }

    public void setInUse() {
        this.inUse = true;
    }

    public static int getCounterChoice() {
        return counterChoice*100;
    }

    public static float getSFXChoice() {
        return (float) (SFXChoice/10.0);
    }

    public static boolean isInvincibleChoice() {
        return invincibleChoice;
    }

    public void drawSetting(Graphics g){
        g.setColor(bg);
        g.fillRect(0,0, Gameclass.GAME_WIDTH,Gameclass.GAME_HEIGHT);

        g.drawImage(topic,xPosition,yPosition,(int)(200*2*Gameclass.SCALE),(int)(200*Gameclass.SCALE),null);

        g.drawImage(invincibleMode,xPosition,yPosition + (int)(120*Gameclass.SCALE),(int)(100*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE*0.75),null);
        if (invincibleChoice){
            g.drawImage(button[0], xPosition + (int) (50 * Gameclass.SCALE), yPosition + (int) (120 * Gameclass.SCALE) + (int) (30 * Gameclass.SCALE * 0.75), (int) (50 * Gameclass.SCALE), (int) (50 * Gameclass.SCALE), null);
        } else {
            g.drawImage(button[1], xPosition + (int) (50 * Gameclass.SCALE), yPosition + (int) (120 * Gameclass.SCALE) + (int) (30 * Gameclass.SCALE * 0.75), (int) (50 * Gameclass.SCALE), (int) (50 * Gameclass.SCALE), null);
        }
        g.drawImage(counterInterval,xPosition,yPosition + (int)(120*Gameclass.SCALE) + (int)(100*Gameclass.SCALE*0.75),(int)(200*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE*0.75),null);
        g.drawImage(slideBar,xPosition + slideBarOffsetX + (int)(slideBarInc*counterChoice),yPosition + slideBarOffsetY + (int)(100*Gameclass.SCALE*0.75),(int)(200*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE*0.5),null);

        g.drawImage(SFX,xPosition,yPosition + (int)(120*Gameclass.SCALE) + (int)(100*2*Gameclass.SCALE*0.75),(int)(200*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE*0.75),null);
        g.drawImage(slideBar,xPosition + slideBarOffsetX + (int)(slideBarInc*SFXChoice),yPosition + slideBarOffsetY + (int)(100*2*Gameclass.SCALE*0.75),(int)(200*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE*0.5),null);

        g.drawImage(selectionBar,xPosition,yPosition + (int)(150*Gameclass.SCALE)+ (int)(100*currentChoice*Gameclass.SCALE*0.75) - (int)(100*Gameclass.SCALE*0.75),(int)(200*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE),null);
        g.drawImage(selectionBar,xPosition,yPosition + (int)(150*Gameclass.SCALE)+ (int)(100*currentChoice*Gameclass.SCALE*0.75),(int)(200*2*Gameclass.SCALE*0.75),(int)(100*Gameclass.SCALE),null);

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
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
            case KeyEvent.VK_ESCAPE -> {
                currentChoice = 0;
                AudioPlayer.playEffect(AudioPlayer.SELECT);
                inUse = false;
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

