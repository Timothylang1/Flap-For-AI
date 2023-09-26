import Game.*;
import edu.macalester.graphics.*;


public class Game {    

    private CanvasWindow canvas = new CanvasWindow("Flap For AI", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    private PipeHandler pipes;
    private Background back;
    private Bird bird;
    private Score score;

    public Game() {
        restartGame();
        System.out.println(canvas.getWidth() + " " + canvas.getHeight());
        canvas.animate(() -> {
            back.move();
            pipes.move();
            score.updateScore(); 
            if (!bird.move()) {
                restartGame();
            }
        });

        canvas.onKeyDown(keys -> {
            bird.rise();
        });

        canvas.onClick(point -> {
            System.out.println(point.getPosition());
        });
    }

    private void restartGame() {
        pipes = new PipeHandler();
        score = new Score(pipes);
        back = new Background();
        bird = new Bird(pipes);

        canvas.removeAll();
        back.addUpperBackground(canvas);
        pipes.addPipesGroup(canvas);
        back.addLowerBackground(canvas);
        bird.addBird(canvas);
        score.addScore(canvas);
        
    }

    public static void main(String[] args) {
        new Game();
    }
}
