package Entities;

import Audio.AudioPlayer;
import GameState.Playing;
import static UtillMethod.Constant.EnvironmentConstant.*;

import UI.Setting;
import UtillMethod.LoadSave;
import main.Gameclass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static UtillMethod.Constant.PlayerConstant.*;
import static UtillMethod.Constant.PlayerConstant.SpriteAmount;
import static UtillMethod.HelpMethod.CanMoveHere;

public class Player extends Entities implements Runnable{
    private int Pwidth = 256,Pheight = 256,damage;
    private Playing playing;
    private Effect effect,skillFX;
    private Rectangle2D.Float attackBox;
    private BufferedImage[][] animations;
    private BufferedImage statusBar,img,skillIcon;
    private BufferedImage bossName;
    private BufferedImage[] healthBar = new BufferedImage[10];
    private BufferedImage[] energyBar = new BufferedImage[10];
    private BufferedImage[] skillBar = new BufferedImage[10];
    private BufferedImage[] weaponIcon = new BufferedImage[5];
    private BufferedImage[] bossHealthBar = new BufferedImage[50];

    private int bossHealthBarWidth = (int) (Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE*0.8);
    private int bossHealthBarHeight =(int) (60 * Gameclass.SCALE);
    private int bossHealthBarX = (int)((Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE*0.5)-bossHealthBarWidth*0.5);
    private int bossHealthBarY = (int) (325 * Gameclass.SCALE);

    private int bossNameWidth = (int) (Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE*0.3);
    private int bossNameHeight =(int) (bossNameWidth*0.35);
    private int bossNameX = (int)((Gameclass.TILE_IN_WIDTH * Gameclass.TILE_SIZE*0.5)-bossNameWidth*0.5);
    private int bossNameY = (int) (300 * Gameclass.SCALE);

    private int statusBarWidth = (int) (60 * Gameclass.SCALE);
    private int statusBarHeight = (int) (60 * Gameclass.SCALE);
    private int statusBarX = (int) (10 * Gameclass.SCALE);
    private int statusBarY = (int) (10 * Gameclass.SCALE);

    private int skillBarWidth = (int) (55 * Gameclass.SCALE);
    private int skillBarHeight = (int) (55 * Gameclass.SCALE);
    private int skillBarX = (int) (15 * Gameclass.SCALE);
    private int skillBarY = (int) (16 * Gameclass.SCALE);

    private int skillIconX=(int)(35*Gameclass.SCALE);
    private int skillIconY=(int)(34*Gameclass.SCALE);
    private int skillIconSize=(int)(17*Gameclass.SCALE);

    private int healthBarWidth = (int) (250 * Gameclass.SCALE);
    private int healthBarHeight = (int) (30 * Gameclass.SCALE);
    private int healthBarXStart = (int) (40 * Gameclass.SCALE);
    private int healthBarYStart = (int) (23 * Gameclass.SCALE);

    private int EnergyBarWidth = (int) (200 * Gameclass.SCALE);
    private int EnergyBarHeight = (int) (25 * Gameclass.SCALE);
    private int EnergyBarXStart = (int) (35 * Gameclass.SCALE);
    private int EnergyBarYStart = (int) (36 * Gameclass.SCALE);
    private Thread energyRegen;

    private int maxEnergy = 10;
    private int currentEnergy = maxEnergy;
    private int prevCurrentEnergy = maxEnergy;
    private int energyWidth = EnergyBarWidth;
    private boolean energyUpdated = true;

    private int maxSkill = 10;
    private int currentSkill;

    private int maxHealth = 10;
    private int currentHealth = maxHealth;
    private int prevCurrentHealth = maxHealth;
    private int healthWidth = healthBarWidth;
    private boolean healthUpdated = true;

    private int animationTick,animationIDX,animationSpeed = 12;
    private boolean attackCheck = false, combatFacing;
    private int action = IDLE, ATKCount =1,animationDir=0, tempFacing = 0, HoldAnimate =0, LastAttackAnimate =0,counterInteract=0;
    private boolean facing,moveRight = false,moveLeft = false,Sprint = false, CombatStatus =false,blockStatus=false,interact=false,afterATK=false,skillActivating = false,falling=false;
    private boolean attacking = false,attackStatus = false,blocking = false,Rolling=false,RollStatus=false,TwoDirStatus=false,wasCounter = false,skillCall = false,invincible = false,reviving = false;
    private int[][] lvlData,lvlFloorCollision;
    private long LastCounterTime, CurrentCounterTime,counterTime = 150;
    private float xOffset=80 * Gameclass.SCALE,yOffset=107 * Gameclass.SCALE,hitWidth,hitHeight,hitY;
    private Timer animationTimer = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            HoldAnimate++;
        }
    });
    private Timer skillTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentHealth = Math.min(currentHealth+1,maxHealth);
            currentEnergy = Math.min(currentEnergy+1,maxEnergy);
            currentSkill = Math.max(currentSkill-2,0);
            if (currentSkill == 0 || falling || currentHealth == 0){
                skillTimer.stop();
                skillFX.setEffectActived(false);
                skillActivating = false;
            }
        }
    });


    public Player(float X,float Y,int W,int H,Playing play){
        super(X,Y,W,H);
        this.playing = play;
        effect = new Effect(x,y);
        skillFX = new Effect(x,y);
        loadanimation();
        initHitbox(x+xOffset,y+yOffset,40,170);
        initAttackBox();
        this.facing = false;
        energyRegen = new Thread(this);
        energyRegen.start();
        this.currentSkill = 0;
    }
    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(hitbox.x,hitbox.y,150,250);
    }

    private void loadanimation() {
        animations = new BufferedImage[13][12];
        img = LoadSave.LoadSprite(LoadSave.PlayerSprite);
        for (int i=0;i< animations.length;i++) {
            for (int j = 0; j < animations[i].length; j++) {
                if (img.getSubimage(j * 512, i * 512, 512, 512) != null) {
                    animations[i][j] = img.getSubimage(j * 512, i * 512, 512, 512);
                }
            }
        }
        img = LoadSave.LoadSprite(LoadSave.GUISprite);
        statusBar = img.getSubimage(0, 0, 64, 64);
        skillIcon = img.getSubimage(64*2, 64*3, 64, 64);
        bossName = img.getSubimage(0, 64*6, 64*4, 64);
        for (int i=0;i<maxHealth;i++) {
            healthBar[i] = img.getSubimage(64 + 19*i , 64, 19, 32);
        }
        for (int i=0;i<maxEnergy;i++) {
            energyBar[i] = img.getSubimage(64 + (144/10)*i , 96, 144/10, 32);
        }
        for (int i=0;i<maxSkill;i++) {
            skillBar[i] = img.getSubimage(5 , (int) (69 + (54.0/10)*i), 59, (int) (54.0/10));
        }
        for (int i=0;i<4;i++) {
            weaponIcon[i] = img.getSubimage(64*i , 64*5, 64, 64);
        }
        for (int i=0;i<50;i++) {
            bossHealthBar[i] = img.getSubimage(i*(int)(Math.ceil((64.0 * 8)/50)), 64*4 , (int)Math.ceil((64.0 * 8)/50), 64);
        }
    }
    public void setSkillActivate(){
        this.skillCall = currentSkill == maxSkill;
        if (facing){
            animationDir = 1;
        } else {
            animationDir = 2;
        }
    }

    public boolean getFacing(){
        return this.facing;
    }

    public void setFacing(boolean facing) {
        this.facing = facing;
    }

    public void setmoveLeft(boolean ml){
        this.moveLeft = ml;
        if (!this.RollStatus && !attackStatus && !TwoDirStatus && currentHealth > 0){
            if (this.CombatStatus && !Sprint){
                this.facing = combatFacing;
            } else {
                this.facing = true;
            }
        }
        else if (!ml && TwoDirStatus) {
            TwoDirStatus = false;
            this.tempFacing = 2;
            this.facing = false;
        }
        else {
            this.tempFacing = 1;
        }
    }
    public void setmoveRight(boolean mr){
        this.moveRight = mr;
        if (!this.RollStatus && !attackStatus && !TwoDirStatus && currentHealth > 0){
            if (this.CombatStatus && !Sprint){
                this.facing = combatFacing;
            } else {
                this.facing = false;
            }
        }
        else if (!mr && TwoDirStatus) {
            TwoDirStatus = false;
            this.tempFacing = 1;
            this.facing = true;
        }
        else {
            this.tempFacing = 2;
        }
    }

    public void setInteract(boolean interact) {
        this.interact = interact;
    }

    public void setCombatstatus(boolean combatstatus){
        this.CombatStatus = combatstatus;
    }

    public boolean isCombatstatus() {
        return CombatStatus;
    }

    public void setSprint(boolean sprint){
        this.Sprint = currentEnergy >= 2 && sprint;
    }

    public void setRolling(boolean roll){
        this.Rolling = currentEnergy >= 3 && roll;
        if (this.Rolling && !effect.isEffectActived()){
            this.effect.newEffect(DASH,(int)(hitbox.x - xOffset),(int)(hitbox.y),this.facing);
            AudioPlayer.playEffect(AudioPlayer.DASH);
        }
        if (attackStatus){
            attacking = false;
            attackStatus = false;
            animationDir = 0;
        }
        if (moveLeft && this.Rolling) {
            animationDir = 1;
        } else if (moveRight && this.Rolling){
            animationDir = 2;
        } else if (this.Rolling){
            if (facing){
                animationDir = 1;
            } else {
                animationDir = 2;
            }
        }
    }

    public boolean isSkillActivating() {
        return skillActivating;
    }

    public boolean getRollStatus() {
        return RollStatus;
    }

    public void setBlock(boolean block){
        if (!attacking && !Rolling && block) {
            this.blocking = block;
            this.blockStatus = block;
            this.counterTime = Setting.getCounterChoice();
            if (counterInteract == 0) {
                LastCounterTime = System.currentTimeMillis();
                counterInteract = 1;
            }
        }
        else if (!block) {
            this.blocking = block;
            this.blockStatus = block;
            counterInteract = 0;
            wasCounter = false;
        }
    }

    public void increaseSkill(){
        if (!skillActivating) {
            this.currentSkill = Math.min(this.currentSkill + 1, maxSkill);
        }
    }

    public void Countered() {
        if (currentHealth > 0) {
            this.effect.newEffect(COUNTER, (int) (hitbox.x - xOffset), (int) (hitbox.y), this.facing);
            this.currentEnergy = Math.min(currentEnergy + 1, maxEnergy);
            this.wasCounter = true;
            AudioPlayer.playEffect(AudioPlayer.COUNTER);
            increaseSkill();
        }
    }
    public void Blocked() {
        if (currentHealth > 0) {
            this.effect.newEffect(BLOCK, (int) (hitbox.x - xOffset), (int) (hitbox.y), this.facing);
            AudioPlayer.playEffect((int)Math.floor(Math.random() * (AudioPlayer.BLOCK2 - AudioPlayer.BLOCK1 + 1) + AudioPlayer.BLOCK1));
        }
    }

    public boolean getCounterInteract(){
        return counterInteract == 1;
    }

    public boolean getBlockStatus() {
        return blockStatus;
    }

    public void setAttacking(boolean atk){
        this.attacking = currentEnergy >= 2 && atk;
    }
    public void LoadlvlData(int[][] LvD,int[][] LvDCol){
        this.lvlData = LvD;
        this.lvlFloorCollision = LvDCol;
    }
    public void resetPosition(){
        moveRight = false;
        moveLeft = false;
    }
    public void setAnimations() {
        int startAni = action,Xtemp=0;
        if (currentHealth==0 || reviving){
            action = SWORDFALL;
            if (facing){
                animationDir = 1;
            } else {
                animationDir = 2;
            }
            falling = true;
            animationSpeed = 20;
        } else if (skillCall) {
            action = SWORDSKILL;
            invincible = true;
        } else if (interact) {
            if (!CanMoveHere(hitbox.x-20, hitbox.y, 100, hitbox.height, lvlData)) {
                if ((int) (hitbox.x) < ((lvlData[0].length / 2) * Gameclass.TILE_SIZE)) {
                    playing.callMapSwitcher(false,false);
                } else {
                    playing.callMapSwitcher(true,true);
                }
            }
            this.interact = false;
        } else if (Rolling){
            if (!TwoDirStatus) {
                action = SWORDROLLING;
                if (animationDir == 1) {
                    Xtemp -= 3;
                } else if (animationDir == 2){
                    Xtemp += 3;
                } else {
                    if (facing) {
                        Xtemp -= 3;
                    } else {
                        Xtemp += 3;
                    }
                }
                this.RollStatus = true;
            }
        } else if (blocking && !attackStatus && !RollStatus){
            action = SWORDBLOCK;
            CurrentCounterTime = System.currentTimeMillis();
            if (CurrentCounterTime - LastCounterTime >= counterTime){
                counterInteract = 2;
            }
        } else if (attacking && currentEnergy>=1){
            if (!this.RollStatus && !TwoDirStatus) {
                if (ATKCount == 1) {
                    action = SWORDONE;
                    hitWidth = 180;
                    hitHeight = 210;
                    hitY = hitbox.y - 50;
                    damage = 4;
                } else if (ATKCount == 2) {
                    action = SWORDTWO;
                    hitWidth = 200;
                    hitHeight = 400;
                    hitY = hitbox.y-200;
                    damage = 5;
                } else if (ATKCount == 3) {
                    action = SWORDTHREE;
                    hitWidth = 310;
                    hitHeight = 150;
                    hitY = hitbox.y-30;
                    damage = 4;
                } else if (ATKCount == 4) {
                    action = SWORDFOUR;
                    hitWidth = 250;
                    hitHeight = 250;
                    hitY = hitbox.y - 100;
                    damage = 8;
                }
                if (facing) {
                    animationDir = 1;
                    if (action == SWORDTHREE){
                        Xtemp-=1;
                    }
                } else {
                    animationDir = 2;
                    if (action == SWORDTHREE){
                        Xtemp+=1;
                    }
                }
                this.attackStatus = true;
            }
        } else if (moveLeft && moveRight && CombatStatus) {
            if (!this.RollStatus && !attackStatus) {
                action = SWORDIDLE;
                TwoDirStatus = true;
            }
        } else if (moveLeft && moveRight) {
            if (!this.RollStatus && !attackStatus) {
                action = IDLE;
                TwoDirStatus = true;
            }
        } else if (moveLeft && Sprint && currentEnergy>=2) {
            if (!this.RollStatus && !attackStatus){
                action = RUNNING;
                this.facing = true;
//                animationSpeed = 8;
                Xtemp-=2;
            }
        } else if (moveRight && Sprint && currentEnergy>=2) {
            if (!this.RollStatus && !attackStatus){
                action = RUNNING;
                this.facing = false;
//                animationSpeed = 8;
                Xtemp+=2;
            }
        } else if (moveRight && CombatStatus){
            if (!this.RollStatus && !attackStatus) {
                facing  = combatFacing;
                action = SWORDSTEPPING;
                if (animationIDX > 2 && animationIDX < 6){
                    Xtemp += 2;
                }
            }
        } else if (moveLeft && CombatStatus){
            if (!this.RollStatus && !attackStatus) {
                facing  = combatFacing;
                action = SWORDSTEPPING;
                if (animationIDX > 2 && animationIDX < 6){
                    Xtemp -= 2;
                }
            }
        } else if (CombatStatus){
            if (HoldAnimate >=1){
                animationTimer.stop();
                afterATK = false;
                HoldAnimate =0;
                action = SWORDIDLE;
                facing  = combatFacing;
            } else if (!afterATK) {
                action = SWORDIDLE;
                facing  = combatFacing;
            }
        } else if (moveRight){
            if (!this.RollStatus && !attackStatus) {
                action = WALK;
                facing = false;
                Xtemp += 1;
            }
        } else if (moveLeft){
            if (!this.RollStatus && !attackStatus) {
                action = WALK;
                facing = true;
                Xtemp -= 1;
            }
        }
        else {
            if (HoldAnimate >=1){
                animationTimer.stop();
                afterATK = false;
                HoldAnimate =0;
                action = IDLE;
            } else if (!afterATK) {
                action = IDLE;
            }
        }
        if (CanMoveHere(hitbox.x+Xtemp, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (CanMoveHere(hitbox.x + Xtemp, hitbox.y, hitbox.width, hitbox.height, lvlFloorCollision)) {
                hitbox.x += Xtemp;
                for (int i = 32; i >= 4; i -= 4) {
                    if (CanMoveHere(hitbox.x, hitbox.y + i * 2, hitbox.width, hitbox.height, lvlFloorCollision)) {
                        hitbox.y += i;
//                    System.out.println("down  "+i);
                        break;
                    }
                }
            } else {
                for (int i = 4; i <= 32; i += 4) {
                    if (CanMoveHere(hitbox.x + Xtemp, hitbox.y - i * 2, hitbox.width, hitbox.height, lvlFloorCollision)) {
                        hitbox.x += Xtemp;
                        hitbox.y -= i;
//                    System.out.println("up  "+i);
                        break;
                    }
                }
            }
        }

        if (startAni!=action){
            afterATK = false;
            animationTimer.stop();
            HoldAnimate =0;
            resetTick();
            TwoDirStatus = false;
            if (this.attackStatus && !Rolling){
                if (ATKCount == 1) {
                    AudioPlayer.playEffect(AudioPlayer.ATK1);
                } else if (ATKCount == 2) {
                    AudioPlayer.playEffect(AudioPlayer.ATK2);
                } else if (ATKCount == 3) {
                    AudioPlayer.playEffect(AudioPlayer.ATK3);
                } else if (ATKCount == 4) {
                    AudioPlayer.playEffect(AudioPlayer.ATK4);
                }
            }
        }
//        animationTimer.stop();
    }
    private void resetTick() {
        animationTick=0;
        animationIDX=0;
    }

    private void updateanimationTick() {
        animationTick++;
        if (animationTick>=animationSpeed){
            animationTick=0;
            if (reviving && animationIDX>0){
                animationIDX--;
                currentHealth = Math.min(currentHealth+1,maxHealth);
                return;
            } else if (action == SWORDFALL && reviving) {
                reviving = false;
                animationSpeed = 12;
                invincible = false;
                falling = false;
                this.tempFacing = 0;
                this.animationDir = 0;
                if (CombatStatus){
                    action = SWORDIDLE;
                    facing = combatFacing;
                } else {
                    action = IDLE;
                }
            }
            animationIDX++;
            if (!attackStatus && !RollStatus && currentHealth > 0){
                if (this.tempFacing == 1 && !TwoDirStatus) {
                    this.facing = true;
                } else if (this.tempFacing == 2 && !TwoDirStatus) {
                    this.facing = false;
                }
                this.tempFacing = 0;
                if (action == WALK) {
                    if (animationIDX%4 == 0){
                        AudioPlayer.playEffect(AudioPlayer.FOOTSTEP1);
                    }
                } else if (action == RUNNING) {
                    if (animationIDX % 3 == 0) {
                        if (animationIDX == SpriteAmount(action)){
                            animationIDX = 1;
                        }
                        AudioPlayer.playEffect(AudioPlayer.FOOTSTEP1);
                    }
                } else if (action == SWORDSTEPPING) {
                    if (animationIDX == 3 || animationIDX == 5){
                        AudioPlayer.playEffect(AudioPlayer.FOOTSTEP1);
                    }
                }
            }
            if (action == SWORDFALL && animationIDX== SpriteAmount(action)-1){
                if (currentSkill == maxSkill){
                    currentSkill = 0;
                    animationIDX--;
                    reviving = true;
                    invincible = true;
                    AudioPlayer.playEffect(AudioPlayer.REVIVE);
                } else {
                    playing.setGameOver(true);
                }
                return;
            }
            if (animationIDX>= SpriteAmount(action)) {
                if (skillCall) {
                    skillTimer.start();
                    invincible = false;
                    skillActivating = true;
                    skillCall = false;
                    this.skillFX.newEffect(SKILL_BUFF,(int)(hitbox.x),(int)(hitbox.y),this.facing);
                    if (this.tempFacing == 1) {
                        this.facing = true;
                    } else if (this.tempFacing == 2) {
                        this.facing = false;
                    }
                    AudioPlayer.playEffect(AudioPlayer.BUFF);
                    this.animationDir = 0;
                } else if (action == RUNNING || action == WALK) {
                    animationIDX = 1;
                    if (action == RUNNING && CombatStatus && !blockStatus){
                        this.currentEnergy -= 2;
                    }
                }else if (this.attackStatus) {
                    if (ATKCount == 4) {
                        ATKCount = 0;
                    }
                    ATKCount++;
                    if (this.tempFacing == 1) {
                        this.facing = true;
                    } else if (this.tempFacing == 2) {
                        this.facing = false;
                    }
                    this.animationDir = 0;
                    this.attackStatus = false;
                    this.tempFacing = 0;
                    this.attacking = false;
                    this.attackCheck = false;
                    if (CombatStatus){
                        this.currentEnergy -= 1;
                    }
                    afterATK = true;
                    animationIDX--;
                    animationTimer.start();
                } else if (this.RollStatus) {
                    this.Rolling = false;
                    this.RollStatus = false;
                    animationDir = 0;
                    if (this.tempFacing == 1) {
                        this.facing = true;
                    } else if (this.tempFacing == 2) {
                        this.facing = false;
                    }
                    tempFacing = 0;
                    this.ATKCount = 1;
                    if (CombatStatus) {
                        this.currentEnergy -= 3;
                    }
                     animationIDX = 0;
                } else {
                    if (wasCounter){
                        animationIDX = 2;
                    } else if (!afterATK) {
                        animationIDX = 0;
                    } else {
                        animationIDX--;
                    }
                }
            }
        }
    }
    private void enemyInRange(ArrayList<Enemy> enemylists){
        int range;
        for (Enemy e:enemylists) {
            range =  (int)(Math.abs(hitbox.x - e.hitbox.x));
            if (e instanceof BossDemon){
                if (range <= 550*1.5 && e.getActive()) {
                    this.CombatStatus = true;
                    combatFacing = (hitbox.x - e.hitbox.x) > 0;
                    return;
                }
            } else {
                if (range <= 550 && e.getActive()) {
                    this.CombatStatus = true;
                    combatFacing = (hitbox.x - e.hitbox.x) > 0;
                    return;
                }
            }
        }
        this.CombatStatus = false;
    }

    public void changeHealth(int value){
        if (!invincible && !Setting.isInvincibleChoice()) {
            if (skillActivating){
                value = (int)Math.floor(value/2.0);
            }
            this.currentHealth = Math.max(Math.min(currentHealth - value, maxHealth), 0);
        }
    }

    public void update(EnemyManager enemyManager){
        updateAttackBox();
        enemyInRange(enemyManager.getEnemylists());
        effect.updateEffect();
        skillFX.updateEffect();
        if (this.attackStatus){
            checkAttack();
        }
        updateanimationTick();
        setAnimations();
//        updateHitbox();
    }

    private void checkAttack() {
        if (!attackCheck && animationIDX > 0){
            attackCheck = true;
            playing.checkEnemyHit(attackBox,damage,this);
        }
    }

    private void updateAttackBox() {
        attackBox.width = hitWidth;
        attackBox.height = hitHeight;
        attackBox.y = hitY;
        if (animationDir==1 && attackStatus){
            attackBox.x = hitbox.x - hitWidth + hitbox.width;
        } else if (animationDir==2 && attackStatus) {
            attackBox.x = hitbox.x;
        }
    }

    private void drawAttackBox(Graphics g,int xlvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - xlvlOffset,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }

    public void render(Graphics g, int xlvlOffset){
        if (animationDir==1) {
            if (skillActivating){
                skillFX.setPosition((int)(hitbox.x),(int)(hitbox.y),true);
            }
            g.drawImage(animations[action][animationIDX], (int)(hitbox.x + (int)(0.696331*Pwidth*Gameclass.SCALE) -xOffset) - xlvlOffset,(int)(hitbox.y - yOffset),(int)(-1*Pwidth*Gameclass.SCALE),(int)(Pheight*Gameclass.SCALE),null);
        }
        else if (animationDir==2){
            if (skillActivating){
                skillFX.setPosition((int)(hitbox.x),(int)(hitbox.y),false);
            }
            g.drawImage(animations[action][animationIDX], (int)(hitbox.x  -xOffset) - xlvlOffset,(int)(hitbox.y- yOffset),(int)(Pwidth*Gameclass.SCALE),(int)(Pheight*Gameclass.SCALE),null);
        }
        else {
            if (facing) {
                g.drawImage(animations[action][animationIDX], (int)(hitbox.x + (int)(0.696331*Pwidth*Gameclass.SCALE)  -xOffset) - xlvlOffset,(int)(hitbox.y- yOffset),(int)(-1*Pwidth*Gameclass.SCALE),(int)(Pheight*Gameclass.SCALE),null);
            }
            else {
                g.drawImage(animations[action][animationIDX], (int)(hitbox.x  -xOffset) - xlvlOffset,(int)(hitbox.y- yOffset),(int)(Pwidth*Gameclass.SCALE),(int)(Pheight*Gameclass.SCALE),null);
            }
            if (skillActivating){
                skillFX.setPosition((int)(hitbox.x),(int)(hitbox.y),facing);
            }
        }
        skillFX.drawEffect(g,xlvlOffset);
        effect.drawEffect(g,xlvlOffset);
    }

    public void drawUI(Graphics g,EnemyManager enemyManager) {
        for (int i = 0; i < currentEnergy; i++) {
            g.drawImage(energyBar[i], EnergyBarXStart + EnergyBarWidth/10 * i, EnergyBarYStart, EnergyBarWidth/10 , EnergyBarHeight, null);
        }
        for (int i = 0; i < currentHealth; i++) {
            g.drawImage(healthBar[i], healthBarXStart + healthWidth/10 * i, healthBarYStart, healthWidth/10, healthBarHeight, null);
        }
        g.drawImage(statusBar,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        for (int i = maxSkill-currentSkill; i < maxSkill; i++) {
            g.drawImage(skillBar[i], skillBarX, skillBarY + (skillBarHeight/maxSkill)*i, skillBarWidth, statusBarHeight/maxSkill, null);
        }
        if (currentSkill == maxSkill || skillActivating) {
            g.drawImage(skillIcon, skillIconX, skillIconY, skillIconSize, skillIconSize, null);
        }
        for (Enemy enemy: enemyManager.getEnemylists()) {
            if(enemy instanceof  BossDemon){
                if (((BossDemon) enemy).isFirstMeet()) {
                    g.drawImage(bossName, bossNameX , bossNameY, bossNameWidth, bossNameHeight, null);
                    for (int i = 0; i < enemy.getCurrentHealth(); i++) {
                        g.drawImage(bossHealthBar[i], bossHealthBarX + (bossHealthBarWidth / 50) * i, bossHealthBarY, bossHealthBarWidth / 50, bossHealthBarHeight, null);
                    }
                }
                break;
            }
        }
    }


    public void resetAll() {
        action = IDLE;
        resetTick();
        animationSpeed = 12;
        ATKCount = 1;
        counterTime = 150;
        animationDir=0;
        tempFacing = 0;
        HoldAnimate =0;
        LastAttackAnimate =0;
        counterInteract=0;
        currentEnergy = maxEnergy;
        currentSkill = maxSkill;
        currentHealth = maxHealth;
        attackCheck=false;
        facing = false;
        moveRight = false;
        moveLeft = false;
        Sprint = false;
        CombatStatus =false;
        blockStatus=false;
        attacking = false;
        attackStatus = false;
        blocking = false;
        Rolling=false;
        RollStatus=false;
        TwoDirStatus=false;
        interact=false;
        falling = false;

        hitbox.x = x + xOffset;
        hitbox.y = y + yOffset;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            if (!falling) {
                this.currentEnergy = Math.max(Math.min(currentEnergy + 1, maxEnergy), 0);
            }
        }
    }
}
