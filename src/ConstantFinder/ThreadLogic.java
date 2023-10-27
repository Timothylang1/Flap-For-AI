package ConstantFinder;

import Game.Constants;
import Game.PipeHandler;
import NEAT.NEAT;
import NEAT.Neural_Constants;

public class ThreadLogic implements Runnable {
    private final PipeHandler pipes = new PipeHandler();
    private final NEAT neat = new NEAT(pipes);
    private double current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;
    public int total_generations = Integer.MAX_VALUE; // Takes care of initial case in ConstantFinder
    public Neural_Constants constants;
    public boolean active = false; // Tells if this runnable was used in the most recent generations

    /*
     * Resets for the next round of a trial
     */
    private void restartGame() {
        pipes.reset();
        neat.reset();
        current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;
    }

    /*
     * Updates the score
     */
    private int updateScore(int score) {
        current_pipe_location -= Constants.GAMESPEED;
        if (current_pipe_location <= Constants.STARTING_BIRD_X) {
            current_pipe_location += Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES;
            return score + 1;
        }
        return score;
    }

    /*
     * Resets the entire runnable object for the next trials
     */
    public void reset(Neural_Constants constants) {
        this.constants = constants;
        current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;
        total_generations = 0;
        neat.updateConstants(constants);
        pipes.reset();
        active = true; // Sets this runnable status to active
    }

    @Override
    public void run() {
        // For each trial
        for (int trial = 0; trial < ConstantsForFinder.NUM_OF_TRIALS; trial++) {
            
            // Temporary counters
            int num_of_generations = 0;
            int score = 0;

            // Run simulation
            while (true) {

                // Pipes
                pipes.move();

                // Update score
                score = updateScore(score);

                // We've successfully completed the game if a bird reaches the max score, then we can close the game
                if (Neural_Constants.MAX_SCORE == score) break;

                // If all the birds have died (i.e. move returns false), then we reset the game and the neural networks
                if (!neat.move()) {
                    restartGame();

                    // If we hit the max number of generations we allow, then we break out of the while loop as well
                    num_of_generations += 1;
                    if (num_of_generations == ConstantsForFinder.MAXIMUM_GENERATIONS) break;
                }

            }
            total_generations += num_of_generations;
            
            // Resets neat for the trial
            neat.updateConstants(constants);
            pipes.reset();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ThreadLogic thread = new ThreadLogic();
        thread.reset(new Neural_Constants());
        thread.run();
        System.out.println("Average: " + Double.toString(thread.total_generations / (double) ConstantsForFinder.NUM_OF_TRIALS));
        long finish = System.currentTimeMillis();
        System.out.println(finish - start); // 5315
    }
}

// 15.37
// 15.26
// 15.16
// 14.65
// 15.43
// 16.03

