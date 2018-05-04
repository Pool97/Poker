package server.automa;

import events.*;
import interfaces.PokerState;
import interfaces.TransitionStrategy;
import javafx.util.Pair;
import server.model.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Action è lo stato che gestisce un giro di puntate in un determinato turno. È bene ricordare che nel corso di un
 * turno ci possono essere da 1 a 4 giri di puntate, in base all'evoluzione del turno e alla scelta dei
 * giocatori di rimanere in gioco oppure di foldare.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Action implements PokerState {
    private TransitionStrategy strategy;
    private final static String STATE_STARTED = "Lo stato di Action è avviato. \n";
    private final static String START_ACTIONS = "Inizio il giro di puntate non obbligatorie... \n";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private final static String PLAYER_OPTIONS = "Propongo al player le opzioni disponibili per la puntata... \n";
    private final static String PLAYERS_INFO = "Informo tutti i players della puntata effettuata... \n";
    private MatchHandler match;

    /**
     * Costruttore della classe Action
     *
     * @param match Gestore dell'automa
     */

    public Action(MatchHandler match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(START_ACTIONS);
        Room room = match.getRoom();
        Position nextPosition = room.getNextPosition(Position.BB);

        while (!((nextPosition == Position.SB) && (onePlayerRemained() || (isEquityReached())))) {
            PlayerModel player = room.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player);
            }
            nextPosition = room.getNextPosition(nextPosition);
        }

        if (onePlayerRemained()) {
            MatchHandler.logger.info(ONE_PLAYER_ONLY);
            match.setState(new TurnEnd());
        } else if (isEquityReached()) {
            MatchHandler.logger.info(EQUITY_REACHED);
            strategy.makeTransition();
        }
    }

    /**
     * Permette di determinare se la puntata massima è stata pareggiata. Un giro di puntate termina
     * quando la puntata più alta effettuata da un qualsiasi giocatore viene pareggiata, ossia nel giro di puntate,
     * partendo dal giocatore successivo al BB fino a terminare con il giocatore in posizione di SB, non c'è stato
     * nessun altro giocatore a rialzare (Raise) il valore dell'effettiva puntata massima.
     *
     * @return true se la puntata massima è pareggiata, false altrimenti.
     */

    private boolean isEquityReached() {
        Room room = match.getRoom();

        ArrayList<PlayerModel> playerNotFolded = room.getPlayers()
                .stream()
                .filter(player -> !player.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));

        int betMaxAllIn = playerNotFolded.stream()
                .filter(PlayerModel::isAllIn)
                .mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);

        ArrayList<PlayerModel> playerNotAllIn = playerNotFolded
                .stream()
                .filter(player -> !player.isAllIn())
                .collect(Collectors.toCollection(ArrayList::new));

        if (playerNotAllIn
                .stream()
                .anyMatch(player -> player.getTurnBet() < betMaxAllIn))
            return false;

        long distinctBets = playerNotAllIn
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .distinct()
                .count();

        return distinctBets <= 1;
    }

    /**
     * Permette di determinare se nel giro di puntate è rimasto solo un giocatore all'attivo, cioè se tutti i giocatori
     * rimanenti hanno preferito effettuare il fold. In questo caso speciale, il giro di puntate deve essere terminato
     * e bisogna effettuare una transizione diretta alla fine del turno, senza passare dal Flop ecc...
     *
     * @return True se è rimasto solo un giocatore, false altrimenti
     */

    private boolean onePlayerRemained() {
        long notFold = match.getRoom().getPlayers()
                .stream()
                .filter(player -> !player.hasFolded())
                .count();
        return notFold == 1;
    }

    /**
     * Permette di impostare la transizione che deve effettuare lo stato di Action al suo termine. Si ricorda
     * che nel caso di Action ci sono multiple possibilità di transizione a stati diversi, il pattern Strategy
     * propone una soluzione OOP per la gestione della multipla possibilità di transizione.
     *
     * @param strategy Tipo di transizione da effettuare al termine dello stato di Action.
     */

    public void setTransitionStrategy(TransitionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Permette di gestire la puntata da effettuare per il turno del Player indicato da argomento.
     *
     * @param player Player che deve effettuare la mossa.
     */

    private void doAction(PlayerModel player) {
        Room room = match.getRoom();
        TurnModel turnModel = match.getTurnModel();

        int maxValue = room.getPlayers()
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);

        int callValue = maxValue - player.getTurnBet();

        ActionOptionsEvent optionsEvent = new ActionOptionsEvent();
        optionsEvent.addOption(new Pair<>(ActionType.FOLD, 0));

        if (callValue > 0)
            optionsEvent.addOption(new Pair<>(ActionType.CALL, callValue));
        else
            optionsEvent.addOption(new Pair<>(ActionType.CHECK, 0));

        if (player.getChips() > callValue)
            optionsEvent.addOption(new Pair<>(ActionType.RAISE, player.getChips()));

        MatchHandler.logger.info(PLAYER_OPTIONS);
        room.sendMessage(player, new Events(optionsEvent));

        Events actionPerformed = room.readMessage(player);
        ActionPerformedEvent playerAction = (ActionPerformedEvent) actionPerformed.getEvent();
        player.addAction(playerAction.getAction());
        turnModel.increasePot(playerAction.getAction().getValue());

        MatchHandler.logger.info(PLAYERS_INFO);
        room.sendBroadcast(new Events(new PlayerUpdatedEvent(player), new PotUpdatedEvent(turnModel.getPot())));
    }
}
