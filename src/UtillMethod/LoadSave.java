package UtillMethod;

import Entities.*;
import main.Gameclass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadSave {
    public static final String GUISprite = "Graphic/GUI.png";
    public static final String MenuSprite = "Graphic/Menu.png";
    public static final String ProjectilesSprite = "Graphic/projectiles.png";
    public static final String PlayerSprite = "Graphic/playerspritesheet.png";

    public static final String NightmareSprite= "Graphic/Nightmare.png";
    public static final String SkeletonBasherSprite= "Graphic/SkeletonBasher.png";
    public static final String SkeletonPolearmSprite= "Graphic/SkeletonPolearm.png";
    public static final String SkeletonWarriorSprite= "Graphic/SkeletonWarrior.png";
    public static final String GoblinSprite= "Graphic/goblin.png";
    public static final String BossSprite= "Graphic/BossDemonSpriteSheet.png";

    public static final String GrassLandLevelSpriteBack = "Graphic/grasslandbackA.png";
    public static final String GrassLandLevelSpriteFloor = "Graphic/grasslandfloorA.png";
    public static final String GrassLandLevelSpriteFront = "Graphic/grasslandfront1A.png";
    public static final String GrassLandLevelSpriteCheckpoint = "Graphic/grasslandmapcheckpointA.png";
    public static final String GrassLandLevelBackground = "Graphic/Field 2-Morning.jpeg";
    public static final String GrassLandLevelSpriteFloorCollision = "Graphic/grasslandfloorCollisionA.png";
    public static final String GrassLandLevelSpriteEnemy= "Graphic/grasslandEnemySpawnA.png";

    public static final String BlightedForestLevelSpriteBack = "Graphic/BlightedForestBack.png";
    public static final String BlightedForestLevelSpriteFloor = "Graphic/BlightedForestFloor.png";
    public static final String BlightedForestLevelSpriteFront = "Graphic/BlightedForestFront.png";
    public static final String BlightedForestLevelSpriteCheckpoint = "Graphic/BlightedForestObj.png";
    public static final String BlightedForestLevelBackground = "Graphic/DesertBG.png";
    public static final String BlightedForestLevelSpriteFloorCollision = "Graphic/BlightedForestCollision.png";
    public static final String BlightedForestLevelSpriteEnemy= "Graphic/BlightedForestEnemySpawn.png";

    public static final String BossMapLevelSpriteBack = "Graphic/BossMapBack.png";
    public static final String BossMapLevelSpriteFloor = "Graphic/BossMapFloor.png";
    public static final String BossMapLevelSpriteFront = "Graphic/BossMapFront.png";
    public static final String BossMapLevelSpriteCheckpoint = "Graphic/BossMapObj.png";
    public static final String BossMapLevelBackground1 = "Graphic/The-Dwan.png";
    public static final String BossMapLevelBackground2 = "Graphic/The-DwanRed.png";
    public static final String BossMapLevelSpriteFloorCollision = "Graphic/BossMapFloorCollision.png";
    public static final String BossMapLevelSpriteEnemy= "Graphic/BossMapEnemySpawn.png";

    public static final String InGameEffect = "Graphic/GameFx.png";

    public static BufferedImage LoadSprite(String file){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+file);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
    public static BufferedImage[][] getEffect(String file,int size){
        BufferedImage img = LoadSprite(file);
        BufferedImage[][] list = new BufferedImage[10][30];
        for (int i=0;i< img.getHeight() / size;i++) {
            for (int j = 0; j < img.getWidth() / size; j++) {
                list[i][j] = img.getSubimage(j * size, i * size, size, size);
            }
        }
        return list;
    }

    public static ArrayList<Enemy> getEnemyinMap(String file){
        BufferedImage img = LoadSprite(file);
        BufferedImage tempimg;
        boolean found = false;
        ArrayList<Enemy> list = new ArrayList<>();
        for (int i=0;i< img.getHeight() / (Gameclass.TILE_DEFAULT_SIZE*2);i++) {
            for (int j = 0; j < img.getWidth() / (Gameclass.TILE_DEFAULT_SIZE*2); j++) {
                tempimg = img.getSubimage(j * (Gameclass.TILE_DEFAULT_SIZE*2), i * (Gameclass.TILE_DEFAULT_SIZE*2), (Gameclass.TILE_DEFAULT_SIZE*2), (Gameclass.TILE_DEFAULT_SIZE*2));
                for (int k = 0; k < (Gameclass.TILE_DEFAULT_SIZE*2); k++) {
                    if (found) {
                        break;
                    }
                    for (int l = 0; l < (Gameclass.TILE_DEFAULT_SIZE*2); l++) {
                        Color color = new Color(tempimg.getRGB(l, k));
                        int value = color.getRed();
                        if (value == 255) {
                            list.add(new Basher(j*Gameclass.TILE_SIZE*2, i * (int) (Gameclass.TILE_SIZE) - (int)(5*Gameclass.TILE_SIZE)));
                            System.out.println("BASHER x=" + j*Gameclass.TILE_SIZE*2 + "y="+ (i * (int) (Gameclass.TILE_SIZE) - (int)(5*Gameclass.TILE_SIZE)) + "r"+color.getRed()+"g"+color.getGreen()+"b"+color.getBlue());
                            found = true;
                            break;
                        } else if (value == 105) {
                            list.add(new BossDemon(j*Gameclass.TILE_SIZE*2, i * (int) (Gameclass.TILE_SIZE) - (int)(15*Gameclass.TILE_SIZE)));
                            System.out.println("BOSS x=" + j*Gameclass.TILE_SIZE*2 + "y="+ (i * (int) (Gameclass.TILE_SIZE) - (int)(15*Gameclass.TILE_SIZE)) + "r"+color.getRed()+"g"+color.getGreen()+"b"+color.getBlue());
                            found = true;
                            break;
                        } else if (value == 245) {
                            list.add(new Goblin(j*Gameclass.TILE_SIZE*2, i * (int) (Gameclass.TILE_SIZE) - (int)(15*Gameclass.TILE_SIZE)));
                            System.out.println("GOBLIN x=" + j*Gameclass.TILE_SIZE*2 + "y="+ (i * (int) (Gameclass.TILE_SIZE) - (int)(15*Gameclass.TILE_SIZE)) + "r"+color.getRed()+"g"+color.getGreen()+"b"+color.getBlue());
                            found = true;
                            break;
                        } else if (value == 235) {
                            list.add(new SkeletonWarrior(j*Gameclass.TILE_SIZE*2, i * (int) (Gameclass.TILE_SIZE) - (int)(15*Gameclass.TILE_SIZE)));
                            System.out.println("WARRIOR x=" + j*Gameclass.TILE_SIZE*2 + "y="+ (i * (int) (Gameclass.TILE_SIZE) - (int)(15*Gameclass.TILE_SIZE)) + "r"+color.getRed()+"g"+color.getGreen()+"b"+color.getBlue());
                            found = true;
                            break;
                        }
                    }
                }
                found = false;
            }
        }
        return list;
    }

    public static int[][] GettingMapdata(String file){
        BufferedImage img = LoadSprite(file);
        BufferedImage tempimg;
        boolean found = false;
        int[][] lvlData = new int[img.getHeight() / Gameclass.TILE_DEFAULT_SIZE][img.getWidth() / Gameclass.TILE_DEFAULT_SIZE];
        for (int i=0;i< img.getHeight() / Gameclass.TILE_DEFAULT_SIZE;i++){
            for (int j = 0;j<img.getWidth() / Gameclass.TILE_DEFAULT_SIZE;j++){
                tempimg = img.getSubimage(j*Gameclass.TILE_DEFAULT_SIZE,i*Gameclass.TILE_DEFAULT_SIZE,Gameclass.TILE_DEFAULT_SIZE,Gameclass.TILE_DEFAULT_SIZE);
                lvlData[i][j] = 0;
                for (int k=0;k<Gameclass.TILE_DEFAULT_SIZE;k++) {
                    if (found) {
                        break;
                    }
                    for (int l=0;l<Gameclass.TILE_DEFAULT_SIZE;l++){
                        Color color = new Color(tempimg.getRGB(l,k));
                        int value1 = color.getRed(),value2 = color.getGreen(),value3= color.getBlue();
                        if (value1!=0 || value2!=0 || value3!=0) {
                            lvlData[i][j] = i*(img.getWidth() / Gameclass.TILE_DEFAULT_SIZE) + j;
                            found = true;
                            break;
                        }
                    }
                }
                found = false;
            }
        }
        return lvlData;
    }
}
