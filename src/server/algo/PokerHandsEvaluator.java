package server.algo;

import server.model.cards.CardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PokerHandsEvaluator {
    private ArrayList<CardModel> cardsToExamine;
    private ArrayList<CardModel> finalCards;
    private List<PokerHand> algorithms;
    private PokerHand maxPlayerHand;

    public PokerHandsEvaluator(ArrayList<CardModel> playerCards, CardModel... communityCards) {
        finalCards = new ArrayList<>();
        cardsToExamine = Arrays.stream(communityCards).collect(Collectors.toCollection(ArrayList::new));
        cardsToExamine.addAll(playerCards);

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

    public ArrayList<CardModel> getFinalCards() {
        return finalCards;
    }

    public ArrayList<CardModel> getCardsToExamine() {
        return cardsToExamine;
    }

    public int getPlayerPoint() {
        return maxPlayerHand.getPoint();
    }

    public String getPlayerPointName() {
        return maxPlayerHand.toString();
    }
}