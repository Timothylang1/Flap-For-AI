package ConstantFinder;

import Game.Background;
import Game.Constants;
import Game.PipeHandler;
import Game.Score;
import GraphVisualization.GraphVisual;
import NEAT.NEAT;
import NEAT.Neural_Constants;
import edu.macalester.graphics.CanvasWindow;

public class ThreadLogic implements Runnable {
    private final CanvasWindow canvas = new CanvasWindow("Flap For AI", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    private final PipeHandler pipes = new PipeHandler();
    private final NEAT neat;
    private double current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;
    private int score_tracker = 0;

    public ThreadLogic(Neural_Constants constants) {
        canvas.closeWindow();
        neat = new NEAT(pipes, constants);

        // Add objects to canvas in correct order
        pipes.addPipesGroup(canvas);
        neat.addBirds(canvas);
    }

    /*
     * Resets everything
     */
    private void restartGame() {
        pipes.reset();
        neat.reset();
    }

    @Override
    public void run() {
        while (true) {
            // Pipes
            pipes.move();

            // We've successfully completed the game if a bird reaches the max score, then we can close the game
            if (Neural_Constants.MAX_SCORE == score_tracker) {
                restartGame();
                break;
            }

            // If all the birds have died (i.e. move returns false), then we reset the game and the neural networks
            if (!neat.move()) {
                restartGame();
            }
        }
    }
}
