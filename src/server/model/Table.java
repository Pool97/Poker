package server.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Table {
    private ArrayList<PlayerModel> players;
    private ArrayList<PlayerModel> spectators;
    private PositionsHandler positionsHandler;
    private Dealer dealer;

    public Table(){
        players = new ArrayList<>();
        spectators = new ArrayList<>();
    }

    public void sit(PlayerModel player) {
        players.add(player);
    }

    public void refreshLists() {
        players.stream().filter(PlayerModel::hasLost).forEach(player -> spectators.add(player));
        players.removeIf(PlayerModel::hasLost);
        removeDisconnectedPlayers();
    }

    public int currentNumberOfPlayers() {
        return players.size();
    }

    public void sortPlayersByPosition() {
        players.sort(Comparator.comparingInt(client -> client.getPosition().ordinal()));
    }

    public ArrayList<PlayerModel> getPlayers() {
        return players;
    }

    public void movePlayersPosition() {
        players.forEach(player -> player.setPosition(positionsHandler.nextPosition(player.getPosition())));
    }

    public void removeDisconnectedPlayers() {
        players.removeIf(PlayerModel::isDisconnected);
    }

    public PlayerModel getPlayer(Position position) {
        return getPlayers().stream().filter(player -> player.getPosition() == position).findFirst().get();
    }

    public void assignInitialPositions() {
        positionsHandler = PositionsHandler.createPositions(currentNumberOfPlayers());
        for (int i = 0; i < currentNumberOfPlayers(); i++)
            players.get(i).setPosition(positionsHandler.getPositions().get(i));
    }

    public void removePosition() {
        positionsHandler.removePosition();
    }

    public Position getNextPosition(Position oldPosition) {
        return positionsHandler.nextPosition(oldPosition);
    }

    public boolean hasWinner() {
        return getPlayers().size() == 1;
    }

    public String getWinner() {
        return getPlayers().stream().findFirst().get().getNickname();
    }

    public void setDealer(Dealer dealer){
        this.dealer = dealer;
    }

    public Dealer getDealer(){
        return dealer;
    }

    public int getSpectatorsNumber(){
        return spectators.size();
    }

    public ArrayList<PlayerModel> getSpectators(){
        return spectators;
    }

    public PlayerModel getPlayerByName(String nickname){
       return players.stream().filter(player -> nickname.equals(player.getNickname())).findFirst().get();
    }

    public int getPotValue(){
        return dealer.getPotValue();
    }
}
