package ConstantFinder;

import NEAT.Neural_Constants;

public class ConstantFinder {
    
    private ThreadLogic[] runnables = new ThreadLogic[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Thread[] threads = new Thread[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Neural_Constants best_constant;
    private int best_generation = Integer.MAX_VALUE; // To help handle with initial case

    public ConstantFinder() {
        // Create all the runnable objects starting at random points as well as filler threads (we will replace a majority of them)
        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new ThreadLogic();
            threads[i] = new Thread();
        }

        System.out.println("Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations");
        reset();
        round(); // Does one round
    }

    /*
     * Creates a new neural constant, then randomizes it
     */
    private void reset() {
        best_constant = new Neural_Constants();
        best_constant.randomize();
        System.out.println(best_constant);
    }

    /*
     * Recursive method that keeps calling itself until we find a local maximum or absolute maximum
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
        boolean found_better = false;
        for (int i = 0; i < threads.length; i++) {
            if (best_generation > runnables[i].total_generations) {
                best_constant = runnables[i].constants;
                best_generation = runnables[i].total_generations;
                found_better = true;
            }
        }

        // long finish = System.currentTimeMillis();
        // System.out.println("Average time: " + Double.toString(((double) (finish - start)) / threads.length));

        // If one of the scores is better, than we restart the process again
        if (found_better) {
            System.out.println(best_constant);
            round();
        }
        
        // If none of the scores are better, than we have reached a local or final maximum
        else {
            System.out.println("Best generation found");
        }
    }


    public static void main(String[] args) {
        // Thread[] temp = new Thread[5];
        // for (int i = 0; i < 5; i++) {
        //     Thread thread = new Thread(new Runnable() {
        //         @Override
        //         public void run() {
        //             try {
        //                 Thread.sleep(5000);
        //                 System.out.println("This ran");

        //             } catch (Exception e) {e.printStackTrace();}
        //         }
        //     });
        //     // thread.start();
        //     temp[i] = thread;
        // }

        // try {
        //     for (int i = 0; i < temp.length; i++) temp[i].join();
        // } catch (Exception e) {e.printStackTrace();}

        // System.out.println("Final print statement");
        new ConstantFinder(); // 2446.4
    }
}
