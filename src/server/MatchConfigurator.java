package server;

import client.messages.CreationRoomMessage;
import server.model.MatchModel;
import server.model.PlayerModel;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * MatchConfigurator è una classe necessaria per configurare il match in seguito alle
 * informazioni fornite dal creatore della stanza, e che evolveranno nel corso di tutti gli stati dell'automa.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class MatchConfigurator {
    private MatchModel matchModel;
    private ServerSocketManager connectionHandler;

    private final static Logger logger = Logger.getLogger(ServerSocketManager.class.getName());
    private final static String PLAYERS_CONNECTED_INFO = "SERVER -> TUTTI I GIOCATORI SONO CONNESSI, LA PARTITA PUÒ INIZIARE. \n";

    /**
     * Costruttore vuoto della classe MatchConfigurator.
     * Configura tutti i parametri del Match.
     */

    public MatchConfigurator(MatchModel matchModel){
        this.matchModel = matchModel;
        this.matchModel.resetPot();
        startRoomCreation();
        connectAllPlayers();
        configureMatch();
    }

    private void startRoomCreation() {
        CountDownLatch roomCreationSignal = new CountDownLatch(1);
        connectionHandler = new ServerSocketManager(roomCreationSignal);
        new Thread(connectionHandler).start();
        try {
            roomCreationSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connectAllPlayers(){
        int playerNumbers = connectionHandler.getMatchConfiguration().getMaxPlayers();
        CountDownLatch playersSignal = new CountDownLatch(playerNumbers - 1);
        connectionHandler.setCountdownForClients(playersSignal);
        try {
            playersSignal.await();
            logger.info(PLAYERS_CONNECTED_INFO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void configureMatch(){
        CreationRoomMessage roomMessage = connectionHandler.getMatchConfiguration();
        matchModel.setStartingChipAmount(roomMessage.getTotalChips());
        matchModel.setInitialBlinds();
        connectionHandler.getPlayersInformation()
                .forEach(message -> matchModel.getPlayers().add(new PlayerModel(message.getNickname(), message.getAvatarFilename())));
    }

    /**
     * Restituisce il gestore della connessione lato Server, utilizzato per configurare le connessioni con i
     * players in fase pre-partita.
     * @return Gestore delle connessioni.
     */

    public ServerSocketManager getConnectionHandler(){
        return connectionHandler;
    }
}
