package Entities;

import Audio.AudioPlayer;
import main.Gameclass;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static UtillMethod.Constant.EnemyConstant.*;

public class Goblin extends Enemy{
    private int Xtemp;
    private Rectangle2D.Float attackBox;
    public Goblin(float X, float Y) {
        super(X, Y, GOBLIN_WIDTH, GOBLIN_HEIGHT, GOBLIN);
        initHitbox(X + GOBLIN_DRAWXOFFSET ,Y + GOBLIN_DRAWYOFFSET,(int)(0.2* GOBLIN_WIDTH),(int)(0.23* GOBLIN_HEIGHT));
        initAttackBox();
        enemyState = GOBLIN_WALK;
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
                    case GOBLIN_PREPARE -> {
                        AnimateSpeed = 15;
                        enemyState = GOBLIN_ATTACK;
                        AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        attackCheck = false;
                    }
                    case GOBLIN_ATTACK, GOBLIN_HIT -> {
                        enemyState = GOBLIN_WALK;
                        AnimateSpeed = 15;
                    }
                    case GOBLIN_FALL -> active = false;
                }
            }
        }
    }
    protected void checkEnemyHit(Rectangle2D.Float ATKbox, Player player) {
        if (ATKbox.intersects(player.getHitbox()) && player.getCounterInteract()){
            changeState(GOBLIN_HIT);
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
            if (enemyState != GOBLIN_FALL) {
                changeState(GOBLIN_FALL);
            }
        } else if (enemyState != GOBLIN_ATTACK && (enemyState != GOBLIN_PREPARE)) {
            changeState(GOBLIN_HIT);
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
            case GOBLIN_WALK:
                if (inRange(player)) {
                    turnToPlayer(player);
                    if (facing) {
                        Xtemp += 1;
                    } else {
                        Xtemp -= 1;
                    }
                    if (inAttackRange(player)){
                        changeState(GOBLIN_PREPARE);
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
            case GOBLIN_ATTACK:
                checkEnemyHit(attackBox,player);
                break;
            case GOBLIN_PREPARE:
                AnimateSpeed = 20;
                break;
            case GOBLIN_HIT:
                break;

        }
        Moveable(Xtemp,lvlData);
    }
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        CurrentHealth = MaxHealth;
        changeState(GOBLIN_WALK);
        active = true;
    }
}
