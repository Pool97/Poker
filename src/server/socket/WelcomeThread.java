package server.socket;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class WelcomeThread implements Callable<Integer> {
    private Socket clientSocket;
    private ObjectInputStream input_stream;
    private ObjectOutputStream output_stream;
    private String nickname;
    private final Logger logger = Logger.getLogger(ServerSocketManager.class.getName());

    public WelcomeThread(Socket client){
        this.clientSocket = client;

    }

    @Override
    public Integer call(){
        int maxPlayers = 0;
        try {
            createStreams();
            maxPlayers = initProcessing();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return maxPlayers;
    }

    private int initProcessing() throws IOException, ClassNotFoundException {
        nickname = (String)input_stream.readObject();
        int playersNumber = input_stream.readInt();
        return playersNumber;
    }

    private void close() throws IOException {
        logger.info("SERVER -> CHIUSURA CONNESSIONE SOCKET ");
        if(output_stream != null && input_stream != null && clientSocket != null) {
            output_stream.close();
            input_stream.close();
            //clientSocket.close();
        }
    }

    private void createStreams () throws IOException {
        output_stream = new ObjectOutputStream(clientSocket.getOutputStream());
        output_stream.flush();
        input_stream = new ObjectInputStream(clientSocket.getInputStream());
        logger.info("SERVER -> STREAM " + " CREATI");
    }
}
