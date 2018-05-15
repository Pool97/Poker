package client.components;

import interfaces.TableSide;

import java.util.ArrayList;
import java.util.List;

public class PokerTable {
    private final static int HORIZONTAL_NUMBER_OF_SEATS = 2;
    private final static int VERTICAL_NUMBER_OF_SEATS = 1;
    private List<TableSide> tableSides;
    private List<PlayerBoard> playerBoards;
    private CommunityCardsBoard communityCardsBoard;


    public PokerTable() {
        playerBoards = new ArrayList<>();
        tableSides = new ArrayList<>();

        createTopSide();
        createRightSide();
        createBottomSide();
        createLeftSide();
        createEmptyCommunityCards();
    }

    private void createTopSide() {
        tableSides.add(new HorizontalTableSide(HORIZONTAL_NUMBER_OF_SEATS));
    }

    private void createRightSide() {
        tableSides.add(new VerticalTableSide(VERTICAL_NUMBER_OF_SEATS));
    }

    private void createBottomSide() {
        tableSides.add(new HorizontalTableSide(HORIZONTAL_NUMBER_OF_SEATS));
    }

    private void createLeftSide() {
        tableSides.add(new VerticalTableSide(VERTICAL_NUMBER_OF_SEATS));
    }

    private void createEmptyCommunityCards() {
        communityCardsBoard = CommunityCardsBoard.createEmptyCommunityCards();
    }

    public HorizontalTableSide getTopSide() {
        return (HorizontalTableSide) tableSides.get(0);
    }

    public VerticalTableSide getRightSide() {
        return (VerticalTableSide) tableSides.get(1);
    }

    public HorizontalTableSide getBottomSide() {
        return (HorizontalTableSide) tableSides.get(2);
    }

    public VerticalTableSide getLeftSide() {
        return (VerticalTableSide) tableSides.get(3);
    }

    public CommunityCardsBoard getCommunityCardsBoard() {
        return communityCardsBoard;
    }

    public void sit(PlayerBoard playerBoard) {
        tableSides.stream().filter(TableSide::hasAvailableSeat).findFirst().get().sit(playerBoard);
        playerBoards.add(playerBoard);
    }

    public List<PlayerBoard> getPlayerBoards() {
        return playerBoards;
    }
}
