package server.states;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import server.model.DeckModel;
import server.model.MatchModel;

import java.util.ArrayList;

/**
 * Si supponga che il match di Poker sia rappresentato logicamente da un automa a stati finiti.
 * StartTurn è lo stato iniziale dell'automa
 *
 * Esso può iniziare per via di due transizioni:
 * - Transizione da MatchConfigurator: StartTurn rappresenta il primo turno della partita di Poker.
 * - Transizione da ShowdownPhase: StartTurn rappresenta un nuovo turno della partita di Poker, subito
 * dopo la conclusione del precedente, conclusosi con lo stato di Showdown.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartTurn implements PokerState, Observable {
    private MatchModel matchModel;
    private DeckModel deckModel;
    private ArrayList<Observer> observers;


    /**
     * Costruttore della classe StartTurn.
     * Permette di aggiornare i valori di Pot, Small Blind e Big Blind della partita e aggiorna
     * tutte le posizioni dei Players rimasti in partita.
     * @param matchModel Modello del Match
     */

    public StartTurn(MatchModel matchModel){
        observers = new ArrayList<>();
        deckModel = new DeckModel();
        deckModel.createAndShuffle();
        this.matchModel = matchModel;
        this.matchModel.resetPot();
        this.matchModel.increaseBlinds();
        this.matchModel.movePlayersPosition();
        //informa i players del cambiamento
        this.notifyObservers();
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
}
