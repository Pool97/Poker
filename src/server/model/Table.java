package server.model;

import server.events.EventsContainer;
import server.model.cards.CardModel;

import java.util.ArrayList;
import java.util.Comparator;

public class Table {
    public ArrayList<PlayerModel> players;
    private ArrayList<PlayerModel> lostPlayers;
    private PositionsHandler positionsHandler;
    private CommunityModel community;
    private int pot;
    private Dealer dealer;

    public Table(){
        players = new ArrayList<>();
        lostPlayers = new ArrayList<>();
        community = new CommunityModel();
    }

    public void sit(PlayerModel player) {
        players.add(player);
    }

    public void refreshLists() {
        players.stream().filter(PlayerModel::hasLost).forEach(player -> lostPlayers.add(player));
        players.removeIf(PlayerModel::hasLost);
        removeDisconnectedPlayers();
    }

    public int getSize() {
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

    public void setPlayersChips(int chips) {
        players.forEach(client -> client.setChips(chips));
    }

    public void assignInitialPositions() {
        positionsHandler = PositionsHandler.createPositions(getSize());
        for (int i = 0; i < getSize(); i++)
            players.get(i).setPosition(positionsHandler.getPositions().get(i));
    }

    public void removePosition() {
        positionsHandler.removePosition();
    }

    public Position getNextPosition(Position oldPosition) {
        return positionsHandler.nextPosition(oldPosition);
    }

    public EventsContainer readMessage(PlayerModel playerModel){
        return players.stream().filter(player -> player.equals(playerModel)).findFirst().get().readMessage();
    }

    public void sendBroadcast(EventsContainer message) {
        players.stream().filter(player -> !player.isDisconnected()).forEach(player -> player.sendMessage(message));
    }

    public void sendBroadcastToLostPlayers(EventsContainer message) {
        lostPlayers.stream().filter(player -> !player.isDisconnected()).forEach(player -> player.sendMessage(message));
    }

    public boolean hasWinner() {
        return getPlayers().size() == 1;
    }

    public String getWinner() {
        return getPlayers().stream().findFirst().get().getNickname();
    }

    public CommunityModel getCommunityModel() {
        return community;
    }

    public void showCommunityCards(CardModel... cards) {
        community.addCards(cards);
    }

    public void emptyCommunity() {
        community.clear();
    }

    public int getPot() {
        return pot;
    }

    public void resetPot() {
        pot = 0;
    }

    public int increasePot(int quantity) {
        return pot += quantity;
    }

    public void setDealer(Dealer dealer){
        this.dealer = dealer;
    }

    public Dealer getDealer(){
        return dealer;
    }

}
