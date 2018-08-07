package server.model;

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

    public boolean equals(CardModel o) {
        if (o == null)
            return false;
        return key == o.key && value == o.value;
    }


    private void setImageDirectoryPath() {
        imageDirectoryPath = System.getProperty("user.dir") + "/res/" + "cards/" +
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
