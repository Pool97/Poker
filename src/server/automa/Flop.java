package server.automa;

import interfaces.PokerState;

import java.util.concurrent.CountDownLatch;


public class Flop implements PokerState {

    public Flop() {
        System.out.println("Sono al Flop/River!");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //ottieni le prime tre carte per la Community Cards (in realtà è da bruciare una carta per ogni carta della community)
        //deckModel.nextCard();
        //deckModel.nextCard();
        //deckModel.nextCard();
        //notifyClients()
        //update Observers aggiornare i clients della modifica della Community Cards
    }

    @Override
    public void goNext() {

    }
}
