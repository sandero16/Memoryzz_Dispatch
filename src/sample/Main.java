package sample;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public void start(DBServerInterface stub) throws Exception {

            Registry registry = LocateRegistry.createRegistry(1100);
            DispatchImpl localdispatch=new DispatchImpl(stub);
            registry.rebind("disp", localdispatch);

            Thread thread=new Thread(new GameMergingThread(localdispatch));
            thread.start();

    }
    public static void main(String[] args){
        Main main=new Main();
        try {
            DBServerInterface stub=main.connectToDatabase();
            main.start(stub);
        }catch (Exception e){

        }
    }
    private DBServerInterface connectToDatabase() {
        DBServerInterface stub = null;
        try {
            // fire to localhost port 1099
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 2001);

            // search for CounterService
            stub = (DBServerInterface) myRegistry.lookup("DBService");

            // call server's method

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stub;
    }
}
