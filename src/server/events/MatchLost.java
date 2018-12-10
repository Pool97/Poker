package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class MatchLost implements ServerEvent {
    private String nickname;
    private int rankPosition;
    private boolean isCreator;

    public MatchLost(String nickname) {
        this.nickname = nickname;
    }

    public MatchLost(String nickname, int rankPosition, boolean isCreator){
        this.nickname = nickname;
        this.rankPosition = rankPosition;
        this.isCreator = isCreator;
    }

    public String getNickname() {
        return nickname;
    }

    public int getRankPosition(){
        return rankPosition;
    }

    public boolean isCreator(){
        return isCreator;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
