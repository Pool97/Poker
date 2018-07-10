package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

import java.util.ArrayList;

public class TurnStartedEvent implements ServerEvent {
    private String nickname;
    private String turnPosition;
    private ArrayList<String> frontImageCards;

    public TurnStartedEvent(String nickname, String turnPosition, ArrayList<String> frontImageCards) {
        this.nickname = nickname;
        this.turnPosition = turnPosition;
        this.frontImageCards = frontImageCards;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTurnPosition() {
        return turnPosition;
    }

    public void setPosition(String turnPosition) {
        this.turnPosition = turnPosition;
    }

    public ArrayList<String> getFrontImageCards() {
        return frontImageCards;
    }

    public void setFrontImageCards(ArrayList<String> frontImageCards) {
        this.frontImageCards = frontImageCards;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}