package server.model;

import javafx.util.Pair;

public class Card extends Pair {
    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Card(Object key, Object value) {
        super(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Card))
            return false;
        Card card = (Card)o;
        if(getKey() == card.getKey() && getValue() == card.getValue())
            return true;
        return false;
    }
}
