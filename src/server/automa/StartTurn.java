package server.automa;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.messages.StartMessage;
import server.model.DeckModel;
import server.model.MatchModel;
import server.model.Room;
import server.socket.ServerManager;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Si supponga che il match di Poker sia rappresentato logicamente da un automa a stati finiti.
 * StartTurn è lo stato iniziale dell'automa
 *
 * Esso può iniziare per via di due transizioni:
 * - Transizione da MatchConfigurator: StartTurn rappresenta il primo turno della partita di Poker.
 * - Transizione da ShowdownState: StartTurn rappresenta un nuovo turno della partita di Poker, subito
 * dopo la conclusione del precedente, conclusosi con lo stato di Showdown.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartTurn implements PokerState, Observable {
    private MatchModel matchModel;
    private DeckModel deckModel;
    private static int TURN_NUMBER;
    private ServerManager connectionHandler;
    private ArrayList<Observer> observers;


    /**
     * Costruttore della classe StartTurn.
     * Permette di aggiornare i valori di Pot, Small Blind e Big Blind della partita e aggiorna
     * tutte le posizioni dei Players rimasti in partita.
     * @param matchModel Modello del Match
     */

    public StartTurn(MatchModel matchModel, ServerManager connectionHandler) {
        observers = new ArrayList<>();
        deckModel = new DeckModel();
        this.matchModel = matchModel;
        this.connectionHandler = connectionHandler;

    }

    /**
     * Permette di aggiungere alla lista degli osservatori un nuovo subscriber.
     * @param observer Osservatore dei cambiamenti che avvengono all'interno di StartTurn.
     */

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Permette di rimuovere dalla lista degli osservatori un determinato subscriber.
     * @param observer Osservatore dei cambiamenti che avvengono all'interno di StartTurn.
     */

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Permette di informare tutti gli osservatori che lo StartTurn si è concluso e che è possibile
     * passare al prossimo stato.
     */

    @Override
    public void notifyObservers() {
        observers.stream().forEach(observer -> observer.update(this));
    }

    //TODO: da rifattorizzare
    @Override
    public void start() {
        deckModel.createAndShuffle();
        matchModel.resetPot();
        if (TURN_NUMBER == 0) {
            matchModel.setStartingChipAmount(connectionHandler.getRoom().getSize() * 10000);

            connectionHandler.getRoom().getPlayers().stream().forEach(player -> player.setTotalChips(matchModel.getStartingChipAmount()));
            matchModel.setInitialBlinds();
            matchModel.setFinalBigBlind();
        } else {
            matchModel.increaseBlinds();
        }
        TURN_NUMBER++;
        Room room = connectionHandler.getRoom();
        for (Socket socket : room.getConnections()) {
            CountDownLatch countdown = new CountDownLatch(1);
            connectionHandler.sendMessage(socket, countdown, new StartMessage(matchModel.getBigBlind(), matchModel.getSmallBlind(), matchModel.getPot(),
                    matchModel.getStartingChipAmount()));
            try {
                countdown.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyObservers();
    }

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }
}
