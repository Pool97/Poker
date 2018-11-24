package server.model;

import interfaces.*;
import interfaces.Observable;
import server.events.EventsContainer;
import server.model.actions.AllIn;
import server.model.actions.Bet;
import server.model.actions.DeadMoney;
import server.model.actions.Fold;
import server.model.cards.CardModel;

import java.io.Serializable;
import java.util.*;
import interfaces.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class PlayerModel implements Serializable, Cloneable, Observable {
    private String nickname;
    private Position position;
    private String avatar;
    private int chips;
    private boolean hasLost;
    private boolean isDisconnected;
    private boolean isCreator;
    private Set<Observer> observers;
    private ArrayList<CardModel> cards;
    private ArrayList<PokerAction> actions;
    private BlockingQueue<EventsContainer> readQueue;
    private BlockingQueue<EventsContainer> writeQueue;

    public PlayerModel(String nickname, String avatar, BlockingQueue<EventsContainer> readQueue, BlockingQueue<EventsContainer> writeQueue) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
        actions = new ArrayList<>();
        cards = new ArrayList<>();
        observers = new HashSet<>();

    }

    public PlayerModel(BlockingQueue<EventsContainer> readQueue, BlockingQueue<EventsContainer> writeQueue){
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
        actions = new ArrayList<>();
        cards = new ArrayList<>();
        observers = new HashSet<>();
    }
    public EventsContainer readMessage(){
        try {
            return readQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(EventsContainer event){
        try {
            writeQueue.put(event);
            notifyObservers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public void addChips(int chips) {
        this.chips += chips;
    }
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void addCard(CardModel card) {
        cards.add(card);
    }

    public ArrayList<CardModel> getCards() {
        return cards;
    }

    public void removeCards() {
        cards.clear();
    }

    public void removeActions() {
        actions.clear();
    }

    public boolean isAllIn() {
        return actions
                .stream()
                .anyMatch(action -> action instanceof AllIn);
    }

    public boolean hasFolded() {
        return actions.stream()
                .anyMatch(action -> action instanceof Fold);
    }

    public Stream<CardModel> cardsStream() {
        return cards.stream();
    }

    public int getTurnBet() {
        return actions.stream().mapToInt(PokerAction::getValue).sum();
    }

    public int getTurnBetWithoutDeadMoney() {
        return actions.stream().filter(action -> !(action instanceof DeadMoney)).mapToInt(PokerAction::getValue).sum();
    }

    public void addAction(PokerAction action) {
        if (chips == action.getValue())
            action = new AllIn(action.getValue());
        if (!(action instanceof DeadMoney)) {
            chips -= action.getValue();
            System.out.println("SONO ENTRATO QUI PERBACCO");
        }
        actions.add(action);
    }

    public boolean hasLost() {
        return hasLost;
    }

    public boolean hasBetted() {
        return actions.stream().anyMatch(action -> (action instanceof Bet));
    }

    public void setLost(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setDisconnected(boolean isDisconnected) {
        this.isDisconnected = isDisconnected;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean isCreator) {
        this.isCreator = isCreator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerModel that = (PlayerModel) o;
        return chips == that.chips &&
                Objects.equals(nickname, that.nickname) &&
                position == that.position &&
                Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, position, avatar, chips);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
