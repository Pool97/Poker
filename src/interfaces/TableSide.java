package interfaces;

import client.ui.components.PlayerBoard;

public interface TableSide {
    boolean hasAvailableSeat();

    void sit(PlayerBoard playerBoard);

    void removePlayer(PlayerBoard playerBoard);

    boolean hasContained(PlayerBoard playerBoard);
}
