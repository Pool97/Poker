package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;


public class ClientSocketTest implements Runnable{
    private Socket socket;
    private String nickname;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;
    private static Logger logger = Logger.getLogger(ClientSocketTest.class.getName());

    public ClientSocketTest(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void run() {
        try{
            attemptToConnect();
            createStreams();
            //initProcessing();
        }catch(IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void attemptToConnect() throws IOException {
        try {
            logger.info("CLIENT " + nickname + " -> CONNESSIONE VERSO UN SERVER ...");
            socket = new Socket("localhost", 4040);
            logger.info("CLIENT " + nickname + " -> CONNESSIONE AVVENUTA VERSO "+ socket.getInetAddress()+" ALLA PORTA REMOTA "+socket.getPort() + " E ALLA PORTA LOCALE " + socket.getLocalPort());
        }catch (NullPointerException e3){
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 4040);
        }

    }

    private void close() throws IOException {
        logger.info("CLIENT " + nickname + " -> CHIUSURA CONNESSIONE SOCKET");
        if(output_stream != null && input_stream != null && socket != null) {
            output_stream.close();
            input_stream.close();
            socket.close();
        }
    }

    private void createStreams() throws IOException {
        output_stream = new ObjectOutputStream(socket.getOutputStream());
        output_stream.flush();

        input_stream = new ObjectInputStream(socket.getInputStream());
        logger.info("CLIENT " + nickname + " -> STREAM CREATI");
    }

    public static void main(String [] args){
        new Thread(new ClientSocketTest("Tunsi97")).start();
        new Thread(new ClientSocketTest("Perry97")).start();
    }
}
