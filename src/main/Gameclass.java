package main;

import GameState.Playing;
import GameState.Menu;
import GameState.GameState;
import java.awt.*;

public class Gameclass implements Runnable{
    private Window gamewindow;
    private Panel panel;
    private Thread gamethread;
    private final int FPS_SET=120;
    private final int UPS_SET=200;
    private Playing playing ;
    private Menu menu;
    private double timeperframe = 1000000000.0/FPS_SET;
    private double timeperupdate = 1000000000.0/UPS_SET;
    private long prevtime;

    private int frames;
    private int update;
    private long lastCheck;
    private double deltaU;
    private double deltaF;
    private long currenttime;

    public static final int TILE_DEFAULT_SIZE = 16;
    public static float SCALE =  (float) 2.3;
    public static final int TILE_IN_WIDTH = 44;
    public static final int TILE_IN_HEIGHT = 24;
    public static final int TILE_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = 1920;
    public static final int GAME_HEIGHT = 1080;

    public Gameclass() {
        panel = new Panel(this);
        SCALE = (float)( 2.3 * (panel.screenHeight/864.0));
        initClasses();
        gamewindow = new Window(panel);

        panel.setFocusable(true);
        panel.requestFocus();
        gameloop();
    }

    private void initClasses() {
        playing = new Playing(this);
        menu = new Menu(this,playing);
    }

    private void gameloop() {
        gamethread = new Thread(this);
        gamethread.start();
    }
    private void update() {
        switch (GameState.state) {
            case PLAYING -> {
                playing.update();
            }
            case MENU -> {
                menu.update();
            }
        }
    }
    public void render(Graphics g){
        switch (GameState.state) {
            case PLAYING -> {
                playing.draw(g);
            }
            case MENU -> {
                menu.draw(g);
            }
        }
    }
    @Override
    public void run() {
        timeperframe = 1000000000.0/FPS_SET;
        timeperupdate = 1000000000.0/UPS_SET;
        prevtime = System.nanoTime();

        frames = 0;
        update = 0;
        lastCheck = System.currentTimeMillis();
        deltaU = 0;
        deltaF = 0;
        while (true) {
            currenttime = System.nanoTime();
            deltaU+= (currenttime-prevtime) / timeperupdate;
            deltaF+= (currenttime-prevtime) / timeperframe;
            prevtime=currenttime;
            if (deltaU>=1){
                update();
                update++;
                deltaU--;
            }
            if (deltaF>=1) {
                panel.repaint();
                frames++;
                deltaF--;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frames = 0;
                update = 0;
            }
        }
    }

    public void LostFocusing(){
        if (GameState.state == GameState.PLAYING){
            playing.LostFocusing();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}
