package server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Table implements Iterable<PlayerModel> {
    private ArrayList<PlayerModel> players;
    private ArrayList<PlayerModel> spectators;
    private Dealer dealer;

    public Table(){
        players = new ArrayList<>();
        spectators = new ArrayList<>();
    }

    public void sit(PlayerModel player) {
        players.add(player);
    }

    public int size(){
        return players.size();
    }

    public void refreshLists() {
        players.stream().filter(PlayerModel::hasLost).forEach(player -> spectators.add(player));
        players.removeIf(PlayerModel::hasLost);
        removeDisconnectedPlayers();
    }

    public int currentNumberOfPlayers() {
        return players.size();
    }

    public ArrayList<PlayerModel> getPlayers() {
        return players;
    }

    public void removeDisconnectedPlayers() {
        players.removeIf(PlayerModel::isDisconnected);
    }

    public boolean hasWinner() {
        return size() == 1;
    }

    public String getWinner() {
        return players.stream().findFirst().get().getNickname();
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

    public String getLastPlayerInGame(){
        if(countPlayersInGame() != 1){
            throw new RuntimeException("Acciderbolina non c'è ne solo uno");
        }
        return getPlayersInGame().get(0).getNickname();

    }

    public ArrayList<PlayerModel> getAllInPlayers(){
        return getPlayersInGame().stream()
                .filter(PlayerModel::isAllIn)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayerModel> getPlayersInGame(){
        return players
                .stream()
                .filter(playerModel -> !playerModel.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayerModel> getActivePlayers() {
        return getPlayersInGame().stream()
                .filter(playerModel -> !playerModel.isAllIn())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int countPlayersInGame(){
        return getPlayersInGame().size();
    }

    public boolean isAllPlayersAllIn() {
        return getActivePlayers().size() == 0 && countPlayersAllIn() >= 1;
    }

    public int countPlayersAllIn(){
        return getAllInPlayers().size();
    }

    public int countActivePlayers() {
        return getActivePlayers().size();
    }

    public int getPotValue(){
        return dealer.getPotValue();
    }

    public void translatePositions(){
        Collections.rotate(players, 1);
    }



    @Override
    public ListIterator<PlayerModel> iterator() {
        return new TableIterator();
    }

    public ListIterator<PlayerModel> iterator(int index){
        return new TableIterator(index);
    }


    public class TableIterator implements ListIterator<PlayerModel>{
        private int cursor;

        public TableIterator(){
            cursor = -1;
        }

        public TableIterator(int index){
            if(index < 0 || index > players.size() - 1)
                cursor = -1;
            else
                this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < players.size() - 1;
        }

        @Override
        public PlayerModel next() {
            cursor++;
            return players.get(cursor);
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public PlayerModel previous() {
            return players.get(cursor);
        }

        public int nextIndex(){
            return cursor + 1;
        }

        @Override
        public int previousIndex() {
            return cursor;
        }

        @Override
        public void remove() {
            players.remove(cursor);
        }

        @Override
        public void set(PlayerModel playerModel) {
            players.set(cursor, playerModel);
        }

        @Override
        public void add(PlayerModel playerModel) {
            players.add(cursor, playerModel);
        }


    }
}
