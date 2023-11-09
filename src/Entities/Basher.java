package Entities;

import Audio.AudioPlayer;
import main.Gameclass;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import static UtillMethod.Constant.EnemyConstant.*;

public class Basher extends Enemy{
    private int Xtemp;
    private Rectangle2D.Float attackBox;
    private int randomAtk;
    public Basher(float X, float Y) {
        super(X, Y, BASHER_WIDTH, BASHER_HEIGHT, BASHER);
        initHitbox(X + BASHER_DRAWXOFFSET ,Y + BASHER_DRAWYOFFSET,(int)(0.2* BASHER_WIDTH),(int)(0.42* BASHER_HEIGHT));
        initAttackBox();
        enemyState = BASHER_WALK;
        attackRange = (float) (2.75 * Gameclass.TILE_SIZE);
        ObserveRangeMax=(int)Math.floor(Math.random() * (100 - (-100) + 1) - 100);
        ObserveRangeMin=(int)Math.floor(Math.random() * ((-800) - (-500) + 1) - 500);
        hitbox.x+=(int)Math.floor(Math.random() * (this.ObserveRangeMax - (this.ObserveRangeMin) + 1) + this.ObserveRangeMin);
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(hitbox.x- (40*Gameclass.SCALE),hitbox.y - 10*Gameclass.SCALE,hitbox.width +(80*Gameclass.SCALE),hitbox.height + 10*Gameclass.SCALE);
    }

    @Override
    public void updateEnemy(int[][] lvlData,Player player){
        updateAnimationTick();
        updateMovement(lvlData,player);
        updateAttackBox();
    }

    public void updateAttackBox(){
        attackBox.y = hitbox.y - 10*Gameclass.SCALE;
        if (facing){
            attackBox.x = hitbox.x- (35*Gameclass.SCALE);
        } else {
            attackBox.x = hitbox.x- (40*Gameclass.SCALE);
        }
    }
    protected void updateAnimationTick(){
        AnimateTick++;
        if (AnimateTick>=AnimateSpeed){
            AnimateTick=0;
            AnimateIDX++;
            if (AnimateIDX>= getSpriteAmount(enemyType,enemyState)) {
                AnimateIDX = 0;
                switch (enemyState){
                    case BASHER_PREPARE -> {
                        AnimateSpeed = 15;
                        randomAtk = (int)Math.floor(Math.random() * (2 - (1) + 1) + 1);
                        enemyState = BASHER_ATTACKONE;
                        AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        attackCheck = false;
                    }
                    case BASHER_ATTACKONE -> {
                        if (randomAtk==2) {
                            enemyState = BASHER_ATTACKTWO;
                            AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        } else {
                            enemyState = BASHER_WALK;
                        }
                        attackCheck = false;
                    }
                    case BASHER_ATTACKTWO, BASHER_HIT -> {
                        enemyState = BASHER_WALK;
                        AnimateSpeed = 15;
                    }
                    case BASHER_FALL -> active = false;
                }
            }
        }
    }
    protected void checkEnemyHit(Rectangle2D.Float ATKbox, Player player) {
        if (ATKbox.intersects(player.getHitbox()) && player.getCounterInteract()){
            changeState(BASHER_HIT);
            player.Countered();
            AnimateSpeed = 240/getSpriteAmount(enemyType,enemyState);
        } else if (ATKbox.intersects(player.getHitbox()) && !player.getBlockStatus() && !player.getRollStatus() && !attackCheck){
            player.changeHealth(GetEnemyDMG(enemyType));
            attackCheck = true;
        } else if (ATKbox.intersects(player.getHitbox()) && player.getBlockStatus() && !attackCheck) {
            player.Blocked();
            attackCheck = true;
        }
    }

    public void beingHit(int value){
        CurrentHealth = Math.max(Math.min((int) (CurrentHealth-value),MaxHealth),0);
        if (CurrentHealth == 0){
            if (enemyState != BASHER_FALL) {
                changeState(BASHER_FALL);
            }
        } else if ((enemyState != BASHER_ATTACKONE) && (enemyState != BASHER_ATTACKTWO) && (enemyState != BASHER_PREPARE)){
            changeState(BASHER_HIT);
            AnimateSpeed = 120/getSpriteAmount(enemyType,enemyState);
        }
    }

    public void drawAttackBox(Graphics g,int XlvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - XlvlOffset,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }

    protected void updateMovement(int[][] lvlData, Player player){
        Xtemp = 0;
        switch (enemyState){
            case BASHER_WALK:
                if (inRange(player)) {
                    turnToPlayer(player);
                    if (facing) {
                        Xtemp += 1;
                    } else {
                        Xtemp -= 1;
                    }
                    if (inAttackRange(player)){
                        changeState(BASHER_PREPARE);
                    }
                } else {
                    if (facing) {
                        Xtemp += 1;
                        this.count += 1;
                    } else {
                        Xtemp -= 1;
                        this.count -= 1;
                    }
                    if (this.count >= ObserveRangeMax) {
                        facing = false;
                    } else if (this.count <= ObserveRangeMin) {
                        facing = true;
                    }
                }
                break;
            case BASHER_ATTACKONE, BASHER_ATTACKTWO:
                checkEnemyHit(attackBox,player);
                break;
            case BASHER_PREPARE:
                AnimateSpeed = 20;
                break;
            case BASHER_HIT:
                break;

        }
        Moveable(Xtemp,lvlData);
    }
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        CurrentHealth = MaxHealth;
        changeState(BASHER_WALK);
        active = true;
    }
}
