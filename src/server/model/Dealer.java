package server.model;

import server.model.cards.CardModel;
import server.model.cards.CommunityModel;
import server.model.cards.DeckModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dealer {
    private DeckModel deck;
    private Table table;
    private PotList potList;
    private CommunityModel communityModel;
    private Map.Entry<String, Integer> minimumLegalRaise;

    public Dealer(){
        deck = new DeckModel();
        communityModel = new CommunityModel();
        potList = new PotList();
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

    public void setMinimumLegalRaise(Map.Entry<String, Integer> minimumLegalRaise){
        this.minimumLegalRaise = minimumLegalRaise;
    }

    public Map.Entry<String, Integer> getMinimumLegalRaise(){
        return minimumLegalRaise;
    }

    public void emptyPot(){
        potList.removePots();
    }

    public int collectForcedBetFrom(PlayerModel player, int forcedBet){
        int valueCollected = 0;
        if(player.getChips() < forcedBet) {
            potList.addWagerFor(player.getNickname(), player.getChips());
            valueCollected = player.getChips();
            player.decreaseChips(player.getChips());
        }else{
            potList.addWagerFor(player.getNickname(), forcedBet);
            player.decreaseChips(forcedBet);
            valueCollected = forcedBet;
        }
        potList.descr();

        return valueCollected;
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

    public int getPotMatchingValue(String nickname, int bigBlind){
        return potList.maxBetAmong(table.getPlayers(), bigBlind) - potList.getTurnBetOf(nickname);
    }

    public void dealStartingChips(){
        for (PlayerModel playerModel : table) playerModel.setChips(table.currentNumberOfPlayers() * 10000);
    }

    public ArrayList<String> playersInPot(int potIndex){
        return potList.getNicknamesInPot(potIndex);
    }

    public int totalSidePots(){
        return potList.size();
    }

    public int getTurnBetOf(String nickname){
        return potList.getTurnBetOf(nickname);
    }

    public int maxBetAmong(CopyOnWriteArrayList<PlayerModel> players){
        return potList.maxBetAmong(players, 0);
    }

    public int getPotValue(){
        return potList.totalValue();
    }

    public int getSidePotValue(int index){
        return potList.getSidePotValue(index);
    }

    public CommunityModel getCommunityModel(){
        return communityModel;
    }
}
