package server.messages;

import interfaces.Message;
import server.model.PlayerModel;

import java.io.Serializable;
import java.util.ArrayList;

public class StartingMatchMessage implements Message, Serializable {
    private ArrayList<PlayerModel> playersInfo;
    private int pot;
    private int bigBlind;
    private int smallBlind;

    public StartingMatchMessage(ArrayList<PlayerModel> playersInfo, int pot, int bigBlind, int smallBlind) {
        this.playersInfo = playersInfo;
        this.pot = pot;
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
    }
}
