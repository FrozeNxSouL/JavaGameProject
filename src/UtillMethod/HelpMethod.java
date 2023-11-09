package UtillMethod;

import main.Gameclass;

public class HelpMethod {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Gameclass.TILE_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Gameclass.GAME_HEIGHT)
            return true;
        float xIndex = x / Gameclass.TILE_SIZE;
        float yIndex = y / Gameclass.TILE_SIZE ;

        int value = lvlData[(int)(yIndex) + 20 ][(int) xIndex];
        if (value != 0) {
            return true;
        }
        return false;
    }
}