package input;

import GameState.GameState;
import main.Panel;
import static UtillMethod.Constant.PlayerConstant.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.AWTException;
import javax.swing.Timer;

public class Keyboardinput implements KeyListener {
    private Panel panel;
    public Keyboardinput(Panel panel) {
        this.panel = panel;
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                panel.getGame().getPlaying().keyPressed(e);
            }
            case MENU -> {
                panel.getGame().getMenu().keyPressed(e);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                panel.getGame().getPlaying().keyReleased(e);
            }
            case MENU -> {
                panel.getGame().getMenu().keyReleased(e);
            }
        }
    }
}
