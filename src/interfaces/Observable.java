package interfaces;

public interface Observable {

    /**
     * Permette di aggiungere alla lista degli osservatori un nuovo subscriber.
     *
     * @param observer Osservatore dei cambiamenti dell'Observable.
     */

    void attach(Observer observer);

    /**
     * Permette di rimuovere dalla lista degli osservatori un determinato subscriber.
     *
     * @param observer Osservatore dei cambiamenti dell'Observable.
     */

    void detach(Observer observer);

    /**
     * Permette di informare tutti gli osservatori dei cambiamenti avvenuti nell'Observable.
     */

    void notifyObservers();
}
