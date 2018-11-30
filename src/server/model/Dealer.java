package server.model;

import server.model.cards.CardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dealer {
    private DeckModel deck;
    private Table table;
    private PotList potList;
    private BettingStructure bettingStructure;
    private CommunityModel communityModel;

    public Dealer(){
        deck = new DeckModel();
        communityModel = new CommunityModel();
        potList = new PotList();
        bettingStructure = new BettingStructure();
    }

    public void shuffleCards(){
        deck = new DeckModel();
    }

    public CardModel dealCard(){
        return deck.next();
    }

    public CardModel dealCommunityCard(){
        CardModel card = dealCard();
        communityModel.addCard(card);
        return card;
    }

    public void emptyCommunity() {
        communityModel.clear();
    }

    public void burnCard(){
        dealCard();
    }

    public void setTable(Table table){
        this.table = table;
    }

    public void givePotTo(String winner){
        table.getPlayerByName(winner).addChips(potList.totalValue());
        potList.removePots();
    }

    public void giveChipsToPotWinners(){
        List<String> activePlayers = Arrays.asList("Prova", "Prova2", "Prova3");
    }

    public void setStartChips(int startChips){
        bettingStructure.setStartChips(startChips);
    }

    public void setInitialBlinds(){
        bettingStructure.setInitialBlinds();
    }

    public void collectForcedBetFrom(PlayerModel player){
        if(player.getPosition() == Position.SB){
            if(player.getChips() < bettingStructure.getSmallBlind()) {
                potList.addWagerFor(player.getNickname(), player.getChips());
                player.decreaseChips(player.getChips());
            }else{
                potList.addWagerFor(player.getNickname(), bettingStructure.getSmallBlind());
                player.decreaseChips(bettingStructure.getSmallBlind());
            }
        }else{
            if(player.getChips() < bettingStructure.getBigBlind()) {
                potList.addWagerFor(player.getNickname(), player.getChips());
                player.decreaseChips(player.getChips());
            }else{
                potList.addWagerFor(player.getNickname(), bettingStructure.getBigBlind());
                player.decreaseChips(bettingStructure.getBigBlind());
            }
        }

        potList.descr();
    }

    public void collectAction(PlayerModel player, int value){
        if(value > 0) {
            player.decreaseChips(value);
            potList.addWagerFor(player.getNickname(), value);
            potList.descr();
        }
    }

    public boolean isBetPossible(ArrayList<PlayerModel> players){
        return potList.countDistinctBetsOf(players) <= 1;
    }

    public int getPotMatchingValue(String nickname){
        return potList.maxBetAmong(table.getPlayers(), bettingStructure.getBigBlind()) - potList.getTurnBetOf(nickname);
    }

    public void dealStartingChips(){
        table.getPlayers().forEach(player -> player.setChips(table.currentNumberOfPlayers() * 10000));
    }

    public void increaseBlinds(){
        bettingStructure.increaseBlinds();
    }

    public int getTurnBetOf(String nickname){
        return potList.getTurnBetOf(nickname);
    }

    public int maxBetAmong(ArrayList<PlayerModel> players){
        return potList.maxBetAmong(players, 0);
    }

    public int getPotValue(){
        return potList.totalValue();
    }

    public int getBigBlind(){
        return bettingStructure.getBigBlind();
    }

    public int getSmallBlind(){
        return bettingStructure.getSmallBlind();
    }

    public CommunityModel getCommunityModel(){
        return communityModel;
    }
}
