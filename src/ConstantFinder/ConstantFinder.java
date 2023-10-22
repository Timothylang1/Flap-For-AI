package ConstantFinder;

import NEAT.Neural_Constants;

public class ConstantFinder {
    
    Thread[] threads = new Thread[Neural_Constants.NUM_OF_VARIABLES * 2];

    public ConstantFinder() {

        Thread thread = new Thread(new ThreadLogic(new Neural_Constants()));
        thread.start();
    }


    public static void main(String[] args) {
        ConstantFinder constantFinder = new ConstantFinder();
    }
}
