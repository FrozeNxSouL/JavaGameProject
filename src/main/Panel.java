package main;
import input.Keyboardinput;

import static main.Gameclass.GAME_HEIGHT;
import static main.Gameclass.GAME_WIDTH;
import javax.swing.JPanel;
import java.awt.*;

public class Panel extends JPanel {
    private Gameclass game;
    public int screenWidth,screenHeight;
    public  Panel(Gameclass game) {
        setPanalSize();
        this.game = game;
        addKeyListener(new Keyboardinput(this));
    }

    private void setPanalSize(){
        Toolkit toolkit =  Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        screenWidth = (int) dim.getWidth();
        screenHeight = (int) dim.getHeight();
        setPreferredSize(new Dimension(screenWidth,screenHeight));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }
    public Gameclass getGame(){
        return game;
    }
}

