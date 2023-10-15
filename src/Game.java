import Game.*;
import NEAT.NEAT;
import edu.macalester.graphics.*;

/*
 * Main class that brings all the classes together, and exectutes the main loop of the game
 */
public class Game {
    private final CanvasWindow canvas2 = new CanvasWindow("Neural Network", 500, 500);
    private final CanvasWindow canvas = new CanvasWindow("Flap For AI", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    private final PipeHandler pipes = new PipeHandler();
    private final Background back = new Background();
    private final Score score = new Score();
    private final NEAT neat = new NEAT(pipes);

    public Game() {
        // Add objects to canvas in correct order
        back.addUpperBackground(canvas);
        pipes.addPipesGroup(canvas);
        back.addLowerBackground(canvas);
        score.addScore(canvas);
        neat.addBirds(canvas);

        canvas.animate(() -> {
            for (int i = 0; i < Constants.FRAMESPEEDSCALAR; i++) {
                back.move();
                pipes.move();
                score.updateScore();
                if (!neat.move()) { // If all the birds have died (i.e. move returns false), then we reset the game and the neural networks
                    restartGame();
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
        score.reset();
        // neat.drawBest(canvas2);
        neat.reset();
    }

    public static void main(String[] args) {
        new Game();
    }
}
