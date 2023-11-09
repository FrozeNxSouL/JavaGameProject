package GameState;

import Audio.AudioPlayer;
import Entities.*;
import LeveL.LevelManager;
import Object.ObjectManager;
import UI.GameComplete;
import UI.GameOver;
import UI.LoadingScreen;
import UI.PauseScreen;
import UtillMethod.Constant.EnvironmentConstant;
import UtillMethod.LoadSave;
import main.Gameclass;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.Timer;

public class Playing extends States implements StateMethods {
    private Player player;
    private EnemyManager enemyManager;
    private GameOver gameOver;
    private GameComplete gameComplete;
    private LoadingScreen loadingScreen;
    private PauseScreen pauseScreen;
    private ObjectManager objectManager;
    private LevelManager levelBack,levelFront,levelFloor,levelObj,levelFloorCollision;
    private int XlvlOffset = 0;
    private int leftBorder;
    private int rightBorder;
    private int lvlTileWide = LoadSave.GettingMapdata(LoadSave.GrassLandLevelSpriteFloor)[0].length;
    private int maxTileWide = lvlTileWide - Gameclass.TILE_IN_WIDTH;
    private int maxlvlOffsetX = maxTileWide*Gameclass.TILE_SIZE;
    private BufferedImage backgroundimg;
    private boolean GameOverStatus = false,spawnPoint=true,firstStart=true,loadingStatus=false,gameCompleted=false,pausing = false;
    private Thread loadingThread;

    private int mapMax=3,mapMin=1,currentMap = mapMin;
    private boolean[] activedEnemy = new boolean[mapMax];

    private float leftPOV = 0.43f,rightPOV = 0.51f,dir = 0.0015f,currentPOV = leftPOV;
    private Timer cameraSlide = new Timer(2, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentPOV += dir;
        }
    });

    public Playing(Gameclass game) {
        super(game);
        initClasses();
    }

    public int getCurrentMap(){
        return currentMap;
    }

    public boolean isLoadingStatus() {
        return loadingStatus;
    }

    private void initClasses() {
        Arrays.fill(activedEnemy, true);
        objectManager = new ObjectManager(this);
        mapSwitcher(spawnPoint);
        gameOver = new GameOver(this);
        AudioPlayer audioPlayer = new AudioPlayer();
        loadingScreen = new LoadingScreen(this);
        gameComplete = new GameComplete(this);
        pauseScreen = new PauseScreen(this);
    }

    public void LostFocusing(){
        player.resetPosition();
    }

    public void callMapSwitcher(boolean point,boolean direction){
        int mapTemp;
        if (direction){
            mapTemp = Math.min(currentMap+1,mapMax);
        } else {
            mapTemp = Math.max(currentMap-1,mapMin);
        }
        if (mapTemp == this.currentMap) {
            return;
        } else {
            this.currentMap = mapTemp;
        }
        loadingStatus=true;
        loadingScreen.setOnLoad(true);
        loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                objectManager.resetAllObjects();
                mapSwitcher(point);
                loadingStatus = false;
                update();
            }
        });
        loadingThread.start();
    }
    public void mapSwitcher(boolean point){
        this.GameOverStatus=false;
        this.spawnPoint = point;
        if (this.currentMap==1) {
            levelBack = new LevelManager(game, LoadSave.GrassLandLevelSpriteBack);
            levelFloor = new LevelManager(game, LoadSave.GrassLandLevelSpriteFloor);
            levelFront = new LevelManager(game, LoadSave.GrassLandLevelSpriteFront);
            levelObj = new LevelManager(game, LoadSave.GrassLandLevelSpriteCheckpoint);
            levelFloorCollision = new LevelManager(game, LoadSave.GrassLandLevelSpriteFloorCollision);
            backgroundimg = LoadSave.LoadSprite(LoadSave.GrassLandLevelBackground);
            lvlTileWide = LoadSave.GettingMapdata(LoadSave.GrassLandLevelSpriteFloor)[0].length;
            if (activedEnemy[this.currentMap - 1]) {
                enemyManager = new EnemyManager(this, objectManager, LoadSave.GrassLandLevelSpriteEnemy);
            }
            if (firstStart) {
                player = new Player((float) 1475, (float) 235, (int) (256 * Gameclass.SCALE), (int) (256 * Gameclass.SCALE), this);
                firstStart = false;
            } else if (spawnPoint){
                player.getHitbox().x = 1475;
                player.setFacing(false);
            } else {
                player.getHitbox().x = (float)(lvlTileWide*Gameclass.TILE_SIZE*0.85);
                player.setFacing(true);
            }
        } else if (this.currentMap==2) {
            levelBack = new LevelManager(game, LoadSave.BlightedForestLevelSpriteBack);
            levelFloor = new LevelManager(game, LoadSave.BlightedForestLevelSpriteFloor);
            levelFront = new LevelManager(game, LoadSave.BlightedForestLevelSpriteFront);
            levelObj = new LevelManager(game, LoadSave.BlightedForestLevelSpriteCheckpoint);
            levelFloorCollision = new LevelManager(game, LoadSave.BlightedForestLevelSpriteFloorCollision);
            backgroundimg = LoadSave.LoadSprite(LoadSave.BlightedForestLevelBackground);
            lvlTileWide = LoadSave.GettingMapdata(LoadSave.BlightedForestLevelSpriteFloor)[0].length;
            if (activedEnemy[this.currentMap - 1]) {
                enemyManager = new EnemyManager(this, objectManager, LoadSave.BlightedForestLevelSpriteEnemy);
            }
            if (spawnPoint) {
                player.getHitbox().x = 1200;
                player.setFacing(false);
            } else {
                player.getHitbox().x = (float)(lvlTileWide*Gameclass.TILE_SIZE*0.9);
                player.setFacing(true);
            }
        } else if (this.currentMap==3) {
            levelBack = new LevelManager(game, LoadSave.BossMapLevelSpriteBack);
            levelFloor = new LevelManager(game, LoadSave.BossMapLevelSpriteFloor);
            levelFront = new LevelManager(game, LoadSave.BossMapLevelSpriteFront);
            levelObj = new LevelManager(game, LoadSave.BossMapLevelSpriteCheckpoint);
            levelFloorCollision = new LevelManager(game, LoadSave.BossMapLevelSpriteFloorCollision);
            backgroundimg = LoadSave.LoadSprite(LoadSave.BossMapLevelBackground1);
            lvlTileWide = LoadSave.GettingMapdata(LoadSave.BossMapLevelSpriteFloor)[0].length;
            if (activedEnemy[this.currentMap - 1]) {
                enemyManager = new EnemyManager(this, objectManager, LoadSave.BossMapLevelSpriteEnemy);
            }
            if (spawnPoint) {
                player.getHitbox().x = 1300;
                player.setFacing(false);
            } else {
                player.getHitbox().x = (float)(lvlTileWide*Gameclass.TILE_SIZE*0.85);
                player.setFacing(true);
            }
        }
        player.LoadlvlData(levelObj.getCurrentLevel().getLvlData(),levelFloorCollision.getCurrentLevel().getLvlData());
        maxTileWide = lvlTileWide - Gameclass.TILE_IN_WIDTH;
        maxlvlOffsetX = maxTileWide*Gameclass.TILE_SIZE;

    }

    @Override
    public void update() {
        if (GameOverStatus || gameCompleted || pausing){
            return;
        }
        enemyManager.update(levelFloorCollision.getCurrentLevel().getLvlData(),player);
        player.update(enemyManager);
        objectManager.update(player);
        CheckCloseBorder();
    }

    private void CheckCloseBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX-XlvlOffset;
        if (player.getFacing()){
            cameraSlide.start();
            dir = Math.abs(dir);
            if (currentPOV >= rightPOV){
                cameraSlide.stop();
            }
            leftBorder = (int) (currentPOV*Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE);
            rightBorder = (int) (currentPOV*Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE);
            if (diff>rightBorder) {
                XlvlOffset += diff - rightBorder;
            } else if (diff<leftBorder) {
                XlvlOffset += diff-leftBorder;
            }
        }
        else {
            cameraSlide.start();
            dir = -Math.abs(dir);
            if (currentPOV <= leftPOV){
                cameraSlide.stop();
            }
            leftBorder = (int) (currentPOV*Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE);
            rightBorder = (int) (currentPOV*Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE);
            if (diff<leftBorder) {
                XlvlOffset += diff-leftBorder;
            } else if (diff>rightBorder){
                XlvlOffset += diff-rightBorder;
            }
        }
        if (XlvlOffset > maxlvlOffsetX){
            XlvlOffset=maxlvlOffsetX;
        } else if (XlvlOffset<0) {
            XlvlOffset=0;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (loadingStatus || loadingScreen.isOnLoad()){
            loadingScreen.drawLoadingScreen(g);
        } else {
            drawBG(g);
            levelBack.Drawmap(g, XlvlOffset);
            levelFloor.Drawmap(g, XlvlOffset);

            player.render(g, XlvlOffset);
            if (activedEnemy[this.currentMap-1]){
                enemyManager.drawEnemy(g, XlvlOffset);
            }
            objectManager.draw(g,XlvlOffset);
            levelFront.Drawmap(g, XlvlOffset);
            levelObj.Drawmap(g, XlvlOffset);
            if (player.isCombatstatus() || player.isSkillActivating()) {
                player.drawUI(g, enemyManager);
            }
            if (pausing){
                pauseScreen.drawPauseScreen(g);
            }
            if (GameOverStatus) {
                gameOver.drawGameover(g);
            }
            if (gameCompleted){
                gameComplete.drawGameComplete(g);
            }
        }
    }

    public void drawBG(Graphics g) {
        g.drawImage(backgroundimg, -(int)(XlvlOffset*0.02), 0 , EnvironmentConstant.BACKGROUND_WIDTH, EnvironmentConstant.BACKGROUND_HEIGHT, null);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (GameOverStatus){
            gameOver.keyPressed(e);
        } else if (gameCompleted) {
            gameComplete.keyPressed(e);
        } else if (pausing) {
            pauseScreen.keyPressed(e);
        } else if (!loadingStatus){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setmoveLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setmoveRight(true);
                    break;
                case KeyEvent.VK_SHIFT:
                    player.setSprint(true);
                    break;
                case KeyEvent.VK_J:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_K:
                    player.setBlock(true);
                    break;
                case KeyEvent.VK_L:
                    player.setRolling(true);
                    break;
                case KeyEvent.VK_E:

                    break;
                case KeyEvent.VK_Q:
                    player.setSkillActivate();
                    break;
                case KeyEvent.VK_F:
                    for (Enemy enemy: enemyManager.getEnemylists()) {
                        if(enemy.getActive()){
                            return;
                        }
                    }
                    activedEnemy[this.currentMap-1] = false;
                    player.setInteract(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    pausing = true;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!GameOverStatus && !gameCompleted &&!pausing) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setmoveLeft(false);
                case KeyEvent.VK_D -> player.setmoveRight(false);
                case KeyEvent.VK_SHIFT -> player.setSprint(false);
                case KeyEvent.VK_K -> player.setBlock(false);
            }
        }
    }

    public void setPausing(boolean pausing) {
        this.pausing = pausing;
    }

    public void setGameOver(boolean b) {
        this.GameOverStatus = b;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    public void ResetGame() {
        this.currentMap = 1;
        this.firstStart = true;
        loadingScreen.setOnLoad(true);
        loadingStatus = true;
        loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Arrays.fill(activedEnemy, true);
                mapSwitcher(true);
                currentPOV = leftPOV;
                GameOverStatus = false;
                gameCompleted = false;
                update();
                loadingStatus = false;
            }
        });
        loadingThread.start();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox,int DMG,Player player) {
        enemyManager.checkHit(attackBox,DMG,player);
    }

    public void bossStateChange(){
        backgroundimg = LoadSave.LoadSprite(LoadSave.BossMapLevelBackground2);
    }
}
