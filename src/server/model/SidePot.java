package server.model;

import java.util.HashMap;
import java.util.Iterator;

public class SidePot {
    private HashMap<String, Integer> wagers;

    public SidePot(){
        wagers = new HashMap<>();
    }

    public SidePot(HashMap<String, Integer> wagers){
        this.wagers = wagers;
    }

    public int getValue(){
        if(wagers.size() == 0)
            return 0;
        else{
            return wagers.values()
                    .stream()
                    .findFirst()
                    .get();
        }
    }

    public void addWagerFrom(String nickname, int wager){
        wagers.put(nickname, wager);
    }

    public SidePot copySidePotDecrementedBy(int value){
        HashMap<String, Integer> copy = new HashMap<>();
        Iterator<String> keys = wagers.keySet().iterator();
        String index;

        while(keys.hasNext()){
            index = keys.next();
            copy.put(index, wagers.get(index) - value);
            wagers.put(index, value);
        }

        return new SidePot(copy);
    }

    public boolean isPresent(String nickname){
        return wagers.containsKey(nickname);
    }

    public String toString(){
        return wagers.toString();
    }

    public int sum(){
        int sum = 0;
        Iterator<String> keys = wagers.keySet().iterator();
        while (keys.hasNext()){
            sum = sum + wagers.get(keys.next());
        }
        return sum;
    }

}
