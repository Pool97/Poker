package interfaces;

import client.components.PlayerBoard;

public interface TableSide {
    boolean hasAvailableSeat();

    void sit(PlayerBoard playerBoard);
}
