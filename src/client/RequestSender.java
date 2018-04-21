package client;

import server.interfaces.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class RequestSender<T extends Message> implements Runnable{
    private Socket socket;

    private T message;

    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;
    private static Logger logger = Logger.getLogger(ClientSocketManager.class.getName());



    private final static String CLOSING_STREAMS_INFO = " CHIUSURA DEGLI STREAMS \n";
    private final static String STREAMS_CREATION_INFO = " STREAMS CREATI \n";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";
    private final static String CLIENT_INFO = "CLIENT -> ";


    public RequestSender(Socket socket, T message){
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        createStreams();
        try {
            output_stream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }



    private void close(){
        logger.info(CLIENT_INFO + CLOSING_STREAMS_INFO);

        if(output_stream != null && input_stream != null) {
            try {
                output_stream.close();
                input_stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(CLIENT_INFO + SERVICE_INTERRUPTED);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void createStreams(){
        try {
            output_stream = new ObjectOutputStream(socket.getOutputStream());
            output_stream.flush();
            input_stream = new ObjectInputStream(socket.getInputStream());
            logger.info(CLIENT_INFO  + STREAMS_CREATION_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(CLIENT_INFO  + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }


}
