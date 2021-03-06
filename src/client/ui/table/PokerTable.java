package client.ui.table;

import client.ui.components.PlayerBoard;
import interfaces.TableSide;
import server.events.PlayerUpdated;
import utils.Utils;

import javax.swing.*;
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
        createCommunityCardsBoard();
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

    private void createCommunityCardsBoard() {
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
        tableSides.stream()
                .filter(TableSide::hasAvailableSeat)
                .findFirst()
                .get()
                .sit(playerBoard);

        playerBoards.add(playerBoard);
    }

    public PlayerBoard getPlayerBoardBy(String playerName) {
        return playerBoards
                .stream()
                .filter(board -> board.getNickname().equalsIgnoreCase(playerName))
                .findFirst()
                .get();
    }

    public boolean isPlayerPresent(String playerName) {
        return playerBoards.stream().anyMatch(playerBoard -> playerBoard.getNickname().equalsIgnoreCase(playerName));
    }

    public void updatePot(int updateValue){
        communityCardsBoard.updatePot(updateValue);
    }

    public void clearCommunityCardBoard(){
        communityCardsBoard.clearBoard();
        communityCardsBoard.createCardPlaceholders();
    }

    public void updatePlayerProperties(PlayerUpdated event) {
        PlayerBoard board = getPlayerBoardBy(event.getNickname());
        board.setChipIndicator(event.getChips());
        if(!event.getAction().equals(""))
            board.setHandIndicator(event.getAction() + " "+ (event.getValue() != 0 ? event.getValue() + "$" : ""));
        board.setAnimationEnabled(false);
        Timer timer = new Timer(3000, ae -> board.setHandIndicator(Utils.EMPTY));
        timer.setRepeats(false);
        timer.start();
        if(event.getAction().equals("FOLD"))
            board.setFolded(true);

    }


    public void addCardToCommunityCardsBoard(String imageDirectoryPath) {
        communityCardsBoard.revealCard(imageDirectoryPath);
    }

    public List<PlayerBoard> getPlayerBoard() {
        return playerBoards;
    }

    public void trigger(String nickname){
        getPlayerBoardBy(nickname).setAnimationEnabled(true);
    }

    public void removePlayer(String nickname) {
        PlayerBoard playerToRemove = getPlayerBoardBy(nickname);
        playerBoards.remove(playerToRemove);
        tableSides.stream()
                .filter(tableSide -> tableSide.hasContained(playerToRemove))
                .forEach(tableSide -> tableSide.removePlayer(playerToRemove));
    }
}
