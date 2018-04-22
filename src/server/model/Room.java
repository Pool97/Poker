package server.model;

import client.messages.WelcomeMessage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe che permette di modellizzare il funzionamento di una stanza da gioco.
 * Si occupa di mantenere aperte le connessioni ai Players e di effettuare tutte
 * le modifiche necessarie ai Model dei Players.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Room {
    private HashMap<PlayerModel, Socket> players;
    private int size;

    /**
     * Costruttore vuoto di Room.
     */

    public Room() {
        players = new HashMap<>();
    }

    /**
     * Permette di aggiungere un nuovo giocatore alla stanza.
     *
     * @param playerMessage Il messaggio contenente le informazioni di base del Player.
     * @param socket        Il collegamento con il Client.
     */

    public void addPlayer(WelcomeMessage playerMessage, Socket socket) {
        PlayerModel playerModel = new PlayerModel(playerMessage.getNickname(), playerMessage.getNickname());
        players.put(playerModel, socket);
    }

    /**
     * Permette di ritornare la dimensione della stanza, ossia il numero di Players presenti.
     *
     * @return Dimensione della stanza
     */

    public int getSize() {
        return size;
    }

    /**
     * Permette di impostare la dimensione della stanza. Utile all'inizio quando si vuole specificare un limite
     * massimo dei giocatori che si devono connettere alla partita.
     *
     * @param size Dimensione da impostare per la stanza
     */

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Permette di ritornare il numero di Players della stanza. Questo metodo è utile in fase iniziale per monitorare
     * l'accesso dei Clients al Server. Quando tutti i clients si sono connessi, utilizzare questo metodo oppure {@link Room#getSize()}
     * è del tutto indifferente perchè restituiscono lo stesso identico valore.
     *
     * @return Numero di Players presenti nella stanza.
     */

    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Permette di restituire il Model di tutti i players sotto forma di ArrayList.
     *
     * @return Model di tutti i Players.
     */

    public ArrayList<PlayerModel> getPlayers() {
        return new ArrayList<>(players.keySet());
    }

    /**
     * Permette di restituire le connessioni di tutti i Players sotto forma di ArrayList.
     *
     * @return Connessioni di tutti i Players.
     */

    public ArrayList<Socket> getConnections() {
        return new ArrayList<>(players.values());
    }

    /**
     * Permette di traslare di una posizione tutti i Players del Match.
     * Esempio: se alla fine del turno un Player era il D (= Dealer) nel prossimo turno diventerà SB
     * ( = Small Blind) e così via per tutti i Players.
     */

    public void movePlayersPosition() {
        for (PlayerModel playerModel : players.keySet()) {
            PlayerPosition actualPosition = playerModel.getTurnPosition();
            if (actualPosition == PlayerPosition.CO)
                playerModel.setTurnPosition(PlayerPosition.D);
            else
                playerModel.setTurnPosition(PlayerPosition.values()[actualPosition.ordinal() + 1]);
        }
    }
}
