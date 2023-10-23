import Game.*;
import GraphVisualization.GraphVisual;
import NEAT.NEAT;
import NEAT.Neural_Constants;
import edu.macalester.graphics.*;

/*
 * Main class that brings all the classes together, and exectutes the main loop of the game
 */
public class Game {
    private final CanvasWindow canvas = new CanvasWindow("Flap For AI", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    private final CanvasWindow canvas2 = new CanvasWindow("Neural Network", Constants.CANVAS2_WIDTH, Constants.CANVAS2_HEIGHT);
    private final PipeHandler pipes = new PipeHandler();
    private final Background back = new Background();
    private final Neural_Constants constants = new Neural_Constants();
    private final Score score = new Score();
    private final NEAT neat = new NEAT(pipes);
    private final GraphVisual gv = new GraphVisual(canvas2);
    private boolean complete = false;

    public Game() {
        // Add objects to canvas in correct order
        back.addUpperBackground(canvas);
        pipes.addPipesGroup(canvas);
        back.addLowerBackground(canvas);
        score.addScore(canvas);
        neat.addBirds(canvas);

        neat.updateConstants(constants);

        canvas.animate(() -> {
            if (!complete) {
                for (int i = 0; i < Constants.FRAMESPEEDSCALAR; i++) {
                    // Move background visuals
                    back.move();
                    pipes.move();

                    // Update the score
                    score.updateScore();

                    // We've successfully completed the game if a bird reaches the max score, then we can close the game
                    if (Neural_Constants.MAX_SCORE == score.score) {
                        restartGame();
                        canvas.closeWindow();
                        complete = true;
                    }

                    // If all the birds have died (i.e. move returns false), then we reset the game and the neural networks
                    if (!neat.move()) {
                        restartGame();
                    }
                }
            }
        });
    }

    /*
     * Resets everything
     */
    private void restartGame() {
        back.reset();
        pipes.reset();
        gv.reset(neat.getBest().genes, neat.getNumSpecies(), score.score); // Updates the visualizer
        neat.reset();
        score.reset();
    }

    public static void main(String[] args) {
        new Game();
    }
}
