package server.model.cards;

import java.io.Serializable;

public class CardModel implements Serializable {
    private String imageDirectoryPath;
    protected CardRank value;
    private CardSuit key;

    public CardModel(CardSuit key, CardRank value) {
        this.key = key;
        this.value = value;
        setImageDirectoryPath();
    }

    private void setImageDirectoryPath() {
        imageDirectoryPath = "/res/" + "cards/" +
                getValue().name().toLowerCase().concat("_") + key.name().toLowerCase().concat(".png");
    }

    public String getImageDirectoryPath() {
        return imageDirectoryPath;
    }

    public CardRank getValue() {
        return value;
    }

    public CardSuit getKey() {
        return key;
    }

    public CardModel setValue() {
        if (value == CardRank.ACE || value == CardRank.ACE_MAX)
            value = value == CardRank.ACE_MAX ? CardRank.ACE : CardRank.ACE_MAX;
        return this;
    }
}
