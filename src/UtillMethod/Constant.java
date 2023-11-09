package UtillMethod;

import main.Gameclass;

public class Constant {
    public static class EnemyConstant{
        public static final int BOSS_DEMON = 99;
        public static final int BOSS_DEMON_WIDTH_DEFAULT= 512;
        public static final int BOSS_DEMON_HEIGHT_DEFAULT = 512;
        public static final int BOSS_DEMON_WIDTH= (int)(0.5*BOSS_DEMON_WIDTH_DEFAULT*Gameclass.SCALE);
        public static final int BOSS_DEMON_HEIGHT =(int)(0.5*BOSS_DEMON_HEIGHT_DEFAULT*Gameclass.SCALE);
        public static final int BOSS_DEMON_DRAWXOFFSET = (int)(100*Gameclass.SCALE);
        public static final int BOSS_DEMON_DRAWYOFFSET = (int)(90*Gameclass.SCALE);

        public static final int BOSS_DEMON_IDLE = 0;
        public static final int BOSS_DEMON_ATTACKONE = 2;
        public static final int BOSS_DEMON_ATTACKTWO = 3;
        public static final int BOSS_DEMON_ATTACKTHREE = 4;
        public static final int BOSS_DEMON_PREPARE = 1;
        public static final int BOSS_DEMON_CHARGE_PREPARE = 6;
        public static final int BOSS_DEMON_CHARGE = 7;
        public static final int BOSS_DEMON_FROMPHASEONE = 8;
        public static final int BOSS_DEMON_RANGE_ATTACK = 5;
        public static final int BOSS_DEMON_CHANGING = 10;
        public static final int BOSS_DEMON_TOPHASETWO = 9;
        public static final int BOSS_DEMON_PHASETWO_IDLE = 11;
        public static final int BOSS_DEMON_PHASETWO_ATTACKONE = 12;
        public static final int BOSS_DEMON_PHASETWO_ATTACKTWO = 13;
        public static final int BOSS_DEMON_PHASETWO_FALL = 14;
        public static final int BOSS_DEMON_HIT = 15;

        public static final int BASHER = 0;
        public static final int BASHER_WIDTH_DEFAULT= 64;
        public static final int BASHER_HEIGHT_DEFAULT = 64;
        public static final int BASHER_WIDTH= (int)(2*BASHER_WIDTH_DEFAULT*Gameclass.SCALE);
        public static final int BASHER_HEIGHT =(int)(2*BASHER_HEIGHT_DEFAULT*Gameclass.SCALE);
        public static final int BASHER_DRAWXOFFSET = (int)(55*Gameclass.SCALE);
        public static final int BASHER_DRAWYOFFSET = (int)(30*Gameclass.SCALE);

        public static final int BASHER_IDLE = 5;
        public static final int BASHER_WALK = 4;
        public static final int BASHER_ATTACKONE = 2;
        public static final int BASHER_ATTACKTWO = 1;
        public static final int BASHER_PREPARE = 0;
        public static final int BASHER_FALL = 3;
        public static final int BASHER_HIT = 6;

        public static final int WARRIOR = 1;
        public static final int WARRIOR_WIDTH_DEFAULT= 150;
        public static final int WARRIOR_HEIGHT_DEFAULT = 150;
        public static final int WARRIOR_WIDTH= (int)(1.3*WARRIOR_WIDTH_DEFAULT*Gameclass.SCALE);
        public static final int WARRIOR_HEIGHT =(int)(1.3*WARRIOR_HEIGHT_DEFAULT*Gameclass.SCALE);
        public static final int WARRIOR_DRAWXOFFSET = (int)(90*Gameclass.SCALE);
        public static final int WARRIOR_DRAWYOFFSET = (int)(80*Gameclass.SCALE);

        public static final int WARRIOR_IDLE = 0;
        public static final int WARRIOR_WALK = 1;
        public static final int WARRIOR_ATTACK = 3;
        public static final int WARRIOR_PREPARE = 2;
        public static final int WARRIOR_FALL = 6;
        public static final int WARRIOR_HIT = 4;

        public static final int GOBLIN = 2;
        public static final int GOBLIN_WIDTH_DEFAULT= 150;
        public static final int GOBLIN_HEIGHT_DEFAULT = 150;
        public static final int GOBLIN_WIDTH= (int)(1.3*GOBLIN_WIDTH_DEFAULT*Gameclass.SCALE);
        public static final int GOBLIN_HEIGHT =(int)(1.3*GOBLIN_HEIGHT_DEFAULT*Gameclass.SCALE);
        public static final int GOBLIN_DRAWXOFFSET = (int)(90*Gameclass.SCALE);
        public static final int GOBLIN_DRAWYOFFSET = (int)(90*Gameclass.SCALE);

        public static final int GOBLIN_IDLE = 0;
        public static final int GOBLIN_WALK = 1;
        public static final int GOBLIN_ATTACK = 3;
        public static final int GOBLIN_PREPARE = 2;
        public static final int GOBLIN_FALL = 5;
        public static final int GOBLIN_HIT = 4;

        public static int getSpriteAmount(int Etype,int Estate){
            switch (Etype){
                case BOSS_DEMON:
                    switch (Estate){
                        case BOSS_DEMON_IDLE,BOSS_DEMON_RANGE_ATTACK,BOSS_DEMON_CHANGING:
                            return 5;
                        case BOSS_DEMON_CHARGE_PREPARE,BOSS_DEMON_ATTACKONE,BOSS_DEMON_ATTACKTWO,BOSS_DEMON_ATTACKTHREE,BOSS_DEMON_TOPHASETWO,BOSS_DEMON_PHASETWO_IDLE:
                            return 6;
                        case BOSS_DEMON_PHASETWO_ATTACKTWO:
                            return 9;
                        case BOSS_DEMON_PREPARE,BOSS_DEMON_FROMPHASEONE,BOSS_DEMON_PHASETWO_FALL:
                            return 8;
                        case BOSS_DEMON_CHARGE:
                            return 11;
                        case BOSS_DEMON_PHASETWO_ATTACKONE,BOSS_DEMON_HIT:
                            return 12;
                        default:
                            return 0;
                    }
                case BASHER :
                    switch (Estate){
                        case BASHER_IDLE, BASHER_ATTACKTWO,BASHER_PREPARE:
                            return 4;
                        case BASHER_WALK:
                            return 12;
                        case BASHER_HIT:
                            return 3;
                        case BASHER_FALL:
                            return 13;
                        case BASHER_ATTACKONE:
                            return 5;
                        default:
                            return 0;
                    }
                case WARRIOR :
                    switch (Estate){
                        case WARRIOR_IDLE,WARRIOR_HIT, WARRIOR_WALK, WARRIOR_FALL:
                            return 4;
                        case WARRIOR_PREPARE:
                            return 6;
                        case WARRIOR_ATTACK:
                            return 2;
                        default:
                            return 0;
                    }
                case GOBLIN :
                    switch (Estate){
                        case GOBLIN_IDLE,GOBLIN_HIT, GOBLIN_FALL:
                            return 4;
                        case GOBLIN_WALK:
                            return 8;
                        case GOBLIN_PREPARE:
                            return 6;
                        case GOBLIN_ATTACK:
                            return 2;
                        default:
                            return 0;
                    }
            }
            return 0;
        }
        public static int GetMaxHealth(int Etype){
            switch (Etype){
                case BASHER :
                    return 10;
                case BOSS_DEMON:
                    return 50;
                case WARRIOR:
                    return 15;
                case GOBLIN:
                    return 6;
                default:
                    return 1;
            }
        }
        public static int GetEnemyDMG(int Etype){
            switch (Etype){
                case BASHER, GOBLIN:
                    return 1;
                case BOSS_DEMON, WARRIOR:
                    return 2;
                default:
                    return 0;
            }
        }
    }

    public static class EnvironmentConstant {
        public static final int BACKGROUND_WIDTH = (int)(1920*0.4*Gameclass.SCALE);
        public static final int BACKGROUND_HEIGHT = (int)(1080*0.4*Gameclass.SCALE);
        public static final int PROJECTILES_WIDTH_DEFAULT= 128;
        public static final int PROJECTILES_HEIGHT_DEFAULT = 128;
        public static final int PROJECTILES_WIDTH= (int)(0.8*PROJECTILES_WIDTH_DEFAULT*Gameclass.SCALE);
        public static final int PROJECTILES_HEIGHT =(int)(0.8*PROJECTILES_HEIGHT_DEFAULT*Gameclass.SCALE);
        public static final int PROJECTILES_SPEED = 5;

        public static final int COUNTER = 0;
        public static final int BLOCK = 2;
        public static final int DASH = 1;
        public static final int SKILL_BUFF = 5;
        public static final int REDBALL = 0;
        public static final int FIRE = 1;

        public static int getEffectSpriteAmount(int action) {
            switch (action) {
                case COUNTER -> {
                    return 25;
                }
                case BLOCK,DASH -> {
                    return 4;
                }
                case SKILL_BUFF -> {
                    return 6;
                }
            }
            return 0;
        }

        public static int getProjectilesSpriteAmount(int action) {
            switch (action) {
                case REDBALL,FIRE -> {
                    return 4;
                }
            }
            return 0;
        }

        public static int getProjectilesDamage(int action) {
            switch (action) {
                case REDBALL -> {
                    return 2;
                }
                case FIRE -> {
                    return 3;
                }
            }
            return 0;
        }
    }
    public static class PlayerConstant {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int WALK = 2;
        public static final int SWORDIDLE = 3;
        public static final int SWORDSTEPPING = 4;
        public static final int SWORDONE = 5;
        public static final int SWORDTWO = 6;
        public static final int SWORDTHREE = 7;
        public static final int SWORDFOUR = 8;
        public static final int SWORDBLOCK = 9;
        public static final int SWORDROLLING = 10;
        public static final int SWORDFALL = 11;
        public static final int SWORDSKILL = 12;

        public static int SpriteAmount(int action) {
            return switch (action) {
                case SWORDTHREE, WALK -> 6;
                case SWORDSTEPPING -> 7;
                case SWORDIDLE, IDLE -> 8;
                case RUNNING, SWORDONE, SWORDTWO, SWORDROLLING -> 9;
                case SWORDFALL,SWORDSKILL -> 12;
                case SWORDFOUR -> 11;
                case SWORDBLOCK -> 1;
                default -> 0;
            };
        }
    }
}
