package server.controller;

import server.algorithm.PokerHandsEvaluator;
import server.model.CommunityModel;
import server.model.PlayerModel;
import server.model.cards.CardModel;
import utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TurnWinnerEvaluator {
    private ArrayList<PlayerModel> players;
    private CommunityModel communityCards;
    private HashMap<String, Integer> playersHand;
    private ArrayList<PokerHandsEvaluator> playerPoints;
    private HashMap<String, String> playersHandByName;
    private ArrayList<String> winners;
    ArrayList<Integer> index = new ArrayList<>();

    public TurnWinnerEvaluator(ArrayList<PlayerModel> players, CommunityModel communityCards) {
        playerPoints = new ArrayList<>();
        playersHand = new HashMap<>();
        playersHandByName = new HashMap<>();
        winners = new ArrayList<>();
        this.communityCards = communityCards;
        this.players = players;

    }

    /*public void evaluateTurnWinner(){
        players.forEach(player -> checkWhoWin(communityCards.getCommunityCards(), player.getCards().get(0), player.getCards().get(1)));
        letsStart();
    }*/
    public String evaluateTurnWinner() {
        ArrayList<PlayerModel> inGamePlayers = players.stream()
                .filter(playerModel -> !playerModel.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));

        for (PlayerModel player : inGamePlayers) {
            PokerHandsEvaluator evaluator = new PokerHandsEvaluator(player.getCards(), communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4));
            evaluator.evaluate();

            playersHand.put(player.getNickname(), evaluator.getPlayerPoint());
            playersHandByName.put(player.getNickname(), evaluator.getPlayerPointName());
        }

        return playersHand.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
    }

    public String getPlayerHandByName(String nickname) {
        return playersHandByName.get(nickname);
    }



    public ArrayList<PokerHandsEvaluator> checkWhoWin(ArrayList<CardModel> c, CardModel p1, CardModel p2){

        PokerHandsEvaluator algo = new PokerHandsEvaluator(c, p1, p2);
        algo.evaluate();
        playerPoints.add(algo);
        return playerPoints;
    }

    public ArrayList<PokerHandsEvaluator> checkWhoWin2(int i, ArrayList<CardModel> c,CardModel... playerCards){

        PokerHandsEvaluator algo = new PokerHandsEvaluator(c);
        algo.evaluate();
        playerPoints.set(i,algo);

        return playerPoints;
    }

    public void pareggio(ArrayList<PokerHandsEvaluator> algo){

        /*ArrayList<CardModel> tmpCardsPlayer1 = new ArrayList<>();
        ArrayList<CardModel> tmpCardsPlayer2 = new ArrayList<>();
        ArrayList<CardModel> tmpCouples1 = new ArrayList<>();
        ArrayList<CardModel> tmpCouples2 = new ArrayList<>();*/

        ArrayList<ArrayList<CardModel>> tmpAlgo = new ArrayList<>();
        ArrayList<ArrayList<CardModel>> tmpAlgoCouples = new ArrayList<>();
        System.out.println("SIZE ALGO: "+algo.size());
        for(int i = 0; i < algo.size(); i++){
            tmpAlgo.add(algo.get(i).getCardsToExamine());
            System.out.println("FIRSTDECK  "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
        }
        /*tmpCardsPlayer1 = algorithm.get(0).getCardsToExamine();
        tmpCardsPlayer2= algorithm.get(1).getCardsToExamine();*/

        for(int i = 0; i < tmpAlgo.size(); i++){
            Utils.sortCards(tmpAlgo.get(i), true);
            System.out.println("SORTDECK: "+i+": " + tmpAlgo.get(i).get(0).getValue()+" "+ tmpAlgo.get(i).get(1).getValue());
        }
        /*Utils.sortCards(tmpCardsPlayer1, true);
        Utils.sortCards(tmpCardsPlayer2, true);*/

        ArrayList<Integer> countC = new ArrayList<>();
        for(int i = 0; i < tmpAlgo.size(); i++){
            countC.add(Utils.couplesNumber(tmpAlgo.get(i)));
            if(countC.get(i) != 0)
                System.out.println("COUNTCOUPLES "+i+": "+ countC.get(i));
        }
        /*int c1 = Utils.couplesNumber(tmpCardsPlayer1);
        int c2 = Utils.couplesNumber(tmpCardsPlayer2);*/
        int oneC = 0, twoC = 0, threeC = 0;
        for(int i = 0; i < countC.size(); i++){
            if(countC.get(i) == 1){
                oneC++;
            }else if(countC.get(i) == 2){
                twoC++;
            }else if(countC.get(i) == 3)
                threeC++;
        }

        int tmpZ = 0;
        int countScale = 0;
        int countFull = 0;
        ArrayList<CardModel> tmpfinalFirstCard = new ArrayList<>();
        for(int i = 0; i < tmpAlgo.size(); i++){
            System.out.println("OUT: "+Utils.getCouples(tmpAlgo.get(i)).size());
            //System.out.println(algorithm.get(i).getFinalCards().get(0).getValue());
            if(algo.get(i).getPlayerPoint() == 7){
                countFull++;
                tmpfinalFirstCard.add(algo.get(i).getFinalCards().get(0));
                tmpfinalFirstCard.add(algo.get(i).getFinalCards().get(2));
                System.out.println(algo.get(i).getFinalCards().get(0).getValue() + " " + algo.get(i).getFinalCards().get(2).getValue());
            }else if(algo.get(i).getPlayerPoint() == 5) {
                countScale++;
                tmpfinalFirstCard.add(algo.get(i).getFinalCards().get(0));
            }
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

        //Utils.checkCouplesNumber(countC)/2 > countC.size()-2
        if(countFull >= 2){
            System.out.println("CHECK FULL");
            CardModel tmpTris = tmpfinalFirstCard.get(1);
            CardModel tmpCouple = tmpfinalFirstCard.get(0);
            int tmpI = findTheIndice(tmpAlgo, tmpCouple);
            for(int i = 0; i<tmpfinalFirstCard.size(); i++){
                if(i%2 != 0 && i != tmpfinalFirstCard.size()-1){
                    if(tmpTris.getValue().ordinal() < tmpfinalFirstCard.get(i+2).getValue().ordinal()){
                        tmpTris = tmpfinalFirstCard.get(i+2);
                        tmpCouple = tmpfinalFirstCard.get(i+1);
                        tmpI = findTheIndice(tmpAlgo, tmpCouple);;
                    }else if(tmpTris.getValue().ordinal() == tmpfinalFirstCard.get(i+2).getValue().ordinal()){
                        if(tmpCouple.getValue().ordinal() < tmpfinalFirstCard.get(i+1).getValue().ordinal()){
                            tmpTris = tmpfinalFirstCard.get(i+2);
                            tmpCouple = tmpfinalFirstCard.get(i+1);
                            tmpI = findTheIndice(tmpAlgo, tmpCouple);;
                        }
                    }
                }
            }

            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpI)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else if(countScale >= 2){
            System.out.println("CHECK STRAIGHT");
            CardModel tmp = tmpfinalFirstCard.get(0);
            int tmpI = 0;
            for(int i = 0; i<tmpfinalFirstCard.size(); i++){
                if(i != 0){
                    if(tmp.getValue().ordinal() < tmpfinalFirstCard.get(i).getValue().ordinal()){
                        tmp = tmpfinalFirstCard.get(i);
                        tmpI = i;
                    }else{
                        System.out.println("WIP");
                    }
                }
            }

            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpI)
                    playerPoints.get(i).setPlayerPoint(0);
            }
        }else if(threeC >= twoC && threeC != 0){
            System.out.println("CHECK TRIS");

            ArrayList<CardModel> tmp = tmpAlgoCouples.get(0);
            int tmpI = findTheIndice(tmpAlgo, tmp);
            for(int i = 0; i < tmpAlgoCouples.size(); i++){
                if(countC.get(i) == 3 && i != 0){
                    if(tmp.get(0).getValue().ordinal() < tmpAlgoCouples.get(i).get(0).getValue().ordinal()){
                        tmp = tmpAlgoCouples.get(i);
                        tmpI = i;
                        playerPoints.get(i).setFinalCards(tmp);
                    }
                }
            }
            for(int i = 0; i < tmpAlgo.size(); i++){
                if( i != tmpI)
                    playerPoints.get(i).setPlayerPoint(0);
            }
            /*for(int i = 0; i < tmpAlgo.size(); i++){
                if( i == tmpI){
                    playerPoints.get(i).setPlayerPoint(4);
                }
                else
                    playerPoints.get(i).setPlayerPoint(0);
                System.out.println(playerPoints.get(i).getPlayerPoint());
            }*/
        }else if(twoC >= oneC && twoC != 0){

            System.out.println("CHECK DOUBLE");
            for (int i = 0; i < tmpAlgoCouples.size(); i++){
                //Utils.sortCards(tmpAlgoCouples.get(i), true);
                if(tmpAlgoCouples.get(i).size() != 2)
                    System.out.println("SIZE:"+tmpAlgoCouples.size()+" "+tmpAlgoCouples.get(i).get(0).getValue().ordinal()+ " "+tmpAlgoCouples.get(i).get(2).getValue()+" "+tmpAlgoCouples.get(i).get(1).getValue().ordinal()+ " "+tmpAlgoCouples.get(i).get(1).getValue());
            }
            ArrayList<CardModel> tmpH = tmpAlgoCouples.get(0);
            int tmpWinnerIndice3 = findTheIndice(tmpAlgo, tmpH);
            Boolean sameFirst= false;
            Boolean sameSecond = false;

            ArrayList<Integer> eTndex = new ArrayList<>();
            eTndex.add(findTheIndice(tmpAlgo, tmpH));
            int tmpE1 = 0;
            int tmpE2 = 0;

            for(int i = 0; i < tmpAlgoCouples.size(); i++){
                int tmpC1 = tmpH.get(0).getValue().ordinal();
                if(i != tmpAlgoCouples.size()-1 &&  tmpC1 != 0 && tmpAlgoCouples.get(i+1).size() != 2){
                    int tmpC2 = tmpAlgoCouples.get(i+1).get(0).getValue().ordinal();
                    if(tmpC1 == tmpC2) {
                        sameFirst = true;
                        int tmpC3 = tmpH.get(2).getValue().ordinal();
                        int tmpC4 = tmpAlgoCouples.get(i+1).get(2).getValue().ordinal();
                        if(tmpC3 == tmpC4) {
                            sameSecond = true;
                            eTndex.set(0,findTheIndice(tmpAlgo, tmpH));
                            eTndex.add(i+1);
                        }
                        else if(tmpC3 < tmpC4)
                            sameSecond = false;


                    }

                    System.out.println(tmpH.get(0).getValue());
                    if(sameFirst == true && sameSecond != true){
                        if(tmpH.get(2).getValue().ordinal() < tmpAlgoCouples.get(i+1).get(2).getValue().ordinal()){
                            tmpH = tmpAlgoCouples.get(i+1);
                            System.out.println(tmpH.get(0).getValue());
                            tmpWinnerIndice3 = i+1;
                        }
                    }else if(tmpH.get(0).getValue().ordinal() < tmpAlgoCouples.get(i+1).get(0).getValue().ordinal()){
                        tmpH = tmpAlgoCouples.get(i+1);
                        System.out.println(tmpH.get(0).getValue());
                        tmpWinnerIndice3 = i+1;
                    }else if(tmpH.get(0).getValue().ordinal() == tmpAlgoCouples.get(i+1).get(0).getValue().ordinal())
                        tmpWinnerIndice3 = -1;
                }

            }

            ArrayList<ArrayList<CardModel>> tmpA = new ArrayList<>();
            int k=0;
            if(sameFirst == true && sameSecond == true && tmpWinnerIndice3 == -1){
                for (int i = 0; i < tmpAlgo.size(); i++){
                    tmpA.add(tmpAlgo.get(i));
                    System.out.println("indice: "+eTndex.get(k));
                    if(eTndex.get(k) == i){

                        //tmpA.add(tmpAlgo.get(i));
                        tmpA.set(i,Utils.removeCard(tmpA.get(i), tmpAlgoCouples.get(i).get(0)));
                        tmpA.set(i,Utils.removeCard(tmpA.get(i), tmpAlgoCouples.get(i).get(2)));
                        //System.out.println("REMOVEDCARD "+i+": " + tmpA.get(i).get(0).getValue());
                        if(k < eTndex.size()-1)
                            k++;
                    }
                }
                //k=0;
                ArrayList<CardModel> tmpC = tmpA.get(0);
                for(k=0; k < eTndex.size(); k++) {
                    for (int i = 0; i < 3; i++) {
                        if(k < eTndex.size()-1){
                            System.out.println(tmpC.get(i).getValue() + " " + tmpA.get(eTndex.get(k + 1)).get(i).getValue());
                            if (tmpC.get(i).getValue().ordinal() < tmpA.get(eTndex.get(k + 1)).get(i).getValue().ordinal()) {
                                playerPoints.get(findTheIndice(tmpAlgo, tmpC)).setPlayerPoint(0);
                                tmpC = tmpA.get(eTndex.get(k + 1));

                            } else if (tmpC.get(i).getValue().ordinal() > tmpA.get(eTndex.get(k + 1)).get(i).getValue().ordinal()) {
                                playerPoints.get(eTndex.get(k+1)).setPlayerPoint(0);
                            }else {
                                System.out.println("WIP");
                                if (i ==0){
                                    //add(eTndex)
                                    index.add(eTndex.get(k));
                                    break;
                                }
                            }
                        }
                    }
                }
            }else{
                for(int i = 0; i < tmpAlgo.size(); i++){
                    if( i != tmpWinnerIndice3)
                        playerPoints.get(i).setPlayerPoint(0);
                }

            }



        }else if(oneC > twoC && oneC != 0){
            System.out.println("CHECK COUPLE");
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
                for(k=0; k < eTndex.size(); k++) {
                    for (int i = 0; i < 5; i++) {
                        if(k < eTndex.size()-1){
                            System.out.println(tmpC.get(i).getValue() + " " + tmpA.get(eTndex.get(k + 1)).get(i).getValue());
                            if (tmpC.get(i).getValue().ordinal() < tmpA.get(eTndex.get(k + 1)).get(i).getValue().ordinal()) {
                                playerPoints.get(findTheIndice(tmpAlgo, tmpC)).setPlayerPoint(0);
                                tmpC = tmpA.get(eTndex.get(k + 1));

                            } else if (tmpC.get(i).getValue().ordinal() > tmpA.get(eTndex.get(k + 1)).get(i).getValue().ordinal()) {
                                playerPoints.get(eTndex.get(k+1)).setPlayerPoint(0);
                            }else {
                                //System.out.println("WIP");
                                if (i ==0){
                                    //add(eTndex)
                                    index.add(eTndex.get(k));
                                    break;
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

        }else{
            /*for (int i = 0; i < tmpAlgo.size(); i++){
                System.out.println("SIZE:"+tmpAlgo.size()+" "+tmpAlgo.get(i).get(0).getValue().ordinal()+ " "+tmpAlgo.get(i).get(0).getValue());
            }*/
            System.out.println("CHECK SOLO");
            ArrayList<CardModel> tmpH = tmpAlgo.get(0);
            int tmpWinnerIndice1 = 0;
            Boolean same = false;

            for(int i = 0; i < tmpAlgo.size(); i++){
                int tmpC1 = tmpAlgo.get(i).get(0).getValue().ordinal();
                if(i != tmpAlgo.size()-1 && tmpC1 != 0){
                    int tmpC2 = tmpAlgo.get(i+1).get(0).getValue().ordinal();
                    if(tmpC1 == tmpC2)
                        same = true;
                }

                winnerIndice(tmpH, tmpAlgo, i, tmpC1, tmpWinnerIndice1);

                //System.out.println("PlayePointVector: " + tmpPP[i]);
                //System.out.println("PlayePointArray: " + playerPoints.get(i).getPlayerPoint());
            }
            //System.out.println("SOLOCARD: "+tmpC + " " + tmpI);
            if(same == true && tmpWinnerIndice1 == -1){
                for (int i = 0; i < tmpAlgo.size(); i++){
                    tmpAlgo.set(i, Utils.removeCard(tmpAlgo.get(i), tmpAlgo.get(i).get(0)));
                    System.out.println("REMOVEDCARD "+i+": " + tmpAlgo.get(i).get(0).getValue());
                    playerPoints.get(i).setCardsToExamine(tmpAlgo.get(i));
                    checkWhoWin2(i,tmpAlgo.get(i));
                }
            }else{
                for(int i = 0; i < tmpAlgo.size(); i++){
                    if( i != tmpWinnerIndice1)
                        playerPoints.get(i).setPlayerPoint(0);
                }

            }
            //System.out.println(tmpCardsPlayer1.get(0).getValue().ordinal()+ " "+tmpCardsPlayer1.get(0).getValue());
            //System.out.println(tmpCardsPlayer2.get(0).getValue().ordinal()+ " "+tmpCardsPlayer2.get(0).getValue());
        }
        //letsStart();
    }

    public int findTheIndice(ArrayList<ArrayList<CardModel>> d, CardModel c){
        int counter = 0;
        for(int i = 0; i < d.size(); i++){
            for (int j = 0; j < 7; j++){
                System.out.println(counter);
                if(d.get(i).get(j).getValue().ordinal() == c.getValue().ordinal())
                    counter++;
                else
                    counter = 0;

                if(counter >= 2)
                    return i;
            }
        }
        return 0;
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

    public void winnerIndice(ArrayList<CardModel> a, ArrayList<ArrayList<CardModel>> aa, int i, int c, int w){
        System.out.println(a.get(0).getValue());
        if (i != 0) {
            if(a.get(0).getValue().ordinal() < c){
                a = aa.get(i);
                System.out.println(a.get(0).getValue());
                w = i;
            }else if(a.get(0).getValue().ordinal() == c)
                w = -1;
        }
    }

    public void letsStart(){
        ArrayList<PokerHandsEvaluator> algo = new ArrayList<>();
        PokerHandsEvaluator tmpH = playerPoints.get(0);
        int[] tmpPP = Utils.riordina(playerPoints, playerPoints.size());
        int tmpWinnerIndice = 0;
        Boolean same = false;

        for(int i = 0; i < tmpPP.length; i++){
            System.out.println("indice:"+tmpPP[i]);
            if(i != tmpPP.length-1 && tmpPP[i] != 0){
                if(tmpPP[i] == tmpPP[i+1])
                    same = true;
            }

            if (i != 0) {
                if(tmpH.getPlayerPoint() < playerPoints.get(i).getPlayerPoint()){
                    tmpH = playerPoints.get(i);
                    tmpWinnerIndice = i;
                }else if(tmpH.getPlayerPoint() == playerPoints.get(i).getPlayerPoint())
                    tmpWinnerIndice = -1;
            }

            System.out.println("PlayePointVector: " + tmpPP[i]);
            System.out.println("PlayePointArray: " + playerPoints.get(i).getPlayerPoint());
        }

        if(same == true && tmpWinnerIndice == -1){
            for(int i = 0; i < playerPoints.size(); i++)
                algo.add(playerPoints.get(i));
            if(index.isEmpty())
                pareggio(algo);
            else {
                System.out.println("SPLIT");
                for(int ind : index){
                    winners.add(players.get(ind).getNickname());
                }
            }
            System.out.println("PAREGGIO");
        }else{
            System.out.println("VINCITORE");
            winners.add(players.get(0).getNickname());
        }
    }

    public ArrayList<String> getWinners(){
        return winners;
    }
}
