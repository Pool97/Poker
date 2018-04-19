import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocketTest implements Runnable{
    private Socket socket;
    private String nickname;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;

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
            //socket = new Socket(txtIP.getText(), 4040);
        }catch (NullPointerException e3){
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 4040);
        }

		/*String ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
		System.out.println(ip);*/

        System.out.println("CLIENT " + nickname + " -> CONNESSIONE VERSO UN SERVER ...");
        //("CLIENT " + nickname + " -> CONNESSIONE VERSO UN SERVER ...");
        System.out.println("CLIENT " + nickname + " -> CONNESSIONE AVVENUTA VERSO "+ socket.getInetAddress()+" ALLA PORTA REMOTA "+socket.getPort() + " E ALLA PORTA LOCALE " + socket.getLocalPort());
        //updateLog("CLIENT " + nickname + " -> CONNESSIONE AVVENUTA VERSO "+ socket.getInetAddress()+" ALLA PORTA REMOTA "+socket.getPort() + " E ALLA PORTA LOCALE " + socket.getLocalPort());

    }

    private void close() throws IOException {
        System.out.println("CLIENT " + nickname + " -> CHIUSURA CONNESSIONE SOCKET");
        //updateLog("CLIENT " + nickname + " -> CHIUSURA CONNESSIONE SOCKET");
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
        System.out.println("CLIENT " + nickname + " -> STREAM CREATI");
        //updateLog("CLIENT " + nickname + " -> STREAM CREATI");
    }

    /*private void updateLog(String text) {
        ClientFrame.txtLogCl.append(String.format("%4d - %s\n", ClientFrame.txtLogCl.getLineCount(), text));
    }*/
}
