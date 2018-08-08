package client.components;

import interfaces.TableSide;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VerticalTableSide extends JPanel implements TableSide {
    private List<PlayerBoard> playersSeated;
    private int numberOfSeats;

    public VerticalTableSide(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
        playersSeated = new ArrayList<>();
        setSideProperties();
    }

    public void setSideProperties() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Utils.TRANSPARENT);
    }

    @Override
    public boolean hasAvailableSeat() {
        return playersSeated.size() != numberOfSeats;
    }

    @Override
    public void sit(PlayerBoard playerBoard) {
        add(playerBoard);
        playersSeated.add(playerBoard);
        separateSeats();
    }

    public void separateSeats() {
        if (hasAvailableSeat())
            add(Box.createRigidArea(new Dimension(0, 50)));
    }

    public boolean hasContained(PlayerBoard playerBoard) {
        return playersSeated.stream().anyMatch(playerBoard1 -> playerBoard1 == playerBoard);
    }

    public void removePlayer(PlayerBoard playerBoard) {
        remove(playerBoard);
    }
}
