package Entities;

import UtillMethod.LoadSave;
import main.Gameclass;

import java.awt.*;
import java.awt.image.BufferedImage;

import static UtillMethod.Constant.EnvironmentConstant.*;

public class Effect extends Entities {
    private int AnimateTick,AnimateIDX,AnimateAction,AnimateSpeed;
    private boolean EffectActived = false;
    private BufferedImage[][] EffectImg;

    public Effect(float X, float Y) {
        super(X, Y, 256, 256);
        EffectImg = LoadSave.getEffect(LoadSave.InGameEffect,256);
    }

    public void setEffectActived(boolean effectActived) {
        EffectActived = effectActived;
    }

    public boolean isEffectActived() {
        return EffectActived;
    }

    public void newEffect(int action,int Xpostion,int Yposition,boolean facing){
        this.AnimateAction = action;
        switch (this.AnimateAction){
            case SKILL_BUFF -> {
                if (facing) {
                    x = Xpostion + 20 +(int)(125*1.9);
                    width = (int) (-256*1.9);
                } else {
                    x = Xpostion + 20 - (int)(125*1.9);
                    width = (int) (256*1.9);
                }
                height = 350;
                y = Yposition - 130;
                AnimateSpeed = 9;
            }
            case COUNTER -> {
                if (facing) {
                    x = Xpostion + (int) (400 * 0.75 * 0.5) + 60 * Gameclass.SCALE;
                    width = (int) (-400 * 0.75);
                } else {
                    x = Xpostion + 60 * Gameclass.SCALE;
                    width = (int) (400 * 0.75);
                }
                y = Yposition - 105 * Gameclass.SCALE;
                height = (int) (700 * 0.75);
                AnimateSpeed = 5;
                AnimateTick=0;
                AnimateIDX=0;
            }
            case DASH -> {
                if (facing){
                    x=Xpostion + (int)(256*0.5) + 80 * Gameclass.SCALE;
                    width = -256;
                } else {
                    x=Xpostion + 50 * Gameclass.SCALE;
                    width = 256;
                }
                y=Yposition - 0 * Gameclass.SCALE;
                height = 256;
                AnimateSpeed = 50;
            }
            case BLOCK -> {
                if (facing){
                    x=Xpostion + (int)(256*0.5) + (int)Math.floor(Math.random() * (75 - (55) + 1) + 55) * Gameclass.SCALE;
                    width = -256;
                } else {
                    x=Xpostion + (int)Math.floor(Math.random() * (75 - (55) + 1) + 55) * Gameclass.SCALE;
                    width = 256;
                }
                y=Yposition - (int)Math.floor(Math.random() * (65 - (35) + 1) + 35) * Gameclass.SCALE;
                height = 256;
                AnimateSpeed = 30;
            }
        }
        this.EffectActived = true;
    }
    private void updateAnimationTick(){
        AnimateTick++;
        if (AnimateTick>=AnimateSpeed){
            AnimateTick=0;
            AnimateIDX++;
            if (AnimateIDX>= getEffectSpriteAmount(AnimateAction)) {
                AnimateIDX = 0;
                if (AnimateAction!=SKILL_BUFF) {
                    EffectActived = false;
                }
            }
        }
    }
    public void updateEffect(){
        updateAnimationTick();
    }

    public void drawEffect(Graphics g,int xlvlOffset){
        if (EffectActived) {
            g.drawImage(EffectImg[AnimateAction][AnimateIDX], (int) x - xlvlOffset, (int) y, width, height, null);
        }
    }

    public void setPosition(int X,int Y,boolean facing){
        if (facing) {
            x = X + 20 +(int)(125*1.9);
            width = (int) (-256*1.9);
        } else {
            x = X + 20 - (int)(125*1.9);
            width = (int) (256*1.9);
        }
        y = Y - 130;
    }
}
