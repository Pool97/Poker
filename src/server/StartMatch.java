package server;

import java.util.ArrayList;

/**
 * Si supponga che il match di Poker sia rappresentato logicamente da un automa a stati finiti.
 * Questo è lo stato iniziale dell'automa, è necessario settare i parametri iniziali del match,
 * che evolveranno nel corso di tutti gli stati dell'automa.
 * È inoltre necessario informare i vari clients dell'avvio della partita.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartMatch implements PokerState, Observable {
    private MatchModel matchModel;
    private ArrayList<Observer> observers;

    /**
     * Costruttore vuoto della classe StartMatch.
     * Setta i parametri iniziali del match e informa tutti i Players che la partità può iniziare.
     */

    public StartMatch(MatchModel matchModel){
        this.matchModel = matchModel;
        observers = new ArrayList<>();
        matchModel.setPot(0);
        matchModel.setSmallBlind(2000);
        matchModel.setBigBlind(4000);
        //informo i players e aggiorno la grafica
        update();

    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void update() {
        for(Observer observer : observers)
            observer.update(this);
    }
}
