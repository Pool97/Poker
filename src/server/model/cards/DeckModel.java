package server.model.cards;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DeckModel implements Iterator<CardModel>{
    private ArrayList<CardModel> cardsList;
    private int index = 0;

    public DeckModel(){
        cardsList = new ArrayList<>();
        createCardsList();
        shuffle();
    }

    private void createCardsList(){
        for (int i = 0; i < 13; ++i)
            for (int j = 0; j < 4; ++j)
                cardsList.add(new CardModel(CardSuit.values()[j], CardRank.values()[i]));
    }

    public void shuffle(){
        Collections.shuffle(cardsList);
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < cardsList.size();
    }

    @Override
    public CardModel next() {
        CardModel cardModel = cardsList.get(index);
        index++;
        return cardModel;
    }
}
