package server.model;

import java.util.ArrayList;
import java.util.List;

public class PotList {
    private List<SidePot> sidePots;

    public PotList(){
        sidePots = new ArrayList<>();
        sidePots.add(new SidePot());
    }

    public void addWagerFor(String nickname, int value){
        insertWagerIn(nickname, value, getLastPotIndexOf(nickname) + 1);
    }

    private void insertWagerIn(String nickname, int value, int index){
        if(index == sidePots.size())
            sidePots.add(new SidePot());

        SidePot pot = sidePots.get(index);
        if(value == pot.getValue()){
            pot.addWagerFrom(nickname, value);
        }

        if(value < pot.getValue()){
            sidePots.add(index, pot.copySidePotDecrementedBy(value));
            pot.addWagerFrom(nickname, value);
        }

        if(value > pot.getValue()  && pot.getValue() == 0){
            pot.addWagerFrom(nickname, value);
        }else if(value > pot.getValue()){
            pot.addWagerFrom(nickname, pot.getValue());
            if(index == sidePots.size() - 1)
                sidePots.add(new SidePot());
            insertWagerIn(nickname, value - pot.getValue(), index + 1);
        }
    }

    public int givePotValueBy(int index){
        return sidePots.get(index).getValue();
    }

    public boolean isNicknameInPot(int potIndex, String nickname){
        return sidePots.get(potIndex).isPresent(nickname);
    }

    public int getLastPotIndexOf(String nickname){
        SidePot lastPot = sidePots.stream()
                .filter(sidePot -> sidePot.isPresent(nickname))
                .reduce((first, second ) -> second)
                .orElse(null);

        return lastPot != null ? sidePots.indexOf(lastPot) : -1;
    }

    public int getTurnBetOf(String nickname){
        return sidePots.stream()
                .filter(sidePot -> sidePot.isPresent(nickname))
                .mapToInt(SidePot::getValue)
                .sum();
    }

    public int countDistinctBetsOf(ArrayList<PlayerModel> players){
        return (int)players.stream()
                .mapToInt(player -> getTurnBetOf(player.getNickname()))
                .distinct()
                .count();
    }

    public int maxBetAmong(ArrayList<PlayerModel> players, int limit){
        int maxBet = 0;
        if(players.size() != 0)
            maxBet = players.stream()
                .mapToInt(player -> getTurnBetOf(player.getNickname()))
                .max()
                .getAsInt();
        return maxBet < limit ? limit : maxBet;
    }

    public int totalValue(){
        return sidePots.stream().mapToInt(SidePot::sum).sum();
    }

    public void removePots(){
        sidePots.clear();
        sidePots.add(new SidePot());
    }

    public void descr(){
        sidePots.stream().forEach(pot -> System.out.println(pot.toString() + "\n\n"));
    }
}
