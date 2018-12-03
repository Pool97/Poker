package server.model;

import server.algorithm.PokerHandsEvaluator;
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

        int tmpZ = 0;

        System.out.println("SIZE ALGO: "+algo.size());

        for(int i = 0; i < algo.size(); i++){
            tmpAlgo.add(algo.get(i).getCardsToExamine());
            System.out.println("FIRSTDECK  "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
            Utils.sortCards(tmpAlgo.get(i), true);
            System.out.println("SORTDECK: "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
            countC.add(Utils.couplesNumber(tmpAlgo.get(i)));
            if(countC.get(i) != 0)
                System.out.println("COUNTCOUPLES "+i+": "+ countC.get(i));

            if((Utils.getCouples(tmpAlgo.get(i)).size()) != 0){
                if(countC.get(i) == 3){
                    System.out.println("IN :"+Utils.getTris(tmpAlgo.get(i)).size());
                    tmpAlgoCouples.add(Utils.getTris(tmpAlgo.get(i)));
                    System.out.println("COUPLES: " + tmpAlgoCouples.get(tmpZ).get(0).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(1).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(2).getValue());
                    tmpZ++;
                }else if(countC.get(i) == 2){
                    System.out.println("IN :"+Utils.getCouples(tmpAlgo.get(i)).size());
                    tmpAlgoCouples.add(Utils.getCouples(tmpAlgo.get(i)));
                    System.out.println("COUPLES: " + tmpAlgoCouples.get(tmpZ).get(0).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(1).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(2).getValue() + " " + tmpAlgoCouples.get(tmpZ).get(3).getValue());
                    tmpZ++;
                }else{
                    System.out.println("IN :"+Utils.getCouples(tmpAlgo.get(i)).size());
                    tmpAlgoCouples.add(Utils.getCouples(tmpAlgo.get(i)));
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
            checkDuo(tmpAlgoCouples, tmpAlgo);
        }
        else if(checkFactor == 3){
            System.out.println("CHECK THREE");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 4){
            System.out.println("CHECK FOUR");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 5){
            System.out.println("CHECK FIVE");
            //checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 6){
            System.out.println("CHECK SIX");
            checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 7){
            System.out.println("CHECK SEVEN");
            checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 8){
            System.out.println("CHECK EIGHT");
            checkDuo(tmpAlgoCouples, tmpAlgo);
        }else if(checkFactor == 9){
            System.out.println("CHECK NINE");
            checkDuo(tmpAlgoCouples, tmpAlgo);
        }

        letsStart();

    }
    int counterSame = 0;

    public void checkSolo(ArrayList<ArrayList<CardModel>> tmpAlgo){

        ArrayList<CardModel> tmpH = tmpAlgo.get(0);
        int tmpWinnerIndice1 = 0;
        Boolean same = false;

        for(int i = 0; i < tmpAlgo.size(); i++){
            int tmpC1 = tmpAlgo.get(i).get(0).getValue().ordinal();
            if(i != tmpAlgo.size()-1 && tmpC1 != 0){
                int tmpC2 = tmpAlgo.get(i+1).get(0).getValue().ordinal();
                if(tmpC1 == tmpC2) {
                    same = true;
                    counterSame++;
                    if(counterSame%7 == 0){
                        index.set(i,i);
                        index.add(i+1);
                    }
                }

                if(tmpH.get(0).getValue().ordinal() < tmpC2){
                    tmpH = tmpAlgo.get(i+1);
                    System.out.println(tmpH.get(0).getValue());
                    tmpWinnerIndice1 = i+1;
                }else if(tmpH.get(0).getValue().ordinal() == tmpC2)
                    tmpWinnerIndice1 = -1;
            }

            //winnerIndice(tmpH, tmpAlgo, i, tmpC1, tmpWinnerIndice1);
            //System.out.println("PlayePointVector: " + tmpPP[i]);
            //System.out.println("PlayePointArray: " + playerPoints.get(i).getPlayerPoint());
        }
        //System.out.println("SOLOCARD: "+tmpC + " " + tmpI);
        if(same == true && tmpWinnerIndice1 == -1){
            for (int i = 0; i < tmpAlgo.size(); i++){
                tmpAlgo.set(i, Utils.removeCard(tmpAlgo.get(i), tmpAlgo.get(i).get(0)));
                //System.out.println("REMOVEDCARD "+i+": " + tmpAlgo.get(i).get(0).getValue());
                playerPoints.get(i).setCardsToExamine(tmpAlgo.get(i));
            }
            if(counterSame%7 != 0)
                checkSolo(tmpAlgo);
        }else{
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpWinnerIndice1)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }
        //System.out.println(tmpCardsPlayer1.get(0).getValue().ordinal()+ " "+tmpCardsPlayer1.get(0).getValue());
        //System.out.println(tmpCardsPlayer2.get(0).getValue().ordinal()+ " "+tmpCardsPlayer2.get(0).getValue());
    }

    public void checkDuo(ArrayList<ArrayList<CardModel>> tmpAlgoCouples, ArrayList<ArrayList<CardModel>> tmpAlgo){

        for (int i = 0; i < tmpAlgoCouples.size(); i++){
            //Utils.sortCards(tmpAlgoCouples.get(i), true);
            System.out.println("SIZE:"+tmpAlgoCouples.size()+" "+tmpAlgoCouples.get(i).get(0).getValue().ordinal()+ " "+tmpAlgoCouples.get(i).get(0).getValue());
        }
        ArrayList<CardModel> tmpH = tmpAlgoCouples.get(0);
        int tmpWinnerIndice2 = findTheIndice(tmpAlgo, tmpH);

        ArrayList<Integer> eTndex = new ArrayList<>();
        eTndex.add(findTheIndice(tmpAlgo, tmpH));
        Boolean same = false;
        //System.out.println(tmpH.get(1).getValue());

        for(int i = 0; i < tmpAlgoCouples.size(); i++){
            int tmpC1 = tmpH.get(0).getValue().ordinal();
            if(i != tmpAlgoCouples.size()-1 &&  tmpC1 != 0){
                int tmpC2 = tmpAlgoCouples.get(i+1).get(0).getValue().ordinal();
                if(tmpC1 == tmpC2) {
                    same = true;
                    eTndex.set(0,findTheIndice(tmpAlgo, tmpH));
                    eTndex.add(i+1);
                }

                //System.out.println(tmpH.get(1).getValue());
                if(tmpH.get(0).getValue().ordinal() < tmpAlgoCouples.get(i+1).get(0).getValue().ordinal()){
                    tmpH = tmpAlgoCouples.get(i+1);
                    //System.out.println(tmpH.get(0).getValue());
                    tmpWinnerIndice2 = findTheIndice(tmpAlgo, tmpH);
                }else if(tmpH.get(0).getValue().ordinal() == tmpAlgoCouples.get(i+1).get(0).getValue().ordinal())
                    tmpWinnerIndice2 = -1;
            }
        }

        ArrayList<ArrayList<CardModel>> tmpA = new ArrayList<>();
        int k=0;
        if(same == true && tmpWinnerIndice2 == -1){
            for (int i = 0; i < tmpAlgo.size(); i++){
                tmpA.add(tmpAlgo.get(i));
                System.out.println("indice: "+eTndex.get(k));
                if(eTndex.get(k) == i){
                    //tmpA.add(tmpAlgo.get(i));
                    tmpA.set(i,Utils.removeCard(tmpA.get(i), tmpAlgoCouples.get(i).get(0)));
                    //System.out.println("REMOVEDCARD "+i+": " + tmpA.get(i).get(0).getValue());
                    if(k < eTndex.size()-1)
                        k++;
                }
            }
            ArrayList<CardModel> tmpC = tmpA.get(0);
            index.set(0,eTndex.get(0));
            for(k=0; k < eTndex.size(); k++) {
                System.out.println(eTndex.get(k));
                for (int i = 0; i < 5; i++) {
                    if(k < eTndex.size()-1){
                        System.out.println(tmpC.get(i).getValue() + " " + tmpA.get(eTndex.get(k + 1)).get(i).getValue());
                        if (tmpC.get(i).getValue().ordinal() < tmpA.get(eTndex.get(k + 1)).get(i).getValue().ordinal()) {
                            playerPoints.get(findTheIndice(tmpAlgo, tmpC)).setPlayerPoint(0);
                            tmpC = tmpA.get(eTndex.get(k + 1));
                        }else if (tmpC.get(i).getValue().ordinal() > tmpA.get(eTndex.get(k + 1)).get(i).getValue().ordinal()) {
                            playerPoints.get(eTndex.get(k+1)).setPlayerPoint(0);
                        }else {

                            //System.out.println("WIP");
                            if (i == 4){
                                //add(eTndex)
                                //index.set(k, eTndex.get(k));
                                index.add(eTndex.get(k+1));
                            }
                        }

                    }
                }
            }
        }else{
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpWinnerIndice2)
                    playerPoints.get(i).setPlayerPoint(0);
            }

        }
    }

    public int findTheIndice(ArrayList<ArrayList<CardModel>> d, ArrayList<CardModel> c){
        int counter = 0;
        for(int i = 0; i < d.size(); i++){
            for (int j = 0; j < 7; j++){
                System.out.println(counter);
                if(d.get(i).get(j).getValue().ordinal() == c.get(0).getValue().ordinal())
                    counter++;
                else
                    counter = 0;

                if(counter >= 2)
                    return i;
            }
        }
        return 0;
    }

    /*public String evaluateTurnWinner() {
        ArrayList<PlayerModel> inGamePlayers = players.stream()
                .filter(playerModel -> !playerModel.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));

        for (PlayerModel player : inGamePlayers) {
            PokerHandsEvaluator evaluator = new PokerHandsEvaluator(player.getCards(), communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4));
            evaluator.evaluate();

            playersHand.put(player.getNickname(), evaluator.getPlayerPoint());
            playersByName.put(player.getNickname(), evaluator.getPlayerPointName());
        }

        return playersHand.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
    }*/

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
                    winners.add(players.get(ind).getNickname());
                }
            }
            System.out.println("\nPAREGGIO");
        }else{
            winners.add(players.get(0).getNickname());
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
