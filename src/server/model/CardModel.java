package server.model;

import javafx.util.Pair;
import utils.Utils;

public class CardModel extends Pair<CardSuit, CardRank> {
    private String imageDirectoryPath;

    public CardModel(CardSuit key, CardRank value) {
        super(key, value);
        setImageDirectoryPath();
    }

    public boolean equals(Pair<CardSuit, CardRank> o) {
        if (!(o instanceof CardModel))
            return false;
        CardModel card = (CardModel) o;
        return getKey() == card.getKey() && getValue() == card.getValue();
    }

    private void setImageDirectoryPath() {
        imageDirectoryPath = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "cards/" +
                getValue().name().toLowerCase().concat("_") + getKey().name().toLowerCase().concat(".png");
    }

    public String getImageDirectoryPath() {
        return imageDirectoryPath;
    }
}
