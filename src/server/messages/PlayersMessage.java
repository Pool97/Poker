package server.messages;

import interfaces.Message;
import server.model.PlayerModel;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayersMessage implements Message, Serializable {
    private ArrayList<PlayerModel> playersInfo;

    public PlayersMessage(ArrayList<PlayerModel> playersInfo) {
        this.playersInfo = playersInfo;
    }

    public ArrayList<PlayerModel> getPlayersInfo() {
        return playersInfo;
    }
}
