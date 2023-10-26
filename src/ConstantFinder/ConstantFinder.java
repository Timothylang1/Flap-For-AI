package ConstantFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import NEAT.Neural_Constants;


public class ConstantFinder {
    
    private ThreadLogic[] runnables = new ThreadLogic[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Thread[] threads = new Thread[Neural_Constants.NUM_OF_VARIABLES * 2];
    private Neural_Constants best_constant;
    private Neural_Constants previous_best;
    private int best_generation;
    private File csvFile;
    private StringBuilder builder;
    private CsvMaker csvMaker;


    public ConstantFinder() throws IOException{
        // Create all the runnable objects starting at random points as well as filler threads (we will replace a majority of them)
        csvFile = new File("./src/ConstantFinder/GenerationsData.csv");
        builder = new StringBuilder();
        csvMaker = new CsvMaker(csvFile, builder);

        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new ThreadLogic();
            threads[i] = new Thread();
        }

        csvMaker.add_to_csv("Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations");

        while (true) {
            System.out.println("Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess\tAverage generations");
            reset();
            round(); // Does one round
        }
    }

    /*
     * Creates a new neural constant, then randomizes it
     */
    private void reset() {
        best_constant = new Neural_Constants();
        best_constant.randomize();
        previous_best = best_constant;
        best_generation = Integer.MAX_VALUE; // To help with initial case
    }

    /*
     * Recursive method that keeps calling itself until we find a local maximum or absolute maximum
     */
    private void round() throws IOException{
        // First, get the current constants, copy and modify them into the runnables, and run any runnables if nessecary
        for (int i = 0; i < threads.length; i++) {
            Neural_Constants neighbor = best_constant.copy();
            if (neighbor.modify(i / 2, i % 2) && !neighbor.equals(previous_best)) { // If we can modify it with this specific modification, then we need to run it
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

        // Set the previous best to be the current generation
        previous_best = best_constant;

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
            System.out.println(best_constant + Neural_Constants.format.format((double) best_generation / ConstantsForFinder.NUM_OF_TRIALS));
            csvMaker.add_to_csv(best_constant + Neural_Constants.format.format((double) best_generation / ConstantsForFinder.NUM_OF_TRIALS));
            round();
        }
        
        // If none of the scores are better, than we have reached a local or final maximum
        else {
            System.out.println("Best generation found");
        }
    }

    /**
     * Closes the FileWriter, allowing the CSV file to be written.
     * @throws IOException
     */
    public void make_csv() throws IOException{
        csvMaker.finish_csv();
    }


    public static void main(String[] args) throws IOException {
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
        ConstantFinder constantFinder = new ConstantFinder(); // 2446.4
        constantFinder.make_csv();
    }
}
