package server.controller;

import server.events.EventsContainer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventDispatcher {
    private ExecutorService dispatcher = Executors.newCachedThreadPool();

    public void sendMessage(Runnable runnable) {
        try {
            dispatcher.submit(runnable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public EventsContainer receiveMessageFrom(PlayerController playerController) throws ExecutionException, InterruptedException {
        return dispatcher.submit(playerController.getTaskForReceive()).get();
    }
}