import server.automa.MatchHandler;


public class ServerSideTest {
    public static void main(String [] args){
        MatchHandler manager = new MatchHandler();
        manager.startServer();
        while (true) {
            manager.start();
        }
    }
}
