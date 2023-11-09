package Entities;

import GameState.Playing;
import static UtillMethod.Constant.EnemyConstant.*;
import UtillMethod.LoadSave;
import Object.ObjectManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] BasherIMG;
    private BufferedImage[][] GoblinIMG;
    private BufferedImage[][] WarriorIMG;
    private BufferedImage[][] BossDemonIMG;
    private ArrayList<Enemy> enemylists;
    private ObjectManager objectManager;

    public EnemyManager(Playing play,ObjectManager obj,String file){
        this.playing = play;
        this.objectManager = obj;
        loadAnimateimg();
        enemylists = new ArrayList<>();
        addEnemy(file);
    }

    public ArrayList<Enemy> getEnemylists() {
        return enemylists;
    }

    private void addEnemy(String file) {
        enemylists = LoadSave.getEnemyinMap(file);
    }

    public void update(int[][] lvlData,Player player){
        for (Enemy e: enemylists) {
            e.updateEnemy(lvlData,player);
        }
    }
    public void checkHit(Rectangle2D.Float ATKbox,int DMG,Player player){
        for (Enemy e:enemylists) {
            if (e.getActive()) {
                if (ATKbox.intersects(e.getHitbox())) {
                    e.beingHit(DMG);
                    player.increaseSkill();
                }
            }
        }
    }

    public void drawEnemy(Graphics g,int Xoffset){
        drawEnemyInMap(g,Xoffset);
    }

    private void drawEnemyInMap(Graphics g,int Xoffset) {
        for (Enemy e: enemylists) {
            if (e.getActive()) {
                if (e instanceof Basher) {
                    if (e.getEnemyFacing()) {
                        g.drawImage(BasherIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x - BASHER_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - BASHER_DRAWYOFFSET), BASHER_WIDTH, BASHER_HEIGHT, null);
                    } else {
                        g.drawImage(BasherIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x + 320 - BASHER_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - BASHER_DRAWYOFFSET), -1 * BASHER_WIDTH, BASHER_HEIGHT, null);
                    }
                } else if (e instanceof BossDemon) {
                    if (((BossDemon)e).getState() && !((BossDemon)e).isFirstTransform()){
                        playing.bossStateChange();
                        ((BossDemon)e).setFirstTransform();
                    }
                    if (e.getEnemyFacing()) {
                        g.drawImage(BossDemonIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x - BOSS_DEMON_DRAWXOFFSET) + (int)(BOSS_DEMON_WIDTH) - Xoffset, (int) (e.getHitbox().y - BOSS_DEMON_DRAWYOFFSET),-1 * BOSS_DEMON_WIDTH, BOSS_DEMON_HEIGHT, null);
                    } else {
                        g.drawImage(BossDemonIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x - BOSS_DEMON_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - BOSS_DEMON_DRAWYOFFSET),BOSS_DEMON_WIDTH, BOSS_DEMON_HEIGHT, null);
                    }
                } else if (e instanceof SkeletonWarrior) {
                    if (e.getEnemyFacing()) {
                        g.drawImage(WarriorIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x - WARRIOR_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - WARRIOR_DRAWYOFFSET), WARRIOR_WIDTH, WARRIOR_HEIGHT, null);
                    } else {
                        g.drawImage(WarriorIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x + 480 - WARRIOR_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - WARRIOR_DRAWYOFFSET), -1 * WARRIOR_WIDTH, WARRIOR_HEIGHT, null);
                    }
                } else if (e instanceof Goblin) {
                    if (e.getEnemyFacing()) {
                        g.drawImage(GoblinIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x - GOBLIN_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - GOBLIN_DRAWYOFFSET), GOBLIN_WIDTH, GOBLIN_HEIGHT, null);
                    } else {
                        g.drawImage(GoblinIMG[e.getEnemyState()][e.getAnimateIDX()], (int) (e.getHitbox().x + 480 - GOBLIN_DRAWXOFFSET) - Xoffset, (int) (e.getHitbox().y - GOBLIN_DRAWYOFFSET), -1 * GOBLIN_WIDTH, GOBLIN_HEIGHT, null);
                    }
                }
            } else {
                if (e instanceof BossDemon) {
                    playing.setGameCompleted(true);
                    break;
                }
            }
        }
    }
    private void loadAnimateimg() {
        BasherIMG = new BufferedImage[7][13];
        BufferedImage temp = LoadSave.LoadSprite(LoadSave.SkeletonBasherSprite);
        for (int i = 0;i < BasherIMG.length;i++){
            for (int j = 0;j < BasherIMG[0].length;j++){
                BasherIMG[i][j] = temp.getSubimage(j* BASHER_WIDTH_DEFAULT,i*BASHER_HEIGHT_DEFAULT,BASHER_WIDTH_DEFAULT,BASHER_HEIGHT_DEFAULT);
            }
        }
        BossDemonIMG = new BufferedImage[16][12];
        temp = LoadSave.LoadSprite(LoadSave.BossSprite);
        for (int i = 0;i < BossDemonIMG.length;i++){
            for (int j = 0;j < BossDemonIMG[0].length;j++){
                BossDemonIMG[i][j] = temp.getSubimage(j* BOSS_DEMON_WIDTH_DEFAULT,i*BOSS_DEMON_HEIGHT_DEFAULT,BOSS_DEMON_WIDTH_DEFAULT,BOSS_DEMON_HEIGHT_DEFAULT);
            }
        }
        WarriorIMG = new BufferedImage[10][10];
        temp = LoadSave.LoadSprite(LoadSave.SkeletonWarriorSprite);
        for (int i = 0;i < WarriorIMG.length;i++){
            for (int j = 0;j < WarriorIMG[0].length;j++){
                WarriorIMG[i][j] = temp.getSubimage(j* WARRIOR_WIDTH_DEFAULT,i*WARRIOR_HEIGHT_DEFAULT,WARRIOR_WIDTH_DEFAULT,WARRIOR_HEIGHT_DEFAULT);
            }
        }
        GoblinIMG = new BufferedImage[10][10];
        temp = LoadSave.LoadSprite(LoadSave.GoblinSprite);
        for (int i = 0;i < GoblinIMG.length;i++){
            for (int j = 0;j < GoblinIMG[0].length;j++){
                GoblinIMG[i][j] = temp.getSubimage(j* GOBLIN_WIDTH_DEFAULT,i*GOBLIN_HEIGHT_DEFAULT,GOBLIN_WIDTH_DEFAULT,GOBLIN_HEIGHT_DEFAULT);
            }
        }
    }


    public void resetAll() {
        for (Enemy e:enemylists){
            if (e instanceof Basher) {
                ((Basher)e).resetEnemy();
            } else if (e instanceof BossDemon) {
                ((BossDemon)e).resetEnemy();
            }
        }
    }
}
