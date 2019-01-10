package client.net;

import interfaces.Event;
import server.events.NullEvent;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ClientWrapper {
    private String nickname;
    private static ClientWrapper instance;
    private Client client;

    private ClientWrapper(){
        client = new Client();
    }
    public static ClientWrapper getInstance(){
        if(instance == null)
            instance = new ClientWrapper();
        return instance;
    }

    public void setParameters(String serverName, int serverPort){
        client.setParameters(serverName, serverPort);
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public void writeMessage(Event message){
        client.writeMessage(message);
    }

    public Event readMessage(){
        try {
           return (Event)client.readMessage();
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return new NullEvent();
    }

    public boolean attemptToConnect(){
        try{
            client.attemptToConnect();
            return true;
        }catch(ConnectException | SocketTimeoutException e){
            JOptionPane.showMessageDialog(null, "Nessun server trovato a questo IP! Riprova più tardi.");
            client.close();
            return false;
        }catch(UnknownHostException e){
            JOptionPane.showMessageDialog(null, "L'IP inserito è malformato. Controlla di averlo digitato correttamente.");
            client.close();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return false;
        }
    }

    public void close(){
        client.close();
    }
}
