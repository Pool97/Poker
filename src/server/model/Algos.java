package server.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static utils.Utils.*;


public class Algos {

    public static int priority1 = 0;
    public static int priority2 = 0;
    public static int priority3 = 0;
    public static int priority4 = 0;
    public static int priority5 = 0;
    public static int priority6 = 0;
    public static int priority7 = 0;
    public static int priority8 = 0;
    public static int priority9 = 0;
    public static int priority10 = 0;

    private ArrayList<Pair<CardSuit, CardRank>> cards;

    private Algos(ArrayList<CardModel> playerCards, Pair<CardSuit, CardRank>... communityCards) {
        cards = Arrays.stream(communityCards).collect(Collectors.toCollection(ArrayList::new));
        cards.addAll(playerCards);
        System.out.println(cards.toString());
    }

    public static Algos newInstance(ArrayList<CardModel> playerCards, Pair<CardSuit, CardRank>... communityCards) {
        return new Algos(playerCards, communityCards);
    }

    public static void riazzera() {
        priority1 = 0;
        priority2 = 0;
        priority3 = 0;
        priority4 = 0;
        priority5 = 0;
        priority6 = 0;
        priority7 = 0;
        priority8 = 0;
        priority9 = 0;
        priority10 = 0;
    }

    public static ArrayList<Pair<CardSuit, CardRank>> scalaReale(Pair<CardSuit, CardRank> com1, Pair<CardSuit, CardRank> com2, Pair<CardSuit, CardRank> com3, Pair<CardSuit, CardRank> com4, Pair<CardSuit, CardRank> com5, Pair<CardSuit, CardRank> ply1, Pair<CardSuit, CardRank> ply2) {

        int tmpC = 1;
        int tmpIC1 = 0;
        int tmpIC2 = 0;
        int tmpIC3 = 0;
        int tmpIC4 = 0;
        int tmpIC5 = 0;


        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        ArrayList<Pair<CardSuit, CardRank>> ayTmp = new ArrayList<>();

        ayTmp.add(com1);
        ayTmp.add(com2);
        ayTmp.add(com3);
        ayTmp.add(com4);
        ayTmp.add(com5);
        ayTmp.add(ply1);
        ayTmp.add(ply2);


        ArrayList<Pair<CardSuit, CardRank>> cards = cardArrayPair(com1, com2, com3, com4, com5, ply1, ply2);
        ArrayList<String> cardsS = cardArrayString(ayTmp);


        int numeroelementi = cardsS.size();

        for (int i = 0; i <= numeroelementi - 1; i++) {
            for (int j = 0; j <= numeroelementi - 1; j++) {
                if (i != j) {
                    if (tmpC != 5) {
                        if (tmpC == 1) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                if (cards.get(i).getValue().toString().equals("ACE") || cards.get(i).getValue().toString().equals("KING") || cards.get(i).getValue().toString().equals("QUEEN") || cards.get(i).getValue().toString().equals("JACK") || cards.get(i).getValue().toString().equals("TEN")) {
                                    if (cards.get(j).getValue().toString().equals("ACE") || cards.get(j).getValue().toString().equals("KING") || cards.get(j).getValue().toString().equals("QUEEN") || cards.get(j).getValue().toString().equals("JACK") || cards.get(j).getValue().toString().equals("TEN")) {
                                        tmpIC1 = i;
                                        tmpIC2 = j;
                                        tmpC++;
                                    }
                                }
                            }
                        } else if (tmpC == 2) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                if (cards.get(j).getValue().toString().equals("ACE") || cards.get(j).getValue().toString().equals("KING") || cards.get(j).getValue().toString().equals("QUEEN") || cards.get(j).getValue().toString().equals("JACK") || cards.get(j).getValue().toString().equals("TEN")) {
                                    tmpIC3 = j;
                                    tmpC++;
                                }
                            }
                        } else if (tmpC == 3) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                if (cards.get(j).getValue().toString().equals("ACE") || cards.get(j).getValue().toString().equals("KING") || cards.get(j).getValue().toString().equals("QUEEN") || cards.get(j).getValue().toString().equals("JACK") || cards.get(j).getValue().toString().equals("TEN")) {
                                    tmpIC4 = j;
                                    tmpC++;
                                }
                            }
                        } else if (tmpC == 4) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                if (cards.get(j).getValue().toString().equals("ACE") || cards.get(j).getValue().toString().equals("KING") || cards.get(j).getValue().toString().equals("QUEEN") || cards.get(j).getValue().toString().equals("JACK") || cards.get(j).getValue().toString().equals("TEN")) {
                                    tmpIC5 = j;
                                    tmpC++;
                                }
                            }
                        }
                    }
                }
            }
            if (tmpC != 5) {
                tmpC = 1;
            }
        }

        if (tmpC == 5) {
            priority1 = 1;
            if (tmpIC1 == 0 || tmpIC2 == 0 || tmpIC3 == 0 || tmpIC4 == 0 || tmpIC5 == 0)
                ay.add(com1);
            if (tmpIC1 == 1 || tmpIC2 == 1 || tmpIC3 == 1 || tmpIC4 == 1 || tmpIC5 == 1)
                ay.add(com2);
            if (tmpIC1 == 2 || tmpIC2 == 2 || tmpIC3 == 2 || tmpIC4 == 2 || tmpIC5 == 2)
                ay.add(com3);
            if (tmpIC1 == 3 || tmpIC2 == 3 || tmpIC3 == 3 || tmpIC4 == 3 || tmpIC5 == 3)
                ay.add(com4);
            if (tmpIC1 == 4 || tmpIC2 == 4 || tmpIC3 == 4 || tmpIC4 == 4 || tmpIC5 == 4)
                ay.add(com5);
            if (tmpIC1 == 5 || tmpIC2 == 5 || tmpIC3 == 5 || tmpIC4 == 5 || tmpIC5 == 5)
                ay.add(ply1);
            if (tmpIC1 == 6 || tmpIC2 == 6 || tmpIC3 == 6 || tmpIC4 == 6 || tmpIC5 == 6)
                ay.add(ply2);

            ay = ordinamento(ay);
        }
        return ay;

    }

    public static ArrayList<Pair<CardSuit, CardRank>> scalaColore(Pair<CardSuit, CardRank> com1, Pair<CardSuit, CardRank> com2, Pair<CardSuit, CardRank> com3, Pair<CardSuit, CardRank> com4, Pair<CardSuit, CardRank> com5, Pair<CardSuit, CardRank> ply1, Pair<CardSuit, CardRank> ply2) {

        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        ArrayList<Pair<CardSuit, CardRank>> ayA14 = new ArrayList<>();
        ArrayList<Pair<CardSuit, CardRank>> ayA1 = new ArrayList<>();


        int counterA14 = 0;
        int counterA1 = 0;

        ayA14.add(com1);
        ayA14.add(com2);
        ayA14.add(com3);
        ayA14.add(com4);
        ayA14.add(com5);
        ayA14.add(ply1);
        ayA14.add(ply2);

        ayA1.add(com1);
        ayA1.add(com2);
        ayA1.add(com3);
        ayA1.add(com4);
        ayA1.add(com5);
        ayA1.add(ply1);
        ayA1.add(ply2);

        ayA14 = ordinamentoA14(ayA14);
        ArrayList<Integer> cardsA14 = cardArrayA14(ayA14);
        ArrayList<String> cardsSA14 = cardArrayString(ayA14);
        int numeroelementiA14 = cardsA14.size();

        ayA1 = ordinamentoA1(ayA1);
        ArrayList<Integer> cardsA1 = cardArrayA1(ayA1);
        ArrayList<String> cardsSA1 = cardArrayString(ayA1);
        int numeroelementiA1 = cardsA1.size();

        ArrayList<Integer> tmpArray = new ArrayList<>();

        for (int i = 0; i <= numeroelementiA14 - 1; i++) {
            for (int j = 0; j <= numeroelementiA14 - 1; j++) {
                if (i != j) {
                    if ((cardsSA14.get(i).equals(cardsSA14.get(j)))) {
                        if (i != numeroelementiA14 - 1 && counterA14 != 4) {
                            int c = 0;
                            //System.out.println(ayA14.get(i).toString());
                            if ((cardsA14.get(i) - 1) == cardsA14.get(j)) {
                                ay.add(ayA14.get(i));
                                counterA14++;
                                System.out.println(counterA14);
                                c++;
                                i = j;
                                if (counterA14 == 4)
                                    ay.add(ayA14.get(j));
                            } else if (counterA14 != 4) {
                                counterA14 = 0;
                                ay = new ArrayList<>();
                            }
                        }
                    }
                }
            }
            if (counterA14 == 4)
                break;
        }
        System.out.println();


        if (ay.size() != 5) {
            ay = new ArrayList<>();
            for (int i = 0; i <= numeroelementiA1 - 1; i++) {
                for (int j = 0; j <= numeroelementiA1 - 1; j++) {
                    if (i != j) {
                        if ((cardsSA1.get(i).equals(cardsSA1.get(j)))) {
                            //System.out.println(cardsSA1.get(i) + "-" + cardsSA1.get(j) );
                            if (i != numeroelementiA1 - 1 && counterA1 != 4) {
                                //System.out.println(ayA14.get(i).toString());
                                if ((cardsA1.get(i) - 1) == cardsA1.get(j)) {
                                    ay.add(ayA1.get(i));
                                    counterA1++;
                                    //System.out.println(counterA1);
                                    i = j;
                                    if (counterA1 == 4)
                                        ay.add(ayA1.get(j));
                                } else if (counterA1 != 4) {
                                    counterA1 = 0;
                                    ay = new ArrayList<>();
                                }
                            }
                        }
                    }
                }
                if (counterA1 == 4)
                    break;
            }
        }


        if (counterA14 == 4) {
            System.out.println("Il counter è a " + counterA14);
            priority2 = 2;
            return ay;
        } else if (counterA1 == 4) {
            System.out.println("Il counter è a " + counterA1);
            priority2 = 2;
            return ay;
        }


        return null;
    }

    public static ArrayList<Pair<CardSuit, CardRank>> poker(Pair<CardSuit, CardRank> com1, Pair<CardSuit, CardRank> com2, Pair<CardSuit, CardRank> com3, Pair<CardSuit, CardRank> com4, Pair<CardSuit, CardRank> com5, Pair<CardSuit, CardRank> ply1, Pair<CardSuit, CardRank> ply2) {

        int tmpIC1 = 0;
        int tmpIC2 = 0;
        int tmpIC3 = 0;
        int tmpIC4 = 0;

        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();

        int[] cards = cardArray(com1, com2, com3, com4, com5, ply1, ply2);

        boolean coppia1 = false;
        boolean coppia1_2 = false;
        boolean coppia2_3 = false;


        int numeroelementi = cards.length;

        for (int i = 0; i <= numeroelementi - 1; i++) {
            for (int j = 0; j <= numeroelementi - 1; j++) {
                if (i != j) {
                    if (coppia1 != true) {
                        if (cards[i] == cards[j]) {
                            coppia1 = true;
                            tmpIC1 = i;
                            tmpIC2 = j;
                        }
                    } else if (coppia1 == true && coppia1_2 != true) {
                        if (cards[i] == cards[j]) {
                            coppia1_2 = true;
                            tmpIC3 = j;
                        }
                    } else if (coppia1 == true && coppia1_2 == true) {
                        if (coppia2_3 != true) {
                            if (cards[i] == cards[j]) {
                                coppia2_3 = true;
                                tmpIC4 = j;
                            }
                        }
                    }
                }
            }
            if (coppia1 == true && coppia1_2 != true)
                coppia1 = false;
            else if (coppia1 == true && coppia1_2 == true && coppia2_3 != true) {
                coppia1 = false;
                coppia1_2 = false;
            }
        }

        if (coppia1 == true && coppia1_2 == true && coppia2_3 == true) {
            priority3 = 3;
            if (tmpIC1 == 0 || tmpIC2 == 0 || tmpIC3 == 0 || tmpIC4 == 0)
                ay.add(com1);
            if (tmpIC1 == 1 || tmpIC2 == 1 || tmpIC3 == 1 || tmpIC4 == 1)
                ay.add(com2);
            if (tmpIC1 == 2 || tmpIC2 == 2 || tmpIC3 == 2 || tmpIC4 == 2)
                ay.add(com3);
            if (tmpIC1 == 3 || tmpIC2 == 3 || tmpIC3 == 3 || tmpIC4 == 3)
                ay.add(com4);
            if (tmpIC1 == 4 || tmpIC2 == 4 || tmpIC3 == 4 || tmpIC4 == 4)
                ay.add(com5);
            if (tmpIC1 == 5 || tmpIC2 == 5 || tmpIC3 == 5 || tmpIC4 == 5)
                ay.add(ply1);
            if (tmpIC1 == 6 || tmpIC2 == 6 || tmpIC3 == 6 || tmpIC4 == 6)
                ay.add(ply2);
        }
        return ay;
    }

    /*public static ArrayList<Pair<CardSuit, CardRank>> full(Pair<CardSuit, CardRank> com1, Pair<CardSuit, CardRank> com2, Pair<CardSuit, CardRank> com3, Pair<CardSuit, CardRank> com4, Pair<CardSuit, CardRank> com5, Pair<CardSuit, CardRank> ply1, Pair<CardSuit, CardRank> ply2){
        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        ArrayList<Pair<CardSuit, CardRank>> tmpT;
        ArrayList<Pair<CardSuit, CardRank>> tmpDC;
        ArrayList<Pair<CardSuit, CardRank>> tmpC;

        tmpT = tris(com1, com2, com3, com4, com5, ply1, ply2);
        if(priority7 == 7){
            System.out.println("TRIPLA TROVATA");
            ay.add(tmpT.get(0));
            ay.add(tmpT.get(1));
            ay.add(tmpT.get(2));

            tmpDC = doppiaCoppia(com1, com2, com3, com4, com5, ply1, ply2);
            tmpC = coppia(com1, com2, com3, com4, com5, ply1, ply2);
            if(priority8 == 8){
                System.out.println("COPPIA TROVATA DC");
                priority4 = 4;
                ay.add(tmpDC.get(0));
                System.out.println(tmpDC.get(0));
                ay.add(tmpDC.get(1));
                System.out.println(tmpDC.get(0));

            }else if(priority9 == 9){
                System.out.println("COPPIA TROVATA C");
                priority4 = 4;
                ay.add(tmpC.get(0));
                System.out.println(tmpC.get(0));
                ay.add(tmpC.get(1));
                System.out.println(tmpC.get(1));

            }

        }


        return ay;
    }*/

    public static ArrayList<Pair<CardSuit, CardRank>> ordinamento(ArrayList<Pair<CardSuit, CardRank>> a) {

        int i, j, n;
        Pair<CardSuit, CardRank> tmp;

        n = a.size();
        //System.out.println("SIZE: " + n);

        for (j = 0; j < n; j++) {
            for (i = n - 2; i >= j; i--) {
                if (controllo(a.get(i)) < controllo(a.get(i + 1))) //cambiare questa condizione per invertire l'ordine
                {
                    tmp = a.get(i);
                    a.set(i, a.get(i + 1));
                    a.set(i + 1, tmp);
                }
            }
        }

        return a;
    }

    public static ArrayList<Pair<CardSuit, CardRank>> ordinamentoA14(ArrayList<Pair<CardSuit, CardRank>> a) {

        int i, j, n;
        Pair<CardSuit, CardRank> tmp;

        n = a.size();
        //System.out.println("SIZE: " + n);

        for (j = 0; j < n; j++) {
            for (i = n - 2; i >= j; i--) {
                if (controlloA14(a.get(i)) < controlloA14(a.get(i + 1))) //cambiare questa condizione per invertire l'ordine
                {
                    tmp = a.get(i);
                    a.set(i, a.get(i + 1));
                    a.set(i + 1, tmp);
                }
            }
        }
        return a;
    }

    public static ArrayList<Pair<CardSuit, CardRank>> ordinamentoA1(ArrayList<Pair<CardSuit, CardRank>> a) {

        int i, j, n;
        Pair<CardSuit, CardRank> tmp;

        n = a.size();
        //System.out.println("SIZE: " + n);

        for (j = 0; j < n; j++) {
            for (i = n - 2; i >= j; i--) {
                if (controlloA1(a.get(i)) < controlloA1(a.get(i + 1))) //cambiare questa condizione per invertire l'ordine
                {
                    tmp = a.get(i);
                    a.set(i, a.get(i + 1));
                    a.set(i + 1, tmp);
                }
            }
        }
        return a;

    }

    public static ArrayList<Pair<CardSuit, CardRank>> duplicati(ArrayList<Pair<CardSuit, CardRank>> a) {
        int k = 1;
        int n = a.size();
        for (int i = 1; i < n; i++) {
            if (a.get(i) != a.get(i - 1)) {
                a.set(k, a.get(i));
                k++;
            }
        }

        return a;
    }

    public ArrayList<Pair<CardSuit, CardRank>> colore() {

        int tmpC = 1;
        int tmpIC1 = 0;
        int tmpIC2 = 0;
        int tmpIC3 = 0;
        int tmpIC4 = 0;
        int tmpIC5 = 0;


        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        ArrayList<Pair<CardSuit, CardRank>> ayTmp = new ArrayList<>(cards);

        ArrayList<String> cardsS = cardArrayString(ayTmp);
        int numeroelementi = cardsS.size();

        for (int i = 0; i <= numeroelementi - 1; i++) {
            for (int j = 0; j <= numeroelementi - 1; j++) {
                if (i != j) {
                    if (tmpC != 5) {
                        if (tmpC == 1) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                tmpIC1 = i;
                                tmpIC2 = j;
                                tmpC++;
                            }
                        } else if (tmpC == 2) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                tmpIC3 = j;
                                tmpC++;
                            }
                        } else if (tmpC == 3) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                tmpIC4 = j;
                                tmpC++;
                            }
                        } else if (tmpC == 4) {
                            if ((cardsS.get(i)).equals(cardsS.get(j))) {
                                tmpIC5 = j;
                                tmpC++;
                            }
                        }
                    }
                }
            }
            if (tmpC != 5) {
                tmpC = 1;
            }
        }

        if (tmpC == 5) {
            priority5 = 5;
            if (tmpIC1 == 0 || tmpIC2 == 0 || tmpIC3 == 0 || tmpIC4 == 0 || tmpIC5 == 0)
                ay.add(this.cards.get(0));
            if (tmpIC1 == 1 || tmpIC2 == 1 || tmpIC3 == 1 || tmpIC4 == 1 || tmpIC5 == 1)
                ay.add(this.cards.get(1));
            if (tmpIC1 == 2 || tmpIC2 == 2 || tmpIC3 == 2 || tmpIC4 == 2 || tmpIC5 == 2)
                ay.add(this.cards.get(2));
            if (tmpIC1 == 3 || tmpIC2 == 3 || tmpIC3 == 3 || tmpIC4 == 3 || tmpIC5 == 3)
                ay.add(this.cards.get(3));
            if (tmpIC1 == 4 || tmpIC2 == 4 || tmpIC3 == 4 || tmpIC4 == 4 || tmpIC5 == 4)
                ay.add(this.cards.get(4));
            if (tmpIC1 == 5 || tmpIC2 == 5 || tmpIC3 == 5 || tmpIC4 == 5 || tmpIC5 == 5)
                ay.add(this.cards.get(5));
            if (tmpIC1 == 6 || tmpIC2 == 6 || tmpIC3 == 6 || tmpIC4 == 6 || tmpIC5 == 6)
                ay.add(this.cards.get(6));

            ay = ordinamento(ay);
        }
        return ay;

    }

    public ArrayList<Pair<CardSuit, CardRank>> scala() {

        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        ArrayList<Pair<CardSuit, CardRank>> ayA14 = new ArrayList<>(cards);
        ArrayList<Pair<CardSuit, CardRank>> ayA1 = new ArrayList<>(cards);

        int counterA14 = 0;
        int counterA1 = 0;

        ayA14 = ordinamentoA14(ayA14);
        ayA14 = duplicati(ayA14);
        ArrayList<Integer> cardsA14 = cardArrayA14(ayA14);

        int numeroelementiA14 = cardsA14.size();

        ayA1 = ordinamentoA1(ayA1);
        ayA1 = duplicati(ayA1);
        ArrayList<Integer> cardsA1 = cardArrayA1(ayA1);
        int numeroelementiA1 = cardsA1.size();

        for (int i = 0; i <= numeroelementiA14 - 1; i++) {
            if (i != numeroelementiA14 - 1 && counterA14 != 4) {
                if ((cardsA14.get(i) - 1) == cardsA14.get(i + 1)) {
                    ay.add(ayA14.get(i));
                    counterA14++;
                    if (counterA14 == 4)
                        ay.add(ayA14.get(i + 1));
                } else {
                    counterA14 = 0;
                    ay = new ArrayList<>();
                }
            }

        }
        System.out.println();
        if (ay.size() != 5) {
            ay = new ArrayList<>();
            for (int i = 0; i <= numeroelementiA1 - 1; i++) {
                if (i != numeroelementiA1 - 1 && counterA1 != 4) {
                    if ((cardsA1.get(i) - 1) == cardsA1.get(i + 1)) {
                        ay.add(ayA1.get(i));
                        counterA1++;
                        if (counterA1 == 4)
                            ay.add(ayA1.get(i + 1));
                    } else {
                        counterA1 = 0;
                        ay = new ArrayList<>();
                    }
                }
            }
        }


        if (counterA14 == 4) {
            System.out.println("Il counter è a " + counterA14);
            priority6 = 6;
            return ay;
        } else if (counterA1 == 4) {
            System.out.println("Il counter è a " + counterA1);
            priority6 = 6;
            return ay;
        }


        return null;
    }

    public ArrayList<Pair<CardSuit, CardRank>> tris() {

        int tmpIC1 = 0;
        int tmpIC2 = 0;
        int tmpIC3 = 0;

        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();

        int[] cards = cardArray(this.cards.get(0), this.cards.get(1),
                this.cards.get(2), this.cards.get(3), this.cards.get(4), this.cards.get(5), this.cards.get(6));

        boolean coppia1 = false;
        boolean coppia1_2 = false;
        boolean coppia2 = false;
        boolean coppia2_2 = false;


        int numeroelementi = cards.length;

        for (int i = 0; i <= numeroelementi - 1; i++) {
            if (coppia1 && !coppia1_2) {
                coppia1 = false;
            } else if (coppia2 && !coppia2_2) {
                coppia2 = false;
            } else if ((!coppia1 && !coppia1_2) || (!coppia2 && (!coppia2_2))) {
                for (int j = 0; j <= numeroelementi - 1; j++) {
                    if (i != j) {
                        if (!coppia1) {
                            if (cards[i] == cards[j]) {
                                System.out.println("Prime due carte della prima tripla!");
                                coppia1 = true;
                                tmpIC1 = i;
                                tmpIC2 = j;
                                indexes.add(i);
                                indexes.add(j);
                            }
                        } else if (!coppia1_2) {
                            if (coppia1) {
                                if (cards[i] == cards[j]) {
                                    System.out.println("L'ultima carta della prima tripla!");
                                    coppia1_2 = true;
                                    tmpIC3 = j;
                                    indexes.add(j);
                                }
                            }
                        } else if (!coppia2) {
                            if (cards[i] == cards[j]) {
                                if (j != tmpIC1 && i != tmpIC2 && j != tmpIC2 && i != tmpIC1 && i != tmpIC3 && j != tmpIC3) {
                                    System.out.println("Prime due carte della seconda tripla!");
                                    coppia2 = true;
                                    indexes.add(i);
                                    indexes.add(j);
                                }
                            }
                        } else if (!coppia2_2) {
                            if (coppia2) {
                                if (cards[i] == cards[j]) {
                                    System.out.println("L'ultima carta della seconda tripla!");
                                    coppia2_2 = true;
                                    indexes.add(j);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (coppia1 && coppia1_2 && coppia2 && coppia2_2) {
            priority7 = 7;
            for (int index : indexes) {
                ay.add(this.cards.get(index));
            }
            ay = ordinamento(ay);
        } else if (coppia1 && coppia1_2) {
            priority7 = 7;
            for (int i = 0; i < 3; i++) {
                ay.add(this.cards.get(indexes.get(i)));
            }
            ay = ordinamento(ay);
        }
        return ay;
    }

    public ArrayList<Pair<CardSuit, CardRank>> doppiaCoppia() {

        int tmpIC1 = 0;
        int tmpJC1 = 0;
        int tmpIC2 = 0;
        int tmpJC2 = 0;

        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();
        int[] cards = cardArray(this.cards.get(0), this.cards.get(1),
                this.cards.get(2), this.cards.get(3), this.cards.get(4), this.cards.get(5), this.cards.get(6));
        ArrayList<Integer> indexesCouples = new ArrayList<>();

        boolean coppia1 = false;
        boolean coppia2 = false;
        boolean coppia3 = false;

        for (int i = 0; i <= cards.length - 1; i++) {

            for (int j = 0; j <= cards.length - 1; j++) {
                if (i != j) {
                    if (!coppia1) {
                        if (cards[i] == cards[j]) {
                            coppia1 = true;
                            indexesCouples.add(i);
                            indexesCouples.add(j);
                            tmpIC1 = i;
                            tmpJC1 = j;
                        }
                    } else if (!coppia2) {
                        if (coppia1) {
                            if (j != tmpIC1 && i != tmpJC1) {
                                if (cards[i] == cards[j]) {

                                    if (cards[tmpIC1] != cards[j] && cards[tmpJC1] != cards[j]) {
                                        coppia2 = true;
                                        tmpIC2 = i;
                                        tmpJC2 = j;
                                        indexesCouples.add(i);
                                        indexesCouples.add(j);
                                    } else {
                                        coppia1 = false;
                                    }
                                }


                            }
                        }
                    } else if (!coppia3) {
                        if (coppia2) {
                            if (j != tmpIC1 && i != tmpJC1 && j != tmpIC2 && i != tmpJC2) {
                                if (cards[i] == cards[j]) {

                                    if (cards[tmpIC2] != cards[j] && cards[tmpJC2] != cards[j]) {
                                        coppia3 = true;
                                        indexesCouples.add(i);
                                        indexesCouples.add(j);
                                    } else {
                                        coppia2 = false;
                                    }
                                }

                            }
                        }
                    }
                }
            }
            System.out.println(coppia1 + " " + coppia2 + " " + coppia3);
        }
        if ((coppia1 && coppia2 && coppia3) || (coppia1 && coppia2)) {
            priority8 = 8;
            for (int index : indexesCouples) {
                ay.add(this.cards.get(index));
            }
            ay = ordinamento(ay);

        }
        return ay;
    }

    public ArrayList<Pair<CardSuit, CardRank>> coppia() {
        int tmpI = 0;
        int tmpJ = 0;
        ArrayList<Pair<CardSuit, CardRank>> ay = new ArrayList<>();

        int[] cards = cardArray(this.cards.get(0), this.cards.get(1),
                this.cards.get(2), this.cards.get(3), this.cards.get(4), this.cards.get(5), this.cards.get(6));

        boolean coppia = false;

        for (int i = 0; i <= cards.length - 1; i++) {

            if (coppia)
                break;

            for (int j = 0; j <= cards.length - 1; j++) {
                if (i != j) {
                    if (!coppia) {
                        if (cards[i] == cards[j]) {
                            coppia = true;
                            tmpI = i;
                            tmpJ = j;
                        }
                    } else {
                        if (cards[i] == cards[j])
                            coppia = false;
                    }
                }
            }
        }

        if (coppia) {
            priority9 = 9;
            ay.add(this.cards.get(tmpI));
            ay.add(this.cards.get(tmpJ));
        }

        return ay;
    }

    public Pair<CardSuit, CardRank> cartaPiuAlta() {
        int tmp = 0;
        int[] cards = cardArray(this.cards.get(0), this.cards.get(1),
                this.cards.get(2), this.cards.get(3), this.cards.get(4), this.cards.get(5), this.cards.get(6));
        int massimo = cards[0];

        for (int i = 0; i <= cards.length - 1; i++) {
            if (cards[i] > massimo) {
                massimo = cards[i];
                tmp = i;
            }
        }

        priority10 = 10;
        return this.cards.get(tmp);
    }
}
