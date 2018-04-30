package events;

import interfaces.Event;
import javafx.util.Pair;
import server.model.ActionType;

import java.io.Serializable;
import java.util.ArrayList;

public class ActionOptionsEvent implements Serializable, Event {
    private ArrayList<Pair<ActionType, Integer>> optionsAvailable;

    public ActionOptionsEvent() {
        optionsAvailable = new ArrayList<>();
    }

    public void addOption(Pair<ActionType, Integer> option) {
        optionsAvailable.add(option);
    }

    public ArrayList<Pair<ActionType, Integer>> getOptions() {
        return optionsAvailable;
    }
}
