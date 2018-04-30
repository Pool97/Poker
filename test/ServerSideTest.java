import server.automa.Match;


public class ServerSideTest {
    public static void main(String [] args){
        Match manager = new Match();
        manager.startServer();
        while (true) {
            manager.start();
        }
    }
}
