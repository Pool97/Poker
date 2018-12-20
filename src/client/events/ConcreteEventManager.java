package client.events;

import client.net.Client;
import client.ui.components.PlayerBoard;
import client.ui.dialogs.LoserDialog;
import client.ui.dialogs.WinnerDialog;
import client.ui.frames.BoardFrame;
import client.ui.table.PokerTable;
import interfaces.ActionManager;
import server.events.*;
import utils.Utils;

import java.awt.*;

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
        event.getOptions()
                .forEach(action -> Client.logger.info("Azione: " + action.toString()));
    }

    @Override
    public void process(ServerClosed event) {
        Client.getInstance().close();
    }

    @Override
    public void process(PlayerRound event) {
        logAvailableActions(event);
        PlayerBoard playerBoard = pokerTable.getPlayerBoardBy(event.getPlayerNickname());
        playerBoard.setAnimationEnabled(true);
        playerBoard.repaint();
        if (event.getPlayerNickname().equalsIgnoreCase(Client.getInstance().getNickname()))
            event.getOptions().forEach(action -> action.accept(actionManager));
    }

    @Override
    public void process(PlayerLogged event) {
        PlayerBoard playerBoardLogged;
        playerBoardLogged = new PlayerBoard(event.getNickname(), event.getPosition(), true, event.getChips(), event.getAvatar());
        if (event.getNickname().equalsIgnoreCase(Client.getInstance().getNickname())) {
            playerBoardLogged.setNicknameColor(Color.YELLOW);
        }
        pokerTable.sit(playerBoardLogged);

    }

    @Override
    public void process(PlayerHasWin event) {
        pokerTable.removePlayer(event.getNickname());
        if (Client.getInstance().getNickname().equals(event.getNickname())) {
            WinnerDialog dialog = new WinnerDialog("Hai vinto mentekatto", "Complimenti Player!! " +
                    event.getNickname());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            boardFrame.dispose();
        }
    }

    @Override
    public void process(MatchLost event) {
        if (Client.getInstance().getNickname().equals(event.getNickname())) {
            LoserDialog dialog = new LoserDialog("Hai perso!", "Ti sei classificato: " + event.getRankPosition() + "°. " +
                    "Vuoi continuare a seguire il match?", event.isCreator());
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
        pokerTable.refreshPot(event.getPot());
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

        if (event.getNickname().equalsIgnoreCase(Client.getInstance().getNickname()))
            playerBoard.coverCards(false);

    }

    @Override
    public void process(PlayerDisconnected event) {
        if (pokerTable.isPlayerPresent(event.getNickname()))
            pokerTable.removePlayer(event.getNickname());
    }

    @Override
    public void process(Showdown event) {
        pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(false));
    }

    @Override
    public void process(TurnEnded event) {
        pokerTable.refreshCommunityCardBoard();
        pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(true));
        pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.setHandIndicator(Utils.EMPTY));
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
