package server.algorithm;

import server.model.PlayerModel;
import server.model.cards.CardModel;
import server.model.cards.CommunityModel;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TurnWinnerEvaluator {
    private ArrayList<PlayerModel> players;
    private CommunityModel communityCards;
    private HashMap<String, String> playersHandByName;
    private HashMap<Integer, String> playersByName;
    private ArrayList<PokerHandsEvaluator> playerPoints;
    private ArrayList<String> winners;
    ArrayList<Integer> index = new ArrayList<>();

    public TurnWinnerEvaluator(ArrayList<PlayerModel> players, CommunityModel communityCards) {
        playerPoints = new ArrayList<>();
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
        HashMap<PokerHandsEvaluator, Integer> tmpHD = new HashMap<>();

        int tmpZ = 0;

        System.out.println("SIZE ALGO: "+algo.size());

        for(int i = 0; i < algo.size(); i++){

            tmpHD.put(algo.get(i), i);

            //System.out.println(algo.get(i).getPlayerCards().size());
            tmpAlgo.add(algo.get(i).getCardsToExamine());
            System.out.println("FIRSTDECK  "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
            Utils.sortCards(tmpAlgo.get(i), true);
            System.out.println("SORTDECK: "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
            hD2.put(tmpAlgo.get(i), i);
            countC.add(Utils.couplesNumber(tmpAlgo.get(i)));
            if(countC.get(i) != 0)
                System.out.println("COUNTCOUPLES "+i+": "+ countC.get(i));

        }

        if(checkFactor == 1)
            checkHigherCard(tmpAlgo);
        else if(checkFactor == 2)
            checkOnePair(algo, tmpAlgo, tmpHD);
        else if(checkFactor == 3)
            checkTwoPair(algo, tmpAlgo, tmpHD);
        else if(checkFactor == 4)
            checkThreeOfAKind(algo, tmpAlgo, tmpHD);
        else if(checkFactor == 5)
            checkStraight(algo, tmpHD);
        else if(checkFactor == 6)
            checkFlush_StraightFlush(algo, tmpHD);
        else if(checkFactor == 7)
            checkFullHouse(algo, tmpHD);
        else if(checkFactor == 8){
            checkFourOfAKind(algo, tmpAlgo, tmpHD);
        }else if(checkFactor == 9){
            checkFlush_StraightFlush(algo, tmpHD);
        }else if(checkFactor == 10){
            System.out.println("CHECK ROYAL FLUSH");
            checkRoyalFlush(algo, tmpHD);
        }

        letsStart();

    }

    public void letsStart(){
        System.out.println("CHECK FASE");
        ArrayList<PokerHandsEvaluator> algo = new ArrayList<>();
        PokerHandsEvaluator tmpFirstIndex = playerPoints.get(0);
        int[] tmpPP = Utils.riordina(playerPoints, playerPoints.size());
        int tmpWinnerIndex = 0;
        Boolean same = false;
        ArrayList<Integer> tmpPPnt = new ArrayList<>();
        for(int i = 0; i < tmpPP.length; i++){
            System.out.println("indice o if i:"+i+" "+tmpPP[i]+ " " + playerPoints.get(i).getPlayerPoint());
            if(i != tmpPP.length-1 && tmpPP[i] != 0){
                if(tmpPP[i] == tmpPP[i+1]) {
                    same = true;
                    tmpPPnt.add(tmpPP[i]);
                }
            }

            if (i != 0) {
                if(tmpFirstIndex.getPlayerPoint() < playerPoints.get(i).getPlayerPoint()){
                    tmpFirstIndex = playerPoints.get(i);
                    tmpWinnerIndex = i;
                }else if(tmpFirstIndex.getPlayerPoint() == playerPoints.get(i).getPlayerPoint())
                    tmpWinnerIndex = -1;
            }
        }

        if(same == true && tmpWinnerIndex == -1){

            for(int i = 0; i < playerPoints.size(); i++)
                algo.add(playerPoints.get(i));

            if(index.isEmpty()) {
                Collections.sort(tmpPPnt, Collections.reverseOrder());
                System.out.println("SAME: "+tmpPPnt.get(0));
                pareggio(algo, tmpPPnt.get(0));
            }else {
                System.out.print(index.size()+ " SPLIT BETWEEN ");
                for(int ind : index){
                    winners.add(playersByName.get(ind));
                }
            }
            System.out.println("\nPAREGGIO");
        }else{
            winners.add(playersByName.get(tmpWinnerIndex));
            System.out.println("VINCITORE");
        }
    }

    int counterSame = 0;

    public void checkHigherCard(ArrayList<ArrayList<CardModel>> tmpAlgo){
        System.out.println("CHECK HIGHER CARD");
        System.out.println("SIZE: " + tmpAlgo.size());
        HashMap<ArrayList<CardModel>,Integer> tmpHD = new HashMap<>();
        for( int i = 0; i < tmpAlgo.size(); i++){
            tmpHD.put(tmpAlgo.get(i), i);
        }

        ArrayList<CardModel> tmpHigh;
        boolean high = true;
        if(index.isEmpty()){
            tmpHigh = tmpAlgo.get(0);
            for(int i = 1; i < tmpAlgo.size(); i++){
                System.out.println("EMPTY INDEX -> CARD " + 0 + ": " + tmpHigh.get(0).getValue() + " CARD " + i + ": " + tmpAlgo.get(i).get(0).getValue());
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
        }else{
            tmpHigh = tmpAlgo.get(index.get(0));
            for (int i = 1; i < index.size(); i++){
                System.out.println("INDEX SIZE: " + index.size());
                System.out.println("INDEX -> CARD " + 0 + ": " + tmpHigh.get(0).getValue() + " CARD " + index.get(i) + ": " + tmpAlgo.get(index.get(i)).get(0).getValue());
                if(tmpHigh.get(0).getValue().ordinal() < tmpAlgo.get(index.get(i)).get(0).getValue().ordinal()){
                    tmpHigh = tmpAlgo.get(index.get(i));
                    high = true;
                }else if(tmpHigh.get(0).getValue().ordinal() == tmpAlgo.get(index.get(i)).get(0).getValue().ordinal()){
                    high = false;
                    if(!index.contains(tmpHD.get(tmpHigh)) && !index.contains(index.get(i))){
                        index.add(tmpHD.get(tmpHigh));
                        index.add(index.get(i));
                    }

                }
            }
        }


        if (high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else{
            int tmpCheck = 0;
            for (int i = 0; i < tmpAlgo.size(); i++){
                System.out.println("Size: " + tmpAlgo.size() + " " + tmpAlgo.get(i).size());
                System.out.println("REMOVEDCARD " + i + ": " + tmpAlgo.get(i).get(0).getValue());
                if(Utils.removeCard(tmpAlgo.get(i), tmpAlgo.get(i).get(0)).size() != 0) {
                    tmpAlgo.set(i, Utils.removeCard(tmpAlgo.get(i), tmpAlgo.get(i).get(0)));
                    System.out.println("FIRST AFTER REMOVE FASE " + i + ": " + tmpAlgo.get(i).get(0).getValue());
                }else
                    tmpCheck++;
            }
            if(tmpCheck != tmpAlgo.size())
                checkHigherCard(tmpAlgo);

        }
    }

    public void checkOnePair(ArrayList<PokerHandsEvaluator> algo, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK ONE PAIR");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        //ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        boolean high = true;
        for(int i = 1; i < algo.size(); i++){
            System.out.println("Tmp SIZE: "+tmpHigh.getFinalCards().size()+" "+"Algo SIZE: "+algo.get(i).getFinalCards().size());
            if(tmpHigh.getFinalCards().size() == 2 && algo.get(i).getFinalCards().size() == 2){
                if(tmpHigh.getFinalCards().get(0).getValue().ordinal() < algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    tmpHigh = algo.get(i);
                    high = true;
                }else if(tmpHigh.getFinalCards().get(0).getValue().ordinal() == algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    high = false;
                    if(!index.contains(tmpHD.get(tmpHigh)))
                        index.add(tmpHD.get(tmpHigh));
                    index.add(i);
                }
            }else if(algo.get(i).getFinalCards().size() == 2){
                tmpHigh = algo.get(i);
            }
        }

        for(int i = 0; i < index.size(); i++){
            System.out.println("INDEX: "+index.get(i));
        }

        if (high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else{
            checkHigherCard(tmpAlgo);
        }
    }

    public void checkTwoPair(ArrayList<PokerHandsEvaluator> algo, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK TWO PAIR");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        //ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        boolean high = true;
        for(int i = 1; i < algo.size(); i++){

            if(tmpHigh.getFinalCards().size() >= 4 && algo.get(i).getFinalCards().size() >= 4 ){
                System.out.println("TWO PAIR FIRST-> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue() +  " " + algo.get(i).getFinalCards().size());
                if(tmpHigh.getFinalCards().get(0).getValue().ordinal() < algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    tmpHigh = algo.get(i);
                    high = true;
                }else if(tmpHigh.getFinalCards().get(0).getValue().ordinal() == algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    System.out.println("TWO PAIR SECOND-> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(2).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(2).getValue());
                    if(tmpHigh.getFinalCards().get(2).getValue().ordinal() < algo.get(i).getFinalCards().get(2).getValue().ordinal()){
                        tmpHigh = algo.get(i);
                        high = true;
                    }else if(tmpHigh.getFinalCards().get(2).getValue().ordinal() == algo.get(i).getFinalCards().get(2).getValue().ordinal()){
                        high = false;
                        if(!index.contains(tmpHD.get(tmpHigh)))
                            index.add(tmpHD.get(tmpHigh));
                        index.add(i);
                    }
                }
            }else if(algo.get(i).getFinalCards().size() == 4){
                tmpHigh = algo.get(i);
            }
        }

        if (high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else{
            checkHigherCard(tmpAlgo);
        }
    }

    public void checkThreeOfAKind(ArrayList<PokerHandsEvaluator> algo, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK THREE OF A KIND");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        //ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        boolean high = true;
        for(int i = 1; i < algo.size(); i++){
            if(tmpHigh.getFinalCards().size() >= 3 && algo.get(i).getFinalCards().size() >= 3){
                System.out.println("THREE OF A KIND -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
                if(tmpHigh.getFinalCards().get(0).getValue().ordinal() < algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    tmpHigh = algo.get(i);
                    high = true;
                }else if(tmpHigh.getFinalCards().get(0).getValue().ordinal() == algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    high = false;
                    if(!index.contains(tmpHD.get(tmpHigh)))
                        index.add(tmpHD.get(tmpHigh));
                    index.add(i);
                }
            }else if(algo.get(i).getFinalCards().size() == 3){
                tmpHigh = algo.get(i);
            }

        }

        if (high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else
            checkHigherCard(tmpAlgo);
    }

    public void checkStraight(ArrayList<PokerHandsEvaluator> algo,  HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK STRAIGHT");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        boolean high = true;

        for (int i = 1; i < algo.size(); i++){
            if(tmpHigh.getFinalCards().size() == 5 && algo.get(i).getFinalCards().size() == 5) {
                if (tmpHigh.getFinalCards().get(0).getValue().ordinal() < algo.get(i).getFinalCards().get(0).getValue().ordinal()) {
                    tmpHigh = algo.get(i);
                    high = true;
                }else if(tmpHigh.getFinalCards().get(0).getValue().ordinal() == algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                    if(contiene(tmpHigh, 0) && contiene(algo.get(i), 0) && contiene(tmpHigh, 1) && contiene(algo.get(i), 1)){
                        if(!index.contains(tmpHD.get(tmpHigh)))
                            index.add(tmpHD.get(tmpHigh));
                        index.add(i);
                        high = false;
                    }else if(!contiene(tmpHigh, 0 )  && !contiene(algo.get(i), 0 ) ){
                        System.out.println("Non contiene la prima carta del player");
                        if(tmpHigh.getPlayerCards().get(0).getValue().ordinal() < algo.get(i).getPlayerCards().get(0).getValue().ordinal()) {
                            tmpHigh = algo.get(i);
                            high = true;
                        }else if(tmpHigh.getPlayerCards().get(0).getValue().ordinal() == algo.get(i).getPlayerCards().get(0).getValue().ordinal()){
                            if(!index.contains(tmpHD.get(tmpHigh)))
                                index.add(tmpHD.get(tmpHigh));
                            index.add(i);
                            high = false;
                        }
                    }else if(!contiene(tmpHigh, 1 )  && !contiene(algo.get(i), 1 ) ){
                        System.out.println("Non contiene la seconda carta del player");
                        if(tmpHigh.getPlayerCards().get(1).getValue().ordinal() < algo.get(i).getPlayerCards().get(1).getValue().ordinal()){
                            tmpHigh = algo.get(i);
                            high = true;
                        }else if(tmpHigh.getPlayerCards().get(1).getValue().ordinal() == algo.get(i).getPlayerCards().get(1).getValue().ordinal()){
                            if(!index.contains(tmpHD.get(tmpHigh)))
                                index.add(tmpHD.get(tmpHigh));
                            index.add(i);
                            high = false;
                        }
                    }else{
                        System.out.println("Non contiene la prima o la seconda carta del player");
                        if(contiene(tmpHigh, 0)){
                            if(tmpHigh.getPlayerCards().get(1).getValue().ordinal() < algo.get(i).getPlayerCards().get(0).getValue().ordinal()){
                                tmpHigh = algo.get(i);
                                high = true;
                            }else if(tmpHigh.getPlayerCards().get(1).getValue().ordinal() == algo.get(i).getPlayerCards().get(0).getValue().ordinal()){
                                if(!index.contains(tmpHD.get(tmpHigh)))
                                    index.add(tmpHD.get(tmpHigh));
                                index.add(i);
                                high = false;
                            }
                        }else if(contiene(tmpHigh, 1)){
                            if(tmpHigh.getPlayerCards().get(0).getValue().ordinal() < algo.get(i).getPlayerCards().get(1).getValue().ordinal()){
                                tmpHigh = algo.get(i);
                                high = true;
                            }else if(tmpHigh.getPlayerCards().get(0).getValue().ordinal() == algo.get(i).getPlayerCards().get(1).getValue().ordinal()){
                                if(!index.contains(tmpHD.get(tmpHigh)))
                                    index.add(tmpHD.get(tmpHigh));
                                index.add(i);
                                high = false;
                            }
                        }
                    }
                }
            }else if(algo.get(i).getFinalCards().size() == 5){
                tmpHigh = algo.get(i);
            }
        }

        if(high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < algo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }
    }

    public void checkFlush_StraightFlush(ArrayList<PokerHandsEvaluator> algo, HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK FLUSH & STRAIGHT FLUSH");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        boolean high = true;
        int counterEq = 0;

        for(int i = 1; i < algo.size(); i++){
            System.out.println("F & SF -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
            if(tmpHigh.getFinalCards().size() >= 5 && algo.get(i).getFinalCards().size() >= 5) {
                System.out.println("F & SF -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
                if(contiene(tmpHigh, 0) && contiene(algo.get(i), 0) && contiene(tmpHigh, 1) && contiene(algo.get(i), 1)) {
                    System.out.println("F & SF -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
                    for (int j = 0; j < 5; j++) {
                        if (tmpHigh.getFinalCards().get(j).getValue().ordinal() < algo.get(i).getFinalCards().get(j).getValue().ordinal()) {
                            tmpHigh = algo.get(i);
                            high = true;
                        } else if (tmpHigh.getFinalCards().get(j).getValue().ordinal() == algo.get(i).getFinalCards().get(j).getValue().ordinal()) {
                            counterEq++;
                            if (counterEq == 5) {
                                if (!index.contains(tmpHD.get(tmpHigh)))
                                    index.add(tmpHD.get(tmpHigh));
                                index.add(i);
                                high = false;
                            }
                        }
                    }
                }else if(!contiene(tmpHigh, 0) && !contiene(algo.get(i), 0) && !contiene(tmpHigh, 1) && !contiene(algo.get(i), 1)) {
                    System.out.println("F & SF  -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
                    if(higher(tmpHigh).get(0).getValue().ordinal() < higher(algo.get(i)).get(0).getValue().ordinal()){
                        tmpHigh = algo.get(i);
                        high = true;
                    }else if(higher(tmpHigh).get(0).getValue().ordinal() == higher(algo.get(i)).get(0).getValue().ordinal()){
                        if(higher(tmpHigh).get(1).getValue().ordinal() < higher(algo.get(i)).get(1).getValue().ordinal()){
                            tmpHigh = algo.get(i);
                            high = true;
                        }else if(higher(tmpHigh).get(1).getValue().ordinal() == higher(algo.get(i)).get(1).getValue().ordinal()){
                            if (!index.contains(tmpHD.get(tmpHigh)))
                                index.add(tmpHD.get(tmpHigh));
                            index.add(i);
                            high = false;
                        }
                    }
                }else{
                    System.out.println("F & SF  -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
                    for (int j = 0; j < 5; j++) {
                        if (tmpHigh.getFinalCards().get(j).getValue().ordinal() < algo.get(i).getFinalCards().get(j).getValue().ordinal()) {
                            tmpHigh = algo.get(i);
                            high = true;
                        } else if (tmpHigh.getFinalCards().get(j).getValue().ordinal() == algo.get(i).getFinalCards().get(j).getValue().ordinal()) {
                            counterEq++;
                            if (counterEq == 5) {
                                if (!index.contains(tmpHD.get(tmpHigh)))
                                    index.add(tmpHD.get(tmpHigh));
                                index.add(i);
                                high = false;
                            }
                        }
                    }
                }
            }else if(algo.get(i).getFinalCards().size() >= 5){
                System.out.println("F & SF -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(0).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(0).getValue());
                tmpHigh = algo.get(i);
            }
        }

        if(high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < algo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }
    }

    public void checkFullHouse(ArrayList<PokerHandsEvaluator> algo, HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK FULL HOUSE");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        boolean high = true;

        for(int i = 1; i < algo.size(); i++){
            if(tmpHigh.getFinalCards().size() == 5 && algo.get(i).getFinalCards().size() == 5){
                if(!contiene(tmpHigh, 0) && !contiene(algo.get(i), 0) && !contiene(tmpHigh, 1) && !contiene(algo.get(i), 1)) {
                    if(higher(tmpHigh).get(0).getValue().ordinal() < higher(algo.get(i)).get(0).getValue().ordinal()){
                        tmpHigh = algo.get(i);
                        high = true;
                    }else if(higher(tmpHigh).get(0).getValue().ordinal() == higher(algo.get(i)).get(0).getValue().ordinal()){
                        if(higher(tmpHigh).get(1).getValue().ordinal() < higher(algo.get(i)).get(1).getValue().ordinal()){
                            tmpHigh = algo.get(i);
                            high = true;
                        }else if(higher(tmpHigh).get(1).getValue().ordinal() == higher(algo.get(i)).get(1).getValue().ordinal()){
                            if (!index.contains(tmpHD.get(tmpHigh)))
                                index.add(tmpHD.get(tmpHigh));
                            index.add(i);
                            high = false;
                        }
                    }
                }else{
                    if(tmpHigh.getFinalCards().get(0).getValue().ordinal() < algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                        tmpHigh = algo.get(i);
                        high = true;
                    }else if(tmpHigh.getFinalCards().get(0).getValue().ordinal() == algo.get(i).getFinalCards().get(0).getValue().ordinal()){
                        if(tmpHigh.getFinalCards().get(4).getValue().ordinal() < algo.get(i).getFinalCards().get(4).getValue().ordinal()){
                            tmpHigh = algo.get(i);
                            high = true;
                        }else if(tmpHigh.getFinalCards().get(4).getValue().ordinal() == algo.get(i).getFinalCards().get(4).getValue().ordinal()){
                            if (!index.contains(tmpHD.get(tmpHigh)))
                                index.add(tmpHD.get(tmpHigh));
                            index.add(i);
                            high = false;
                        }
                    }
                }
            }else if(algo.get(i).getFinalCards().size() == 5){
                tmpHigh = algo.get(i);
            }
        }

        if(high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < algo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }
    }

    public void checkFourOfAKind(ArrayList<PokerHandsEvaluator> algo, ArrayList<ArrayList<CardModel>> tmpAlgo, HashMap<PokerHandsEvaluator, Integer> tmpHD){
        System.out.println("CHECK FOUR OF A KIND");
        index.clear();
        PokerHandsEvaluator tmpHigh = algo.get(0);
        //ArrayList<CardModel> tmpHigh = tmpAlgoCouples.get(0);
        boolean high = true;
        for(int i = 1; i < algo.size(); i++){
            if(tmpHigh.getFinalCards().size() >= 4 && algo.get(i).getFinalCards().size() >= 4){
                System.out.println("FOUR OF A KIND -> CARD " + 0 + ": " + tmpHigh.getFinalCards().get(3).getValue() + " CARD " + i + ": " +  algo.get(i).getFinalCards().get(3).getValue());
                if(tmpHigh.getFinalCards().get(3).getValue().ordinal() < algo.get(i).getFinalCards().get(3).getValue().ordinal()){
                    tmpHigh = algo.get(i);
                    high = true;
                }else if(tmpHigh.getFinalCards().get(3).getValue().ordinal() == algo.get(i).getFinalCards().get(3).getValue().ordinal()){
                    high = false;
                    if(!index.contains(tmpHD.get(tmpHigh)))
                        index.add(tmpHD.get(tmpHigh));
                    index.add(i);
                }
            }else if(algo.get(i).getFinalCards().size() == 4){
                tmpHigh = algo.get(i);
            }

        }

        if (high == true){
            System.out.println("Indice Vincitore: " + tmpHD.get(tmpHigh));
            int tmpIndex = tmpHD.get(tmpHigh);
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpIndex)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else
            checkHigherCard(tmpAlgo);
    }

    public void checkRoyalFlush(ArrayList<PokerHandsEvaluator> algo, HashMap<PokerHandsEvaluator, Integer> tmpHD){

    }

    public ArrayList<CardModel> higher(PokerHandsEvaluator pCards){
        ArrayList<CardModel> tmp = new ArrayList<>();
        if(pCards.getPlayerCards().get(0).getValue().ordinal() > pCards.getPlayerCards().get(1).getValue().ordinal()) {
            tmp.add(pCards.getPlayerCards().get(0));
            tmp.add(pCards.getPlayerCards().get(1));
            return tmp;
        }else{
            tmp.add(pCards.getPlayerCards().get(1));
            tmp.add(pCards.getPlayerCards().get(0));
            return tmp;
        }
    }

    public Boolean contiene(PokerHandsEvaluator common, int i){
        return common.getFinalCards().contains(common.getPlayerCards().get(i));
    }

    public String getPlayerHandByName(String nickname) {
        return playersHandByName.get(nickname);
    }

    public ArrayList<String> getWinners(){
        return winners;
    }
}
