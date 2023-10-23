package ConstantFinder;

import NEAT.Neural_Constants;

public class ConstantFinder {
    
    private ThreadLogic[] runnables = new ThreadLogic[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Thread[] threads = new Thread[Neural_Constants.NUM_OF_VARIABLES * 2];

    public ConstantFinder() {

        Thread thread = new Thread(new ThreadLogic());
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    private void reset() {

    }


    public static void main(String[] args) {
        ConstantFinder constantFinder = new ConstantFinder();
    }
}
