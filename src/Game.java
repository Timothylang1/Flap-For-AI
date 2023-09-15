import edu.macalester.graphics.*;


public class Game {    

    private CanvasWindow canvas = new CanvasWindow("Flap For AI", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
    private PipeHandler pipes;
    private Background back;
    private Bird bird;

    public Game() {
        restartGame();
        canvas.animate(() -> {
            back.move();
            pipes.move();
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
        canvas.removeAll();
        PipeHandler pipes = new PipeHandler();
        Background back = new Background(canvas, pipes);
        Bird bird = new Bird(pipes, canvas);
    }

    public static void main(String[] args) {
        new Game();
    }
}
