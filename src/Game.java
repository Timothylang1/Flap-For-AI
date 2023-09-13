import edu.macalester.graphics.*;


public class Game {    

    public Game() {
        
        CanvasWindow canvas = new CanvasWindow("Testing", Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        Background back = new Background(canvas);
        PipeHandler pipes = new PipeHandler(canvas);
        Bird bird = new Bird();
        canvas.add(bird);
        canvas.animate(() -> {
            bird.fall();
            if (bird.getCenter().getY() > 200) {
                bird.rise();
            }
        });

        // canvas.animate(() -> {
        //     back.move();
        // });
        // Image top = new Image(0, 0, "Final/UpperBackground.png");
        // Image bot = new Image(0, top.getHeight(), "Final/LowerBackground.png");
        // Image pipe1 = new Image(0, 0, "Final/Pipe.png");
        // pipe1.setScale(0.2);
        // pipe1.setCenter(50, 300);
        // Image pipe2 = new Image(0, 0, "Final/Pipe.png");
        // pipe2.setScale(0.2);
        // pipe2.rotateBy(180);
        // pipe2.setCenter(50, -100);
        // canvas.add(top);
        // canvas.add(pipe1);
        // canvas.add(pipe2);
        // canvas.add(bot);
        // Bird bird = new Bird();
        // canvas.add(bird);
        // canvas.animate(() -> {
        //     bird.fall();
        // });
        // canvas.onKeyDown(keys -> {
        //     bird.rise();
        // });

        canvas.onClick(point -> {
            System.out.println(point.getPosition());
        });
    }

    public static void main(String[] args) {
        new Game();
    }
}
