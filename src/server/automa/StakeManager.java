package server.automa;

import interfaces.Strategy;
import server.BigBlindStrategy;
import server.NormalStrategy;
import server.SmallBlindStrategy;
import server.model.*;
import server.socket.ServerManager;

public class StakeManager {
    private TurnModel turnModel;
    private ServerManager serverHandler;
    private Strategy stakeStrategy;
    private int maxRaise;

    public StakeManager(TurnModel turnModel, ServerManager serverHandler) {
        this.turnModel = turnModel;
        this.serverHandler = serverHandler;
        this.maxRaise = 0;
    }

    public boolean startStake() {
        if (StakeState.PHASE == 0) {
            serverHandler.logger.info("Riscuoto SB e BB del turno...\n");
            setStrategy(new SmallBlindStrategy(turnModel, serverHandler));
            setMaxRaise(doAction());
            setStrategy(new BigBlindStrategy(turnModel, serverHandler));
            setMaxRaise(doAction());
        }

        Room room = serverHandler.getRoom();
        PositionManager manager = room.getAvailablePositions();
        Position nextPosition = manager.nextPosition(Position.BB);

        serverHandler.logger.info("Partono adesso le puntate non obbligatorie...\n");
        while ((nextPosition != Position.SB) && (onePlayerRemained() || isEquityReached())) {
            PlayerModel player = room.getPlayerByPosition(nextPosition);
            if (!player.hasFolded()) {
                setStrategy(new NormalStrategy(turnModel, serverHandler, player));
                setMaxRaise(doAction());
            }
            nextPosition = manager.nextPosition(nextPosition);
        }

        serverHandler.logger.info("La puntata massima è stata pareggiata, effettuo la transizione al Flop...\n");
        return isEquityReached();
    }

    public int doAction() {
        return stakeStrategy.doAction();
    }


    public void setStrategy(Strategy strategy) {
        this.stakeStrategy = strategy;
    }

    public void setMaxRaise(int raise) {
        maxRaise = raise > maxRaise ? raise : maxRaise;
    }

    public boolean isEquityReached() {
        return serverHandler.getRoom().getPlayers().stream()
                .filter(player -> !player.hasFolded())
                .noneMatch(player -> player.sumChipsBetted() != 200);
    }

    public boolean onePlayerRemained() {
        return serverHandler.getRoom().getPlayers()
                .stream()
                .filter(player -> !player.hasFolded())
                .count() == 1;
    }
}
