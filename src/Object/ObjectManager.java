package Object;

import Entities.Player;
import GameState.Playing;
import UtillMethod.LoadSave;
import main.Gameclass;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static UtillMethod.Constant.EnvironmentConstant.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[] redBallImg, fireImg;
    private static ArrayList<Projectiles> projectiles = new ArrayList<>();

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage Img = LoadSave.LoadSprite(LoadSave.ProjectilesSprite);
        redBallImg = new BufferedImage[4];
        fireImg = new BufferedImage[4];
        for (int i = 0;i< redBallImg.length;i++){
            redBallImg[i] = Img.getSubimage(i*PROJECTILES_WIDTH_DEFAULT,0,PROJECTILES_WIDTH_DEFAULT,PROJECTILES_HEIGHT_DEFAULT);
            fireImg[i] = Img.getSubimage(i*PROJECTILES_WIDTH_DEFAULT,PROJECTILES_HEIGHT_DEFAULT,PROJECTILES_WIDTH_DEFAULT,PROJECTILES_HEIGHT_DEFAULT);
        }
    }

    public void update(Player player) {
        updateProjectiles(player);
    }

    private void updateProjectiles(Player player) {
        for (Projectiles p : projectiles) {
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox()) && !player.getRollStatus() && !player.getBlockStatus()) {
                    player.changeHealth(getProjectilesDamage(p.getProjectileType()));
                    p.setActive(false);
                } else if (IsProjectileOutOfRange(p))
                    p.setActive(false);
            }
        }
    }

    private boolean IsProjectileOutOfRange(Projectiles p) {
        return p.getRange() > 3000;
    }

    public static void callProjectile(int x,int y,boolean dir,int type) {
        projectiles.add(new Projectiles((int) x, (int)y, dir,type));
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectiles p : projectiles) {
            if (p.isActive()) {
                if (p.getProjectileType() == FIRE) {
                    if (p.getDir()) {
                        g.drawImage(fireImg[p.getAnimateIDX()], (int) (p.getHitbox().x + PROJECTILES_WIDTH - xLvlOffset -  (10 * Gameclass.SCALE)), (int) (p.getHitbox().y -  (10 * Gameclass.SCALE)), -1*PROJECTILES_WIDTH, PROJECTILES_HEIGHT, null);
                    } else {
                        g.drawImage(fireImg[p.getAnimateIDX()], (int) (p.getHitbox().x - xLvlOffset - (10 * Gameclass.SCALE)  - PROJECTILES_WIDTH), (int) (p.getHitbox().y -  (10 * Gameclass.SCALE)), PROJECTILES_WIDTH, PROJECTILES_HEIGHT, null);
                    }
                } else {
                    if (p.getDir()) {
                        g.drawImage(redBallImg[p.getAnimateIDX()], (int) (p.getHitbox().x - xLvlOffset - (10 * Gameclass.SCALE)), (int) (p.getHitbox().y - (10 * Gameclass.SCALE)), PROJECTILES_WIDTH, PROJECTILES_HEIGHT, null);
                    } else {
                        g.drawImage(redBallImg[p.getAnimateIDX()], (int) (p.getHitbox().x - xLvlOffset - (10 * Gameclass.SCALE) - PROJECTILES_WIDTH), (int) (p.getHitbox().y - (10 * Gameclass.SCALE)), PROJECTILES_WIDTH, PROJECTILES_HEIGHT, null);
                    }
                }
            }
        }
    }

    public void resetAllObjects() {
        projectiles.clear();
    }
}