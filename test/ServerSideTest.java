import server.automa.StateManager;

public class ServerSideTest {
    public static void main(String [] args){
        StateManager manager = new StateManager();
        manager.startServer();
    }
}
