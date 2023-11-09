package GameState;

import main.Gameclass;

public class States {
    protected Gameclass game;

    public States(Gameclass game){
        this.game = game;
    }
    public Gameclass getGame(){
        return this.game;
    }
}
