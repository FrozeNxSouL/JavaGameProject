package Entities;

import Audio.AudioPlayer;
import main.Gameclass;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static UtillMethod.Constant.EnemyConstant.*;

public class SkeletonWarrior extends Enemy{
    private int Xtemp;
    private Rectangle2D.Float attackBox;
    public SkeletonWarrior(float X, float Y) {
        super(X, Y, WARRIOR_WIDTH, WARRIOR_HEIGHT, WARRIOR);
        initHitbox(X + WARRIOR_DRAWXOFFSET ,Y + WARRIOR_DRAWYOFFSET,(int)(0.15* WARRIOR_WIDTH),(int)(0.28* WARRIOR_HEIGHT));
        initAttackBox();
        enemyState = WARRIOR_WALK;
        attackRange = (float) (3 * Gameclass.TILE_SIZE);
        ObserveRangeMax=(int)Math.floor(Math.random() * (100 - (-100) + 1) - 100);
        ObserveRangeMin=(int)Math.floor(Math.random() * ((-800) - (-500) + 1) - 500);
        hitbox.x+=(int)Math.floor(Math.random() * (this.ObserveRangeMax - (this.ObserveRangeMin) + 1) + this.ObserveRangeMin);
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(hitbox.x- (40*Gameclass.SCALE),hitbox.y - 10*Gameclass.SCALE,hitbox.width +(90*Gameclass.SCALE),hitbox.height + 10*Gameclass.SCALE);
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
            attackBox.x = hitbox.x- (30*Gameclass.SCALE);
        } else {
            attackBox.x = hitbox.x- (60*Gameclass.SCALE);
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
                    case WARRIOR_PREPARE -> {
                        AnimateSpeed = 15;
                        enemyState = WARRIOR_ATTACK;
                        AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        attackCheck = false;
                    }
                    case WARRIOR_ATTACK, WARRIOR_HIT -> {
                        enemyState = WARRIOR_WALK;
                        AnimateSpeed = 15;
                    }
                    case WARRIOR_FALL -> active = false;
                }
            }
        }
    }
    protected void checkEnemyHit(Rectangle2D.Float ATKbox, Player player) {
        if (ATKbox.intersects(player.getHitbox()) && player.getCounterInteract()){
            changeState(WARRIOR_HIT);
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
            if (enemyState != WARRIOR_FALL) {
                changeState(WARRIOR_FALL);
            }
        } else if (enemyState != WARRIOR_ATTACK && (enemyState != WARRIOR_PREPARE)){
            changeState(WARRIOR_HIT);
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
            case WARRIOR_WALK:
                if (inRange(player)) {
                    turnToPlayer(player);
                    if (facing) {
                        Xtemp += 1;
                    } else {
                        Xtemp -= 1;
                    }
                    if (inAttackRange(player)){
                        changeState(WARRIOR_PREPARE);
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
            case WARRIOR_ATTACK:
                checkEnemyHit(attackBox,player);
                break;
            case WARRIOR_PREPARE:
                AnimateSpeed = 20;
                break;
            case WARRIOR_HIT:
                break;

        }
        Moveable(Xtemp,lvlData);
    }
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        CurrentHealth = MaxHealth;
        changeState(WARRIOR_WALK);
        active = true;
    }
}
