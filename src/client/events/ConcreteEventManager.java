package client.events;

import client.net.Client;
import client.net.ClientWrapper;
import client.ui.components.PlayerBoard;
import client.ui.dialogs.LoserDialog;
import client.ui.dialogs.WinnerDialog;
import client.ui.frames.BoardFrame;
import client.ui.table.PokerTable;
import interfaces.ActionManager;
import server.events.*;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;

public class ConcreteEventManager extends EventsAdapter {
    private ActionManager actionManager;
    private BoardFrame boardFrame;
    private PokerTable pokerTable;

    public ConcreteEventManager(BoardFrame boardFrame) {
        actionManager = new ConcreteActionManager(boardFrame.getActionBoard());
        this.boardFrame = boardFrame;
        this.pokerTable = boardFrame.getPokerTable();
    }

    private void logAvailableActions(PlayerRound event) {
        Client.logger.info("Azioni disponibili: " + event.getPlayerNickname() + " \n");
        event.getOptions().forEach(action -> Client.logger.info("Azione: " + action.toString()));
    }

    @Override
    public void process(ServerClosed event) {
        ClientWrapper.getInstance().close();
    }

    @Override
    public void process(PlayerRound event) {
        logAvailableActions(event);
        pokerTable.trigger(event.getPlayerNickname());
        if (event.getPlayerNickname().equals(ClientWrapper.getInstance().getNickname()))
            event.getOptions().forEach(action -> action.accept(actionManager));
    }

    @Override
    public void process(PlayerLogged event) {
        PlayerBoard playerBoardLogged;
        playerBoardLogged = new PlayerBoard(event.getNickname(), true, event.getChips(), event.getAvatar());

        if (event.getNickname().equals(ClientWrapper.getInstance().getNickname()))
            playerBoardLogged.setNicknameColor(Color.YELLOW);

        pokerTable.sit(playerBoardLogged);
    }

    @Override
    public void process(PlayerHasWin event) {
        if (ClientWrapper.getInstance().getNickname().equals(event.getNickname())) {
            WinnerDialog dialog = new WinnerDialog(event.getNickname(),
                    pokerTable.getPlayerBoardBy(event.getNickname()).getChips(),
                    pokerTable.getPlayerBoardBy(event.getNickname()).getAvatar());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        pokerTable.removePlayer(event.getNickname());    }

    @Override
    public void process(MatchLost event) {
        if (ClientWrapper.getInstance().getNickname().equals(event.getNickname())) {
            LoserDialog dialog = new LoserDialog(event.isCreator(), event.getNickname(),
                    pokerTable.getPlayerBoardBy(event.getNickname()).getAvatar(), event.getRankPosition());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
        pokerTable.removePlayer(event.getNickname());
    }

    @Override
    public void process(PlayerUpdated event) {
        pokerTable.updatePlayerProperties(event);
    }

    @Override
    public void process(PotUpdated event) {
        pokerTable.updatePot(event.getPot());
    }

    @Override
    public void process(CommunityUpdated event) {
        while (event.number() != 0)
            pokerTable.addCardToCommunityCardsBoard(event.getCard().getImageDirectoryPath());
    }

    @Override
    public void process(TurnStarted event) {
        PlayerBoard playerBoard = pokerTable.getPlayerBoardBy(event.getNickname());
        playerBoard.setPosition(event.getTurnPosition());
        playerBoard.changeCard(event.getFrontImageCards().get(0), 0);
        playerBoard.changeCard(event.getFrontImageCards().get(1), 1);

        if (event.getNickname().equalsIgnoreCase(ClientWrapper.getInstance().getNickname()))
            playerBoard.coverCards(false);

    }

    @Override
    public void process(PlayerDisconnected event) {
        if (pokerTable.isPlayerPresent(event.getNickname()))
            pokerTable.removePlayer(event.getNickname());
    }

    @Override
    public void process(Showdown event) {
        ArrayList<String> nicknamePlayersInGame = event.getPlayersInGame();
        nicknamePlayersInGame.stream()
                .map(nickname -> pokerTable.getPlayerBoardBy(nickname))
                .forEach(playerBoard -> playerBoard.coverCards(false));
    }

    @Override
    public void process(TurnEnded event) {
        pokerTable.clearCommunityCardBoard();
        pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(true));
        pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.setHandIndicator(Utils.EMPTY));
        pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.setFolded(false));
    }

    @Override
    public void process(ChatMessage event) {
        boardFrame.getChat().addMessage(event);
    }

    @Override
    public void process(ChatNotify event) {
        boardFrame.getChat().addNotify(event);
    }
}
