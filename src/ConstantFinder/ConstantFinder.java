package ConstantFinder;

import java.io.IOException;

import NEAT.Neural_Constants;


public class ConstantFinder {
    
    private ThreadLogic[] runnables = new ThreadLogic[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Thread[] threads = new Thread[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Neural_Constants best_constant = new Neural_Constants();
    private CsvMaker csvMaker = new CsvMaker();

    public ConstantFinder() throws IOException{
        // Create all the runnable objects starting at random points as well as filler threads (we will replace a majority of them)
        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new ThreadLogic();
            threads[i] = new Thread();
        }

        // Randomize the constants
        best_constant.randomize();

        csvMaker.add_to_csv("Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations");
        System.out.println("Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations");
        
        round(); // Does one round
    }

    /*
     * Recursive method that keeps calling itself
     */
    private void round() {
        // First, get the current constants, copy and modify them into the runnables, and run any runnables if nessecary
        for (int i = 0; i < threads.length; i++) {
            Neural_Constants neighbor = best_constant.copy();
            if (neighbor.modify(i / 2, i % 2)) { // If we can modify it with this specific modification, then we need to run it
                runnables[i].reset(neighbor); // Makes the runnable constants the new neighbor constants as well as resets the object
                Thread thread = new Thread(runnables[i]); // Toss in the runnable into a new thread
                thread.start(); // Start the thread
                threads[i] = thread; // Add the thread to the array to join later
            }
        }

        // Then join the threads together so they all finish at the same time (doesn't matter if they're active or not)
        try {
            for (int i = 0; i < threads.length; i++) threads[i].join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Once all the threads are done, find the best score. The less generations it took, the better the score
        int best_generation = Integer.MAX_VALUE;
        for (int i = 0; i < threads.length; i++) {
            if (runnables[i].active && best_generation > runnables[i].total_generations) {
                best_constant = runnables[i].constants;
                best_generation = runnables[i].total_generations;
                runnables[i].active = false; // Sets the runnable back to inactive
            }
        }

        // If one of the scores is better, than we restart the process again
        System.out.println(best_constant + Neural_Constants.format.format((double) best_generation / ConstantsForFinder.NUM_OF_TRIALS));
        csvMaker.add_to_csv(best_constant + Neural_Constants.format.format((double) best_generation / ConstantsForFinder.NUM_OF_TRIALS));
        round();
    }


    public static void main(String[] args) throws IOException {
        new ConstantFinder();
    }
}
