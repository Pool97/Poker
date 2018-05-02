package server.model;

import java.util.ArrayList;

public class MatchModel {
    private int smallBlind;
    private int bigBlind;
    private int startChips;
    private PositionManager positionManager;

    public MatchModel(int smallBlind, int bigBlind, int startChips) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.startChips = startChips;
        positionManager = new PositionManager();
    }

    public MatchModel() {
        smallBlind = 0;
        bigBlind = 0;
        positionManager = new PositionManager();
    }

    /**
     * Permette di incrementare i bui in ogni turno. Per non rendere troppo lunga la partita ogni Big Blind
     * sarà il doppio di quello precedente. Lo Small Blind si calcola di conseguenza, essendo da regolamento
     * ufficiale la metà del Big Blind. Ovviamente è ragionevole porre un limite all'aumento del Big Blind, esso
     * non può assumere valori che sono al di sopra dell'importo iniziale di chips-per-player.
     */

    public void increaseBlinds() {
        if (bigBlind < startChips) {
            bigBlind *= 2;
            smallBlind = bigBlind / 2;
        }
    }

    public void setInitialBlinds() {
        bigBlind = startChips / 50;
        smallBlind = bigBlind / 2;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getStartChips() {
        return startChips;
    }

    public void setStartChips(int startChips) {
        this.startChips = startChips;
    }

    public Position getNextPosition(Position oldPosition) {
        return positionManager.nextPosition(oldPosition);
    }

    public Position getPosition(int index) {
        return positionManager.getPosition(index);
    }

    public ArrayList<Position> getPositions() {
        return positionManager.getPositions();
    }

    public void setPositions(int size) {
        positionManager.addPositions(size);
    }
}
