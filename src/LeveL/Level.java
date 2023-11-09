package LeveL;

public class Level {
    private int[][] lvlData;

    public Level(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public int getSpriteIndex(int x, int y) {
        return this.lvlData[x][y];
    }

    public int[][] getLvlData() {
        return this.lvlData;
    }
}
