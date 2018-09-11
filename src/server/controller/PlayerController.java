package server.controller;

import server.events.EventsContainer;
import server.model.PlayerModel;

import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Classe che gestisce la connessione con il PlayerBoard, lato server.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerController {
    private PlayerModel playerModel;
    private StreamHandler connection;

    public PlayerController(Socket socket) {
        connection = new StreamHandler(socket);
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public Runnable getTaskForSend(EventsContainer message) {
        return connection.createRequestSender(message);
    }

    public Callable<EventsContainer> getTaskForReceive() {
        return connection.createRequestHandler();
    }
}


