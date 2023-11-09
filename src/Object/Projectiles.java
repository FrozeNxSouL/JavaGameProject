package Object;

import main.Gameclass;

import java.awt.geom.Rectangle2D;

import static UtillMethod.Constant.EnvironmentConstant.*;

public class Projectiles {
    protected Rectangle2D.Float hitbox;
    protected int AnimateTick = 0,AnimateSpeed = 12,AnimateIDX=0,ProjectileType,range=0;
    protected boolean active = true,dir;

    public Projectiles(int x,int y,boolean dir,int type){
        hitbox = new Rectangle2D.Float(x + (10 * Gameclass.SCALE),y+(10 * Gameclass.SCALE),PROJECTILES_WIDTH- (10 * Gameclass.SCALE),PROJECTILES_HEIGHT- (10 * Gameclass.SCALE));
        this.dir = dir;
        this.ProjectileType = type;
    }

    public void setPos(int x,int y){
        hitbox.x = x;
        hitbox.y = y;
    }

    private void updateAnimationTick(){
        AnimateTick++;
        if (AnimateTick>=AnimateSpeed){
            AnimateTick=0;
            AnimateIDX++;
            if (AnimateIDX>= getProjectilesSpriteAmount(ProjectileType)) {
                AnimateIDX = 0;
            }
        }
    }

    public int getProjectileType() {
        return ProjectileType;
    }

    public boolean getDir() {
        return dir;
    }

    public int getAnimateIDX() {
        return AnimateIDX;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRange() {
        return range;
    }

    public void updatePos(){
        updateAnimationTick();
        if (dir) {
            hitbox.x += PROJECTILES_SPEED;
        } else {
            hitbox.x -= PROJECTILES_SPEED;
        }
        range += PROJECTILES_SPEED;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
