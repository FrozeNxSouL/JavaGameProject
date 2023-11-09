package Entities;

import main.Gameclass;
import java.awt.geom.Rectangle2D;
import static UtillMethod.Constant.EnemyConstant.*;
import static UtillMethod.HelpMethod.CanMoveHere;

public abstract class Enemy extends Entities{
    protected int AnimateIDX=0,AnimateTick=0,enemyState,enemyType=0,AnimateSpeed = 15,count=0,ObserveRangeMax,ObserveRangeMin;
    protected boolean facing = true;
    protected float attackRange;
    protected int MaxHealth;
    protected int CurrentHealth;
    protected boolean active = true,attackCheck = false;

    public Enemy(float X, float Y, int W, int H,int type) {
        super(X, Y, W, H);
        this.enemyType = type;
        this.MaxHealth = GetMaxHealth(type);
        this.CurrentHealth = this.MaxHealth;
        initHitbox(x,y,width,height);
    }
    public abstract void updateEnemy(int[][] lvlData, Player player);

    public int getCurrentHealth() {
        return CurrentHealth;
    }

    protected void turnToPlayer(Player player){
        if (player.hitbox.x > hitbox.x){
            facing = true;
        } else {
            facing = false;
        }
    }

    protected boolean inRange(Player player){
        int range =  (int)(Math.abs((hitbox.x + hitbox.width/2) - player.hitbox.x));
        return range <= attackRange * 5;
    }
    protected boolean inAttackRange(Player player){
        int range =  (int)(Math.abs((hitbox.x + hitbox.width/2) - player.hitbox.x));
        return range <= attackRange;
    }

    protected void changeState(int state){
        this.enemyState = state;
        AnimateIDX = 0;
        AnimateTick = 0;
        AnimateSpeed = 15;
    }

    protected void Moveable(int X,int[][] lvlData){
        if (CanMoveHere(hitbox.x+X, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += X;
            for (int i = 32;i>=4;i-=4) {
                if (CanMoveHere(hitbox.x, hitbox.y + i*2, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += i;
                    break;
                }
            }
        } else {
            for (int i = 4;i<=32;i+=4) {
                if (CanMoveHere(hitbox.x+X, hitbox.y - i*2, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.x += X;
                    hitbox.y -= i;
                    break;
                }
            }
        }
    }
    protected abstract void checkEnemyHit(Rectangle2D.Float ATKbox, Player player);
    public abstract void beingHit(int value);
    public abstract void resetEnemy();
    protected abstract void updateMovement(int[][] lvlData,Player player);

    public boolean getEnemyFacing(){
        return facing;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public int getAnimateIDX() {
        return AnimateIDX;
    }

    public boolean getActive() {
        return active;
    }

}
