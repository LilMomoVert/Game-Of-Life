package gol.Model;


import java.util.ArrayList;
/**
 * Created by Kani Boyka on 5/4/2017.
 */

public class GameThreads {


    private ArrayList<Thread> workers = new ArrayList<>();


    public GameThreads() {

    }

    public void addThreads(Runnable runnable){
           workers.add(new Thread(runnable));
            System.out.println("I GOT ADDED");
    }


    public void runThreads() throws InterruptedException {
//        workers.forEach(Thread::start);
        for (Thread thread : workers){
            thread.start();
            System.out.println("STARTED");
        }
        for(Thread thread : workers){
            thread.join();
            System.out.println("I JOINED");
        }
        clearWorkers();
    }



    public void clearWorkers(){
        workers.clear();
        System.out.println("ALL CLEARED");
    }
}