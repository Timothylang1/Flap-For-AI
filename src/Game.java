import edu.macalester.graphics.*;


public class Game {    

    public Game() {
        
        CanvasWindow canvas = new CanvasWindow("Testing", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        PipeHandler pipes = new PipeHandler();
        Background back = new Background(canvas, pipes);
        Bird bird = new Bird(pipes);
        canvas.add(bird);
        canvas.animate(() -> {
            back.move();
            pipes.move();
            bird.move();
            
        });

        canvas.onKeyDown(keys -> {
            bird.rise();
        });

        canvas.onClick(point -> {
            System.out.println(point.getPosition());
        });
    }

    public static void main(String[] args) {
        new Game();
    }
}
