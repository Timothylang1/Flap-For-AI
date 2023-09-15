import java.util.ArrayList;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

public class PipeHandler {

    private final Random RAND = new Random();
    private GraphicsGroup pipes = new GraphicsGroup();
    private ArrayList<Image> pipe_images = new ArrayList<Image>();
    private final double WITDH_OF_PIPE = new Image(0, 0, "Final/Pipe.png").getWidth() * Constants.PIPE_SCALE;
    private Image lower_pipe;
    private Image upper_pipe;
    
    public PipeHandler() {
        double x = 0;
        while (x < Constants.CANVAS_WIDTH) {
            // Generate pipes
            createPipe();
            x += Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES;
        }
        // Generates one more pipe so that we can't see the pipe being generated as it slides past
        createPipe();
        // Assign the closest pipe as the main pipe that birds can collide into
        lower_pipe = pipe_images.get(0);
        upper_pipe = pipe_images.get(1);

    }

    public void addPipesGroup(CanvasWindow canvas) {
        canvas.add(pipes);
    }

    /*
     * Return true if a bird collides with a pipe
     */
    public boolean checkCollisionPipe(double x, double y) {
        // Calculate 4 points of collision (4 corners of bird)
        Point NE = new Point(x + Constants.BIRD_SIZE_X / 2, y - Constants.BIRD_SIZE_Y / 2);
        Point SE = new Point(x + Constants.BIRD_SIZE_X / 2, y + Constants.BIRD_SIZE_Y / 2);
        Point SW = new Point(x - Constants.BIRD_SIZE_X / 2, y + Constants.BIRD_SIZE_Y / 2);
        Point NW = new Point(x - Constants.BIRD_SIZE_X / 2, y - Constants.BIRD_SIZE_Y / 2);

        // Check for collision
        return 
        lower_pipe.getElementAt(SE.getX(), SE.getY()) != null || 
        lower_pipe.getElementAt(SW.getX(), SW.getY()) != null || 
        upper_pipe.getElementAt(NE.getX(), NE.getY()) != null || 
        upper_pipe.getElementAt(NW.getX(), NW.getY()) != null;
    }

    /*
     * Creates lower pipe, then upper pipe at calculated values
     */
    private void createPipe() {
        Image lowerPipe = new Image(0, 0, "Final/Pipe.png");
        lowerPipe.setScale(Constants.PIPE_SCALE);
        Image upperPipe = new Image(0, 0, "Final/Pipe.png");
        upperPipe.setScale(Constants.PIPE_SCALE);
        upperPipe.rotateBy(180);
        double y_placement = RAND.nextDouble() * (Constants.UPPER_BACKGROUND_HEIGHT - Constants.VERTICAL_DISTANCE_BETWEEN_PIPES);
        double x_placement = calculateX();
        lowerPipe.setCenter(x_placement, y_placement + Constants.VERTICAL_DISTANCE_BETWEEN_PIPES + lowerPipe.getHeight() * Constants.PIPE_SCALE / 2);
        upperPipe.setCenter(x_placement, -upperPipe.getHeight() * Constants.PIPE_SCALE / 2 + y_placement);
        pipes.add(lowerPipe);
        pipes.add(upperPipe);
        pipe_images.add(lowerPipe);
        pipe_images.add(upperPipe);
    }

    private double calculateX() {
        if (pipe_images.size() == 0) { // This is only for the initial case when they're no images in the list yet. Spawns pipe offscreen
            return Constants.CANVAS_WIDTH + WITDH_OF_PIPE;
        }
        return (pipe_images.get(pipe_images.size() - 1).getCenter().getX() + Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES);
    }

    public void move() {
        if (pipe_images.get(0).getCenter().getX() < -WITDH_OF_PIPE) {
            pipes.remove(pipe_images.remove(0)); // Remove lower pipe
            pipes.remove(pipe_images.remove(0)); // Remove upper pipe
            createPipe();
        }
        pipe_images.forEach(x -> x.moveBy(-Constants.GAMESPEED, 0));
        // Updates which pipe is currently the potential one for birds to collide into
        if (lower_pipe.getCenter().getX() + Constants.PIPE_WIDTH < Constants.STARTING_BIRD_X - Constants.BIRD_SIZE_X / 2) {
            lower_pipe = pipe_images.get(pipe_images.index(lower_pipe) + 2);
            upper_pipe = pipe_images.get(pipe_images.index(upper_pipe) + 2);
        }
    }
}
