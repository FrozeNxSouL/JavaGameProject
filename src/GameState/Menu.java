package GameState;

import Audio.AudioPlayer;
import UI.Setting;
import UtillMethod.LoadSave;
import main.Gameclass;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Menu extends States implements StateMethods{
    private Playing playing;
    private BufferedImage[] text = new BufferedImage[3];
    private BufferedImage selection;
    private Setting setting;
    private int currentChoice = 0;
    private Color bg = new Color(0,0,0,250);
    private int xPosition = (Gameclass.TILE_SIZE * Gameclass.TILE_IN_WIDTH/2);
    private int yPosition = (int)(Gameclass.TILE_SIZE * Gameclass.TILE_IN_HEIGHT * 0.4);

    public Menu(Gameclass game,Playing playing) {
        super(game);
        BufferedImage img = LoadSave.LoadSprite(LoadSave.MenuSprite);
        for (int i=0;i< text.length;i++){
            text[i] = img.getSubimage(0,256*2 + 256*i,256*2,256);
        }
        selection = img.getSubimage(0,256*5,256*2,256);
        this.playing = playing;
        setting = new Setting(playing);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        if (setting.isInUse()){
            setting.drawSetting(g);
        } else {
            g.setColor(bg);
            g.fillRect(0, 0, Gameclass.GAME_WIDTH, Gameclass.GAME_HEIGHT);
            for (int i = 0; i < text.length; i++) {
                g.drawImage(text[i], xPosition - (int) (200 * Gameclass.SCALE), yPosition - (int) (100 * Gameclass.SCALE) + (int) (50 * Gameclass.SCALE * i), (int) (200 * 2 * Gameclass.SCALE), (int) (200 * Gameclass.SCALE), null);
            }
            g.drawImage(selection, xPosition - (int) (230 * Gameclass.SCALE), yPosition - (int) (100 * Gameclass.SCALE) + (int) (50 * currentChoice * Gameclass.SCALE), (int) (200 * 2 * Gameclass.SCALE), (int) (200 * Gameclass.SCALE), null);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (setting.isInUse()) {
            setting.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    currentChoice++;
                    AudioPlayer.playEffect(AudioPlayer.SELECT);
                    if (currentChoice == 3) {
                        currentChoice = 0;
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    currentChoice--;
                    AudioPlayer.playEffect(AudioPlayer.SELECT);
                    if (currentChoice == -1) {
                        currentChoice = 2;
                    }
                }
                case KeyEvent.VK_ENTER -> {
                    AudioPlayer.playEffect(AudioPlayer.ENTER);
                    if (currentChoice == 0) {
                        GameState.state = GameState.PLAYING;
                    } else if (currentChoice == 1) {
                        setting.setInUse();
                    } else if (currentChoice == 2) {
                        System.exit(0);
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
