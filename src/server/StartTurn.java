package server;

import java.util.ArrayList;

/**
 * StartTurn rappresenta il secondo stato dell'automa.
 * Esso può iniziare per via di due transizioni:
 * - Transizione da StartMatch: StartTurn rappresenta il primo turno della partita di Poker.
 * - Transizione da ShowdownTurn: StartTurn rappresenta un nuovo turno della partita di Poker, subito
 * dopo la conclusione del precedente, conclusosi con lo stato di Showdown.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartTurn implements PokerState, Observable{
    private MatchModel matchModel;
    private ArrayList<Observer> observers;

    /**
     * Costruttore della classe StartTurn.
     * Permette di aggiornare i valori di Pot, Small Blind e Big Blind della partita e aggiorna
     * tutte le posizioni dei Players rimasti in partita.
     * @param matchModel Modello del Match
     */

    public StartTurn(MatchModel matchModel){
        this.matchModel = matchModel;
        observers = new ArrayList<>();
        matchModel.setPot(0);
        matchModel.setBigBlind(20000);
        matchModel.setSmallBlind(10000);
        //aggiornare la posizione dei Players
        update();
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
     * passare al prossimo turno.
     */

    @Override
    public void update() {
        for(Observer observer : observers){
            observer.update(this);
        }
    }
}
