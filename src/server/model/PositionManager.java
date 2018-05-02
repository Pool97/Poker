package server.model;

import java.util.ArrayList;

public class PositionManager {

    private ArrayList<Position> availablePositions;

    public PositionManager() {
        availablePositions = new ArrayList<>();
    }

    public void addPositions(int playersNumber) {
        availablePositions.add(Position.SB);

        if (playersNumber >= 2)
            availablePositions.add(Position.BB);
        if (playersNumber >= 3)
            availablePositions.add(0, Position.D);
        if (playersNumber >= 4)
            availablePositions.add(Position.CO);
        if (playersNumber >= 5)
            availablePositions.add(availablePositions.size() - 1, Position.UTG);
        if (playersNumber >= 6)
            availablePositions.add(availablePositions.size() - 1, Position.HJ);
    }

    public void removePosition() {
        if (availablePositions.contains(Position.UTG))
            availablePositions.remove(Position.UTG);
        else if (availablePositions.contains(Position.HJ))
            availablePositions.remove(Position.HJ);
        else if (availablePositions.contains(Position.CO))
            availablePositions.remove(Position.CO);
        else
            availablePositions.remove(Position.D);
    }

    public Position nextPosition(Position oldPosition) {
        return availablePositions.get((availablePositions.indexOf(oldPosition) + 1) % availablePositions.size());
    }

    public Position getPosition(int index) {
        return availablePositions.get(index);
    }

    public ArrayList<Position> getPositions() {
        return availablePositions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Position position : availablePositions)
            sb.append(position.toString() + "\n");
        return sb.toString();
    }
}
