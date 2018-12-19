package client.ui.table;

import client.ui.components.PlayerBoard;
import interfaces.TableSide;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HorizontalTableSide extends JPanel implements TableSide {
    private List<PlayerBoard> playersSeated;
    private int numberOfSeats;

    public HorizontalTableSide(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
        playersSeated = new ArrayList<>();
        setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        setSideProperties();
    }

    public void setSideProperties() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Utils.TRANSPARENT);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public boolean hasAvailableSeat() {
        return playersSeated.size() != numberOfSeats;
    }

    @Override
    public void sit(PlayerBoard playerBoard) {
        add(playerBoard);
        playersSeated.add(playerBoard);
        revalidate();
        separateSeats();
    }

    public void separateSeats() {
        if (hasAvailableSeat())
            add(Box.createRigidArea(new Dimension(300, 10)));
    }

    public boolean hasContained(PlayerBoard playerBoard) {
        return playersSeated.stream().anyMatch(playerBoard1 -> playerBoard1 == playerBoard);
    }

    public void removePlayer(PlayerBoard playerBoard) {
        playersSeated.remove(playerBoard);
        remove(playerBoard);
    }
}
