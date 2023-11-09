package Entities;

import Audio.AudioPlayer;
import GameState.Playing;
import main.Gameclass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import static UtillMethod.Constant.EnemyConstant.*;
import static UtillMethod.Constant.EnvironmentConstant.*;
import static Object.ObjectManager.*;

public class BossDemon extends Enemy{
    private int Xtemp,state = 1,randomAtk,randomATKNumber,hitWidth,hitY,hitX,hitHeight,damage;
    private boolean firstTime = false,firstMeet=false,firstTransform = false,interval = false,transforming=false;
    private Rectangle2D.Float attackBox;
    private Timer attackInterval = new Timer(700, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            interval=false;
            attackInterval.stop();
        }
    });
    public BossDemon(float X, float Y) {
        super(X, Y, BOSS_DEMON_WIDTH, BOSS_DEMON_HEIGHT, BOSS_DEMON);
        initHitbox(X + BOSS_DEMON_DRAWXOFFSET,Y + BOSS_DEMON_DRAWYOFFSET,(int)(0.2 * BOSS_DEMON_WIDTH),(int)(0.6*BOSS_DEMON_HEIGHT));
        initAttackBox();
        enemyState = BOSS_DEMON_IDLE;
        attackRange = (float) (4 * Gameclass.TILE_SIZE);
    }
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(hitbox.x- (40* Gameclass.SCALE),hitbox.y - 10*Gameclass.SCALE,hitbox.width +(80*Gameclass.SCALE),hitbox.height + 10*Gameclass.SCALE);
    }

    public boolean isFirstMeet() {
        return firstMeet;
    }

    @Override
    protected void checkEnemyHit(Rectangle2D.Float ATKbox, Player player) {
        if (ATKbox.intersects(player.getHitbox()) && player.getCounterInteract()){
            if (state == 1) {
                changeState(BOSS_DEMON_HIT);
                interval = false;
                AnimateSpeed = 240/getSpriteAmount(enemyType,enemyState);
                player.Countered();
            } else if (state==2 && !attackCheck){
                firstTime = false;
                attackCheck = true;
                player.Countered();
            }
        } else if (ATKbox.intersects(player.getHitbox()) && !player.getBlockStatus() && !player.getRollStatus() && !attackCheck){
            player.changeHealth(damage);
            attackCheck = true;
        } else if (ATKbox.intersects(player.getHitbox()) && player.getBlockStatus() && !attackCheck) {
            player.Blocked();
            attackCheck = true;
        }
    }
    @Override
    protected boolean inRange(Player player){
        int range = (int)(Math.abs((hitbox.x + hitbox.width/2) - player.hitbox.x));
        return range <= attackRange * 100;
    }
    @Override
    public void beingHit(int value){
        CurrentHealth = Math.max(Math.min((int)(CurrentHealth-value),MaxHealth),0);
        if (CurrentHealth == 0) {
            if (!transforming && state==1) {
                changeState(BOSS_DEMON_FROMPHASEONE);
                transforming = true;
            } else if (enemyState != BOSS_DEMON_PHASETWO_FALL && state==2) {
                changeState(BOSS_DEMON_PHASETWO_FALL);
            }
        }
    }

    @Override
    protected void updateMovement(int[][] lvlData, Player player) {
        Xtemp = 0;
        switch (enemyState){
            case BOSS_DEMON_IDLE:
                if (super.inRange(player) && !firstMeet){
                    firstMeet = true;
                } else if (this.inRange(player) && firstMeet) {
                    turnToPlayer(player);
                    if (interval){
                        return;
                    }
                    if (facing) {
                        Xtemp += 1;
                    } else {
                        Xtemp -= 1;
                    }
                    if (!firstTime) {
                        randomATKNumber = (int) Math.floor(Math.random() * (3 - (1) + 1) + 1);
                        firstTime = true;
                    }
                    switch (randomATKNumber) {
                        case 1 -> {
                            if (inAttackRange(player)) {
                                changeState(BOSS_DEMON_PREPARE);
                            }
                        }
                        case 2 -> changeState(BOSS_DEMON_CHARGE_PREPARE);
                        case 3 -> changeState(BOSS_DEMON_RANGE_ATTACK);
                    }
                    if (inAttackRange(player)){
                        changeState(BOSS_DEMON_PREPARE);
                    }
                } else {
                    firstMeet = false;
                    firstTime = false;
                }
                break;
            case BOSS_DEMON_ATTACKONE:
                hitY = (int) (hitbox.y - 10*Gameclass.SCALE);
                hitHeight = (int)(30*Gameclass.SCALE + hitbox.height);
                hitWidth = (int)(90*Gameclass.SCALE + hitbox.width);
                damage = 3;
                checkEnemyHit(attackBox,player);
                break;
            case BOSS_DEMON_ATTACKTWO:
                hitY = (int) (hitbox.y - 10*Gameclass.SCALE);
                hitHeight = (int)(30*Gameclass.SCALE + hitbox.height);
                hitWidth = (int)(110*Gameclass.SCALE + hitbox.width);
                damage = 2;
                checkEnemyHit(attackBox,player);
                break;
            case BOSS_DEMON_ATTACKTHREE:
                hitY = (int) (hitbox.y - 30*Gameclass.SCALE);
                hitHeight = (int)(50*Gameclass.SCALE + hitbox.height);
                hitWidth = (int)(110*Gameclass.SCALE + hitbox.width);
                damage = 3;
                checkEnemyHit(attackBox,player);
                break;
            case BOSS_DEMON_CHARGE:
                hitY = (int) (hitbox.y);
                hitHeight = (int) hitbox.height;
                hitWidth = (int)hitbox.width;
                damage = 4;
                checkEnemyHit(attackBox,player);
                if (facing){
                    hitbox.x += 5;
                } else {
                    hitbox.x -= 5;
                }
                break;
            case BOSS_DEMON_PREPARE,BOSS_DEMON_CHARGE_PREPARE,BOSS_DEMON_RANGE_ATTACK:
                AnimateSpeed = 20;
                break;
            case BOSS_DEMON_PHASETWO_IDLE:
                if (super.inRange(player) && !firstMeet){
                    firstMeet = true;
                } else if (this.inRange(player) && firstMeet) {
                    turnToPlayer(player);
                    if (interval){
                        return;
                    }
                    if (facing) {
                        Xtemp += 1;
                    } else {
                        Xtemp -= 1;
                    }
                    if (inAttackRange(player)){
                        if (!firstTime) {
                            randomATKNumber = (int) Math.floor(Math.random() * (2 - (1) + 1) + 1);
                            firstTime = true;
                        }
                        switch (randomATKNumber) {
                            case 1 -> changeState(BOSS_DEMON_PHASETWO_ATTACKONE);
                            case 2 -> changeState(BOSS_DEMON_PHASETWO_ATTACKTWO);
                        }
                    }
                } else {
                    firstMeet = false;
                    firstTime = false;
                }
                break;
            case BOSS_DEMON_PHASETWO_ATTACKTWO:
                hitY = (int) (hitbox.y - 30*Gameclass.SCALE);
                hitHeight = (int)(50*Gameclass.SCALE + hitbox.height);
                hitWidth = (int)(30*Gameclass.SCALE + hitbox.width);
                damage = 3;
                if (AnimateIDX>=3 && AnimateIDX<=6) {
                    checkEnemyHit(attackBox, player);
                }
                break;
        }
        Moveable(Xtemp,lvlData);
    }
    @Override
    public void updateEnemy(int[][] lvlData,Player player){
        updateAnimationTick();
        updateMovement(lvlData,player);
        updateAttackBox();
    }

    public void updateAttackBox(){
        attackBox.width = hitWidth;
        attackBox.height = hitHeight;
        attackBox.y = hitY;
        if (state == 2){
            if (facing) {
                attackBox.x = hitbox.x + hitbox.width;
            } else {
                attackBox.x = hitbox.x - hitWidth;
            }
        } else {
            if (facing) {
                attackBox.x = hitbox.x - 50 * Gameclass.SCALE;
            } else {
                attackBox.x = hitbox.x - hitWidth + hitbox.width + 50 * Gameclass.SCALE;
            }
        }
    }
    protected void updateAnimationTick(){
        AnimateTick++;
        if (AnimateTick>=AnimateSpeed){
            AnimateTick=0;
            AnimateIDX++;
            if (enemyState == BOSS_DEMON_PHASETWO_ATTACKONE){
                if (AnimateIDX>=9){
                    AnimateSpeed = 120;
                    if (facing) {
                        callProjectile((int)( hitbox.x + hitbox.width), (int) hitbox.y, true, REDBALL);
                    } else {
                        callProjectile((int) hitbox.x, (int) hitbox.y, false, REDBALL);
                    }
                    AudioPlayer.playEffect(AudioPlayer.ENEMYRANGEATK2);
                }
            }
            if (AnimateIDX>= getSpriteAmount(enemyType,enemyState)-1) {
                AnimateIDX = 0;
                switch (enemyState){
                    case BOSS_DEMON_CHARGE_PREPARE -> {
                        enemyState = BOSS_DEMON_CHARGE;
                        AudioPlayer.playEffect(AudioPlayer.ENEMYCHARGE);
                        attackCheck = false;
                        AnimateSpeed = 15;
                    }
                    case BOSS_DEMON_PREPARE -> {
                        AnimateSpeed = 15;
                        randomAtk = (int)Math.floor(Math.random() * (3 - (1) + 1) + 1);
                        enemyState = BOSS_DEMON_ATTACKONE;
                        AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        attackCheck = false;
                    }
                    case BOSS_DEMON_ATTACKONE -> {
                        if (randomAtk>=2) {
                            enemyState = BOSS_DEMON_ATTACKTWO;
                            AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        } else {
                            enemyState = BOSS_DEMON_IDLE;
                            attackInterval.start();
                            interval = true;
                            firstTime = false;
                        }
                        attackCheck = false;
                    }
                    case BOSS_DEMON_ATTACKTWO -> {
                        if (randomAtk>=3) {
                            enemyState = BOSS_DEMON_ATTACKTHREE;
                            AudioPlayer.playEffect(AudioPlayer.ENEMYATK1);
                        } else {
                            enemyState = BOSS_DEMON_IDLE;
                            attackInterval.start();
                            interval = true;
                            firstTime = false;
                        }
                        attackCheck = false;
                    }
                    case BOSS_DEMON_RANGE_ATTACK -> {
                        if (facing) {
                            callProjectile((int)( hitbox.x + hitbox.width), (int) hitbox.y, true,FIRE);
                        } else {
                            callProjectile((int) hitbox.x, (int) hitbox.y, false,FIRE);
                        }
                        AudioPlayer.playEffect(AudioPlayer.ENEMYRANGEATK1);
                        enemyState = BOSS_DEMON_IDLE;
                        attackInterval.start();
                        interval = true;
                        firstTime = false;
                    }
                    case BOSS_DEMON_CHARGE, BOSS_DEMON_ATTACKTHREE,BOSS_DEMON_HIT -> {
                        enemyState = BOSS_DEMON_IDLE;
                        attackCheck = false;
                        attackInterval.start();
                        interval = true;
                        firstTime = false;
                        AnimateSpeed = 15;
                    }
                    case BOSS_DEMON_FROMPHASEONE -> {
                        enemyState = BOSS_DEMON_CHANGING;
                    }
                    case BOSS_DEMON_CHANGING -> {
                        enemyState = BOSS_DEMON_TOPHASETWO;
                        state=2;
                        CurrentHealth = MaxHealth;
                        attackRange = (float)(7 * Gameclass.TILE_SIZE);
                    }
                    case BOSS_DEMON_TOPHASETWO -> {
                        enemyState = BOSS_DEMON_PHASETWO_IDLE;
                        transforming = false;
                        firstTime = false;
                    }
                    case BOSS_DEMON_PHASETWO_IDLE -> {
                        firstTime = false;
                    }
                    case BOSS_DEMON_PHASETWO_ATTACKONE,BOSS_DEMON_PHASETWO_ATTACKTWO -> {
                        firstTime = false;
                        attackCheck = false;
                        AnimateSpeed = 15;
                        attackInterval.start();
                        interval = true;
                        enemyState = BOSS_DEMON_PHASETWO_IDLE;
                    }
                    case BOSS_DEMON_PHASETWO_FALL -> {
                        active = false;
                    }
                }
            }
        }
    }

    public boolean getState() {
        return state == 2;
    }

    public boolean isFirstTransform() {
        return firstTransform;
    }

    public void setFirstTransform() {
        this.firstTransform = true;
    }

    public void drawAttackBox(Graphics g, int XlvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - XlvlOffset,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }

    @Override
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        state=1;
        CurrentHealth = MaxHealth;
        changeState(BOSS_DEMON_IDLE);
        active = true;
    }
}
