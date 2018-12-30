package server.algorithm;

import server.model.cards.CardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class HandEvaluator {
    private ArrayList<CardModel> cardsToExamine;
    private ArrayList<CardModel> finalCards;
    private List<PokerHand> algorithms;
    private PokerHand maxPlayerHand;
    private ArrayList<CardModel> playerCard;

    public HandEvaluator(ArrayList<CardModel> playerCards, CardModel... communityCards) {
        finalCards = new ArrayList<>();
        cardsToExamine = Arrays.stream(communityCards).collect(Collectors.toCollection(ArrayList::new));
        cardsToExamine.addAll(playerCards);
        playerCard = Arrays.stream(communityCards).collect(Collectors.toCollection(ArrayList::new));

        algorithms = Arrays.asList(new ScalaReale(), new ScalaColore(), new Colore(), new PokerOrTris(4),
                new Full(), new Scala(), new PokerOrTris(3), new DoppiaCoppia(), new Coppia(), new CartaPiuAlta());

    }

    public void evaluate() {
        for (PokerHand pokerHand : algorithms) {
            if (pokerHand.evaluate(cardsToExamine, finalCards)) {
                maxPlayerHand = pokerHand;
                break;
            }
        }
    }

    public ArrayList<CardModel> getPlayerCards(){ return playerCard; }

    public ArrayList<CardModel> getFinalCards() {
        return finalCards;
    }

    public ArrayList<CardModel> getCardsToExamine() {
        return cardsToExamine;
    }

    public int getPlayerPoint() {
        return maxPlayerHand.getPoint();
    }

    public void setPlayerPoint(int v) {
        maxPlayerHand.setPoint(v);
    }

    public void setFinalCards(ArrayList<CardModel> finalCards) {
        this.finalCards = finalCards;
    }

    public void setCardsToExamine(ArrayList<CardModel> c) {
        cardsToExamine = c;
    }

    public String getPlayerPointName() {
        return maxPlayerHand.toString();
    }
}