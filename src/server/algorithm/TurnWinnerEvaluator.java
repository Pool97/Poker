package server.algorithm;

import server.model.PlayerModel;
import server.model.cards.CardModel;
import server.model.cards.CommunityModel;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnWinnerEvaluator {
    private ArrayList<PlayerModel> players;
    private CommunityModel communityCards;
    private HashMap<String, Integer> playersHand;
    private HashMap<String, String> playersHandByName;
    private HashMap<Integer, String> playersByName;
    private ArrayList<PokerHandsEvaluator> playerPoints;
    private ArrayList<String> winners;
    ArrayList<Integer> index = new ArrayList<>();

    /*public TurnWinnerEvaluator(ArrayList<PlayerModel> players, CommunityModel communityCards) {
        playersHand = new HashMap<>();
        playersByName = new HashMap<>();
        this.communityCards = communityCards;
        this.players = players;

    }*/

    public TurnWinnerEvaluator(ArrayList<PlayerModel> players, CommunityModel communityCards) {
        playerPoints = new ArrayList<>();
        playersHand = new HashMap<>();
        playersByName = new HashMap<>();
        playersHandByName = new HashMap<>();
        winners = new ArrayList<>();
        this.communityCards = communityCards;
        this.players = players;

    }

    public void evaluateTurnWinner(){
        for(int i = 0; i < players.size(); i++){
            checkWhoWin(communityCards.getCardsList(), players.get(i).getCards().get(0), players.get(i).getCards().get(1), players.get(i).getNickname(), i);
        }
        letsStart();
    }

    public ArrayList<PokerHandsEvaluator> checkWhoWin(ArrayList<CardModel> c, CardModel p1, CardModel p2, String playerName, int playerIndex){

        PokerHandsEvaluator algo = new PokerHandsEvaluator(c, p1, p2);
        algo.evaluate();
        playersHandByName.put(playerName, algo.getPlayerPointName());
        playersByName.put(playerIndex,playerName);
        playerPoints.add(algo);
        return playerPoints;
    }

    public void pareggio(ArrayList<PokerHandsEvaluator> algo, int checkFactor){
        index.add(0);

        ArrayList<ArrayList<CardModel>> tmpAlgo = new ArrayList<>();
        ArrayList<ArrayList<CardModel>> tmpAlgoCouples = new ArrayList<>();
        ArrayList<Integer> countC = new ArrayList<>();
        HashMap<ArrayList<CardModel>,Integer> hD = new HashMap<>();
        HashMap<ArrayList<CardModel>,Integer> hD2 = new HashMap<>();

        int tmpZ = 0;

        System.out.println("SIZE ALGO: "+algo.size());

        for(int i = 0; i < algo.size(); i++){
            tmpAlgo.add(algo.get(i).getCardsToExamine());
            System.out.println("FIRSTDECK  "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
            Utils.sortCards(tmpAlgo.get(i), true);
            System.out.println("SORTDECK: "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
            hD2.put(tmpAlgo.get(i), i);
            countC.add(Utils.couplesNumber(tmpAlgo.get(i)));
            if(countC.get(i) != 0)
                System.out.println("COUNTCOUPLES "+i+": "+ countC.get(i));

            if((Utils.getCouples(tmpAlgo.get(i)).size()) != 0){
                if(countC.get(i) == 3){
                    System.out.println("IN :"+Utils.getTris(tmpAlgo.get(i)).size());
                    tmpAlgoCouples.add(Utils.getTris(tmpAlgo.get(i)));
                    hD.put(tmpAlgoCouples.get(i), i);
                    System.out.println("COUPLES: " + tmpAlgoCouples.get(tmpZ).get(0).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(1).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(2).getValue());
                    tmpZ++;
                }else if(countC.get(i) == 2){
                    System.out.println("IN :"+Utils.getCouples(tmpAlgo.get(i)).size());
                    tmpAlgoCouples.add(Utils.getCouples(tmpAlgo.get(i)));
                    hD.put(tmpAlgoCouples.get(i), i);
                    System.out.println("COUPLES: " + tmpAlgoCouples.get(tmpZ).get(0).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(1).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(2).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(3).getValue());
                    tmpZ++;
                }else{
                    System.out.println("IN :"+Utils.getCouples(tmpAlgo.get(i)).size());
                    tmpAlgoCouples.add(Utils.getCouples(tmpAlgo.get(i)));
                    hD.put(tmpAlgoCouples.get(i), i);
                    System.out.println("COUPLES: " + tmpAlgoCouples.get(tmpZ).get(0).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(1).getValue());
                    tmpZ++;
                }
            }
        }

        if(checkFactor == 1){
            System.out.println("CHECK SOLO");
            checkSolo(tmpAlgo);
        }else if(checkFactor == 2){
            System.out.println("CHECK DUO");
            checkDuo(tmpAlgoCouples, tmpAlgo, hD);
        } else if(checkFactor == 3){
            System.out.println("CHECK DOUBLE");
            checkDouble(tmpAlgoCouples, tmpAlgo, hD);
        }else if(checkFactor == 4){
            System.out.println("CHECK TRIS");
            checkTris(tmpAlgoCouples, tmpAlgo, hD);
        }else if(checkFactor == 5){
            System.out.println("CHECK FIVE");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 6){
            System.out.println("CHECK SIX");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 7){
            System.out.println("CHECK SEVEN");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 8){
            System.out.println("CHECK EIGHT");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 9){
            System.out.println("CHECK NINE");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }

        letsStart();

    }
    int counterSame = 0;

    public void checkSolo(ArrayList<ArrayList<CardModel>> tmpAlgo){
        index.clear();
        HashMap<ArrayList<CardModel>,Integer> tmpHD = new HashMap<>();
        for( int i = 0; i < tmpAlgo.size(); i++){
            tmpHD.put(tmpAlgo.get(i), i);
        }

        ArrayList<CardModel> tmpHigh = tmpAlgo.get(0);
        Boolean high = true;
        for(int i = 1; i < tmpAlgo.size(); i++){
            if(tmpHigh.get(0).getValue().ordinal() < tmpAlgo.get(i).get(0).getValue().ordinal()){
                tmpHigh = tmpAlgo.get(i);
                high = true;
            }else if(tmpHigh.get(0).getValue().ordinal() == tmpAlgo.get(i).get(0).getValue().ordinal()){
                high = false;
                if(!index.contains(tmpHD.get(tmpHigh)))
                    index.add(tmpHD.get(tmpHigh));
                index.add(i);
            }
        }

        if (high == true){
            System.out.println(tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else{
            for (int i = 0; i < tmpAlgo.size(); i++){
                if(i != tmpAlgo.size()-1){
                    System.out.println("REMOVEDCARD "+i+": " + tmpAlgo.get(i).get(0).getValue());
                    tmpAlgo.set(i, Utils.removeCard(tmpAlgo.get(i), tmpAlgo.get(i).get(0)));
                    System.out.println("FIRST AFTER REMOVE FASE "+i+": " + tmpAlgo.get(i).get(0).getValue());
                }
            }
            checkSolo(tmpAlgo);
        }
    }

    public void checkDuo(ArrayList<ArrayList<CardModel>> tmpAlgoCouples, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<ArrayList<CardModel>, Integer> hD){
        index.clear();
        ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        Boolean high = true;
        for(int i = 1; i < tmpAlgoCouples.size(); i++){
            if(tmpHigh.get(0).getValue().ordinal() < tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                tmpHigh = tmpAlgoCouples.get(i);
                high = true;
            }else if(tmpHigh.get(0).getValue().ordinal() == tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                high = false;
                if(!index.contains(hD.get(tmpHigh)))
                    index.add(hD.get(tmpHigh));
                index.add(i);
            }
        }

        if (high == true){
            System.out.println(hD.get(tmpHigh));
            int tmpIndex = hD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else{
            checkSolo(tmpAlgo);
        }
    }

    public void checkDouble(ArrayList<ArrayList<CardModel>> tmpAlgoCouples, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<ArrayList<CardModel>, Integer> hD){
        index.clear();
        ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        Boolean high = true;
        for(int i = 1; i < tmpAlgoCouples.size(); i++){
            if(tmpHigh.get(0).getValue().ordinal() < tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                tmpHigh = tmpAlgoCouples.get(i);
                high = true;
            }else if(tmpHigh.get(0).getValue().ordinal() == tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                high = false;
                if(tmpHigh.get(2).getValue().ordinal() < tmpAlgoCouples.get(i).get(2).getValue().ordinal()){
                    tmpHigh = tmpAlgoCouples.get(i);
                    high = true;
                }else if(tmpHigh.get(2).getValue().ordinal() == tmpAlgoCouples.get(i).get(2).getValue().ordinal()){
                    high = false;
                    if(!index.contains(hD.get(tmpHigh)))
                        index.add(hD.get(tmpHigh));
                    index.add(i);
                }
            }
        }

        if (high == true){
            System.out.println(hD.get(tmpHigh));
            int tmpIndex = hD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else{
            checkSolo(tmpAlgo);
        }
    }

    public void checkTris(ArrayList<ArrayList<CardModel>> tmpAlgoCouples, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<ArrayList<CardModel>, Integer> hD){
        index.clear();
        ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        Boolean high = true;
        for(int i = 1; i < tmpAlgoCouples.size(); i++){
            if(tmpHigh.get(0).getValue().ordinal() < tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                tmpHigh = tmpAlgoCouples.get(i);
                high = true;
            }else if(tmpHigh.get(0).getValue().ordinal() == tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                high = false;
                if(tmpHigh.get(1).getValue().ordinal() < tmpAlgoCouples.get(i).get(1).getValue().ordinal()){
                    tmpHigh = tmpAlgoCouples.get(i);
                    high = true;
                }else if(tmpHigh.get(1).getValue().ordinal() == tmpAlgoCouples.get(i).get(1).getValue().ordinal()){
                    high = false;
                    if(tmpHigh.get(2).getValue().ordinal() < tmpAlgoCouples.get(i).get(2).getValue().ordinal()){
                        tmpHigh = tmpAlgoCouples.get(i);
                        high = true;
                    }else if(tmpHigh.get(2).getValue().ordinal() == tmpAlgoCouples.get(i).get(2).getValue().ordinal()){
                        high = false;
                        if(!index.contains(hD.get(tmpHigh)))
                            index.add(hD.get(tmpHigh));
                        index.add(i);
                    }
                }
            }
        }

        if (high == true){
            System.out.println(hD.get(tmpHigh));
            int tmpIndex = hD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }
    }

    public void letsStart(){

        ArrayList<PokerHandsEvaluator> algo = new ArrayList<>();
        PokerHandsEvaluator tmpFirstIndex = playerPoints.get(0);
        int[] tmpPP = Utils.riordina(playerPoints, playerPoints.size());
        int tmpWinnerIndex = 0;
        Boolean same = false;
        HashMap<Boolean,Integer> sameMap = new HashMap<Boolean, Integer>();
        //System.out.println("Player Name: "+playersByName.get(tmpWinnerIndex));
        for(int i = 0; i < tmpPP.length; i++){
            //System.out.println("indice:"+tmpPP[i]);
            if(i != tmpPP.length-1 && tmpPP[i] != 0){
                if(tmpPP[i] == tmpPP[i+1]) {
                    same = true;
                    sameMap.put(same,playerPoints.get(i).getPlayerPoint());
                }
            }

            if (i != 0) {
                if(tmpFirstIndex.getPlayerPoint() < playerPoints.get(i).getPlayerPoint()){
                    tmpFirstIndex = playerPoints.get(i);
                    tmpWinnerIndex = i;
                }else if(tmpFirstIndex.getPlayerPoint() == playerPoints.get(i).getPlayerPoint())
                    tmpWinnerIndex = -1;
            }

            //System.out.println("PlayePointVector: " + tmpPP[i]);
            //System.out.println("PlayePointArray: " + playerPoints.get(i).getPlayerPoint());
        }

        if(same == true && tmpWinnerIndex == -1){
            for(int i = 0; i < playerPoints.size(); i++)
                algo.add(playerPoints.get(i));
            if(index.isEmpty()) {
                //System.out.println(sameMap.get(same));
                pareggio(algo, sameMap.get(same));
            }else {
                System.out.print(index.size()+ " SPLIT BETWEEN ");
                for(int ind : index){
                    System.out.print(playersByName.get(ind)+" ");
                    winners.add(playersByName.get(ind));
                }
            }
            System.out.println("\nPAREGGIO");
        }else{
            System.out.println("VINCITORE: " + playersByName.get(tmpWinnerIndex));
            winners.add(playersByName.get(tmpWinnerIndex));
            System.out.println("VINCITORE");
        }
    }

    public String getPlayerHandByName(String nickname) {
        return playersHandByName.get(nickname);
    }

    public ArrayList<String> getWinners(){
        return winners;
    }
}
