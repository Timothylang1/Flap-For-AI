import Game.*;
import edu.macalester.graphics.*;


public class Game {    

    private final CanvasWindow canvas = new CanvasWindow("Flap For AI", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    private final PipeHandler pipes = new PipeHandler();
    private final Background back = new Background();
    private final Score score = new Score();
    private final NEAT neat;

    public Game() {
        // Add objects to canvas in correct order
        back.addUpperBackground(canvas);
        pipes.addPipesGroup(canvas);
        back.addLowerBackground(canvas);
        score.addScore(canvas);
        
        // NEAT needs to be created inside the constructor so that all the birds are visible when we add them to the canvas last
        neat = new NEAT(pipes, canvas);

        canvas.animate(() -> {
            back.move();
            pipes.move();
            score.updateScore();
            if (!neat.move()) { // If all the birds have died (i.e. move returns false), then we reset the game and the neural networks
                restartGame();
            }
        });

        // canvas.onClick(point -> {
        //     System.out.println(point.getPosition());
        // });
    }

    private void restartGame() {
        back.reset();
        pipes.reset();
        score.reset();
        neat.reset();
    }

    public static void main(String[] args) {
        new Game();
    }
}
