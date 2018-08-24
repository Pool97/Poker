package server.model;

import java.util.ArrayList;

public class PositionsHandler {

    private ArrayList<Position> availablePositions;

    private PositionsHandler(int quantity) {
        availablePositions = new ArrayList<>();
        availablePositions.add(Position.SB);

        if (quantity >= 2)
            availablePositions.add(Position.BB);
        if (quantity >= 3)
            availablePositions.add(0, Position.D);
        if (quantity >= 4)
            availablePositions.add(Position.CO);
        if (quantity >= 5)
            availablePositions.add(availablePositions.size() - 1, Position.UTG);
        if (quantity >= 6)
            availablePositions.add(availablePositions.size() - 1, Position.HJ);
    }

    public static PositionsHandler createPositions(int quantity) {
        return new PositionsHandler(quantity);
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
