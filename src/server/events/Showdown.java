package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

import java.util.ArrayList;

public class Showdown implements ServerEvent {
    private ArrayList<String> playersInGame;

    public Showdown() {
        playersInGame = new ArrayList<>();
    }

    public void addNicknamePlayerToList(String nickname){
        playersInGame.add(nickname);
    }

    public ArrayList<String> getPlayersInGame(){
        return playersInGame;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
