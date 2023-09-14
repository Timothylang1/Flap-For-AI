import java.util.ArrayList;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;

public class PipeHandler {

    private final Random RAND = new Random();
    private GraphicsGroup pipes = new GraphicsGroup();
    private ArrayList<Image> pipe_images = new ArrayList<Image>();
    private final double WITDH_OF_PIPE = new Image(0, 0, "Final/Pipe.png").getWidth() * Constants.PIPE_SCALE;
    
    public PipeHandler() {
        double x = 0;
        while (x < Constants.CANVAS_WIDTH) {
            // Generate pipes
            createPipe();
            x += Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES;
        }
        // Generates one more pipe so that we can't see the pipe being generated as it slides past
        createPipe();
    }

    public void addPipesGroup(CanvasWindow canvas) {
        canvas.add(pipes);
    }

    public boolean checkCollisionPipe(double x, double y) {
        return false;
     
    }

    private void createPipe() {
        Image lowerPipe = new Image(0, 0, "Final/Pipe.png");
        lowerPipe.setScale(Constants.PIPE_SCALE);
        Image upperPipe = new Image(0, 0, "Final/Pipe.png");
        upperPipe.setScale(Constants.PIPE_SCALE);
        upperPipe.rotateBy(180);
        double y_placement = RAND.nextDouble() * (Constants.UPPER_BACKGROUND_HEIGHT - Constants.VERTICAL_DISTANCE_BETWEEN_PIPES);
        double x_placement = calculateX();
        upperPipe.setCenter(x_placement, -upperPipe.getHeight() * Constants.PIPE_SCALE / 2 + y_placement);
        lowerPipe.setCenter(x_placement, y_placement + Constants.VERTICAL_DISTANCE_BETWEEN_PIPES + lowerPipe.getHeight() * Constants.PIPE_SCALE / 2);
        pipes.add(upperPipe);
        pipes.add(lowerPipe);
        pipe_images.add(upperPipe);
        pipe_images.add(lowerPipe);
    }

    private double calculateX() {
        if (pipe_images.size() == 0) { // This is only for the initial case when they're no images in the list yet. Spawns pipe offscreen
            return Constants.CANVAS_WIDTH + WITDH_OF_PIPE;
        }
        return (pipe_images.get(pipe_images.size() - 1).getCenter().getX() + Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES);
    }

    public void move() {
        if (pipe_images.get(0).getCenter().getX() < -WITDH_OF_PIPE) {
            pipes.remove(pipe_images.remove(0)); // Remove upper pipe
            pipes.remove(pipe_images.remove(0)); // Remove lower pipe
            createPipe();
        }
        pipe_images.forEach(x -> x.moveBy(-Constants.GAMESPEED, 0));
    }
}
