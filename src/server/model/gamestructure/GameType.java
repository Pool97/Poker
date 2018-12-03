package server.model.gamestructure;

public abstract class GameType {
    private int ante;

    public GameType(int ante){
        this.ante = ante;
    }

    public int getAnte(){
        return ante;
    }
}
