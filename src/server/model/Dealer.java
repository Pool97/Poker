package server.model;

import server.model.cards.CardModel;

public class Dealer {
    private DeckModel deck;
    private Table table;
    private PotHandler potHandler;
    private GameType gameType;

    public Dealer(){
        deck = new DeckModel();
        deck.shuffle();
        gameType = new GameType();
    }

    public void shuffleCards(){
        deck.shuffle();
    }

    public CardModel getNextCard() {
        return deck.nextCard();
    }

    public void dealCards(){
        table.getPlayers().forEach(player -> player.addCard(getNextCard()));
        table.getPlayers().forEach(player -> player.addCard(getNextCard()));
    }

    public void dealCommunityCard(){
        table.showCommunityCards(getNextCard());
    }

    public void burnCard(){
        getNextCard();
    }

    public void setTable(Table table){
        this.table = table;
    }

    public void givePotTo(String winner){
        potHandler = new PotHandler(table.getPlayers());
        potHandler.assignPotsTo(winner);
    }

    public void setStartChips(int startChips){
        gameType.setStartChips(startChips);
    }

    public void setInitialBlinds(){
        gameType.setInitialBlinds();
    }

    public void collectSmallBlind(){
        //TODO
    }

    public void collectBigBlind(){
        //TODO
    }

    public void collectActionChips(){
        //TODO
    }

    public void dealStartingChips(){
        table.getPlayers().forEach(player -> player.setChips(table.getSize() * 10000));
    }

    public GameType getGameType(){
        return gameType;
    }
}
