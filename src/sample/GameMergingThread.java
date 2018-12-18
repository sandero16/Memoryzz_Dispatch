package sample;

public class GameMergingThread implements Runnable {
    private DispatchImpl dispatchImpl;
    public GameMergingThread(DispatchImpl dispatchImpl){
        this.dispatchImpl=dispatchImpl;
    }

    @Override
    public void run() {
        while (true) {
            dispatchImpl.mergeServers();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                System.out.println(ie);
            }
        }
    }
}
