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
                    x = Xpostion + (int)(120*Gameclass.SCALE);
                    width = (int) (-212*Gameclass.SCALE);
                } else {
                    x = Xpostion - (int)(95*Gameclass.SCALE);
                    width = (int) (212*Gameclass.SCALE);
                }
                height = (int) (152*Gameclass.SCALE);
                y = Yposition - (int) (56*Gameclass.SCALE);
                AnimateSpeed = 9;
            }
            case COUNTER -> {
                if (facing) {
                    x = Xpostion + (int) (65*Gameclass.SCALE) + 60 * Gameclass.SCALE;
                    width = (int) (-130*Gameclass.SCALE);
                } else {
                    x = Xpostion + 60 * Gameclass.SCALE;
                    width = (int) (130*Gameclass.SCALE);
                }
                y = Yposition - 105 * Gameclass.SCALE;
                height = (int) (230*Gameclass.SCALE);
                AnimateSpeed = 5;
                AnimateTick=0;
                AnimateIDX=0;
            }
            case DASH -> {
                if (facing){
                    x=Xpostion + (int)((112*Gameclass.SCALE) * 0.5) + 80 * Gameclass.SCALE;
                    width = -(int)(112*Gameclass.SCALE);
                } else {
                    x=Xpostion + 50 * Gameclass.SCALE;
                    width = (int)(112*Gameclass.SCALE);
                }
                y=Yposition - 0 * Gameclass.SCALE;
                height = (int)(112*Gameclass.SCALE);
                AnimateSpeed = 35;
            }
            case BLOCK -> {
                if (facing){
                    x=Xpostion + (int)((112*Gameclass.SCALE)*0.5) + (int)Math.floor(Math.random() * (75 - (55) + 1) + 55) * Gameclass.SCALE;
                    width = -(int)(112*Gameclass.SCALE);
                } else {
                    x=Xpostion + (int)Math.floor(Math.random() * (75 - (55) + 1) + 55) * Gameclass.SCALE;
                    width = (int)(112*Gameclass.SCALE);
                }
                y=Yposition - (int)Math.floor(Math.random() * (65 - (35) + 1) + 35) * Gameclass.SCALE;
                height = (int)(112*Gameclass.SCALE);
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
            x = X + (int)(120*Gameclass.SCALE);
            width = (int) (-212*Gameclass.SCALE);
        } else {
            x = X - (int)(95*Gameclass.SCALE);
            width = (int) (212*Gameclass.SCALE);
        }
        height = (int) (152*Gameclass.SCALE);
        y = Y - (int) (56*Gameclass.SCALE);
    }
}
