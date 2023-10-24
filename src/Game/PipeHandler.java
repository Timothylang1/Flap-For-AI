package Game;
import java.util.ArrayList;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

/*
 * Controls the movement of the pipes
 */
public class PipeHandler {

    private GraphicsGroup pipes = new GraphicsGroup();
    private ArrayList<Image> pipe_images = new ArrayList<Image>();
    private int tracker = 0; // Tracks the indexes of the current pipes that the bird can interact with
    private final Random RAND = new Random();

    public PipeHandler() {
        reset();
    }

    /*
     * Removes all pipes and generates new ones to replace them
     */
    public void reset() {
        pipes.removeAll();
        pipe_images.clear();
        double x = 0;
        while (x < Constants.CANVAS_WIDTH) {
            // Generate pipes
            createPipes();
            x += Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES;
        }

        // Generates one more pipe so that we can't see the pipe being generated as it slides past
        createPipes();
        
        // Reset tracker
        tracker = 0;
    }

    /*
     * Adds pipes to the canvas
     */
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
        pipe_images.get(tracker).getElementAt(SE.getX(), SE.getY()) != null || // Check colisions with lower pipe
        pipe_images.get(tracker).getElementAt(SW.getX(), SW.getY()) != null || 
        pipe_images.get(tracker + 1).getElementAt(NE.getX(), NE.getY()) != null || // Check collisions with upper pipe
        pipe_images.get(tracker + 1).getElementAt(NW.getX(), NW.getY()) != null;
    }

    /*
     * Creates lower pipe, then upper pipe at calculated values
     */
    private void createPipes() {
        Image lowerPipe = new Image(0, 0, "Final/Pipe.png");
        lowerPipe.setScale(Constants.PIPE_SCALE);
        Image upperPipe = new Image(0, 0, "Final/Pipe.png");
        upperPipe.setScale(Constants.PIPE_SCALE);
        upperPipe.rotateBy(180);
        calculatePlacement(lowerPipe, upperPipe);
        pipes.add(lowerPipe);
        pipes.add(upperPipe);
        pipe_images.add(lowerPipe);
        pipe_images.add(upperPipe);
    }

    /*
     * Calculates placement of pipes
     */
    private void calculatePlacement(Image lower_pipe, Image upper_pipe) {
        double y_placement = RAND.nextDouble() * (Constants.UPPER_BACKGROUND_HEIGHT - Constants.VERTICAL_DISTANCE_BETWEEN_PIPES);
        double x_placement = calculateX();
        lower_pipe.setCenter(x_placement, y_placement + Constants.VERTICAL_DISTANCE_BETWEEN_PIPES + Constants.PIPE_HEIGHT / 2);
        upper_pipe.setCenter(x_placement, -Constants.PIPE_HEIGHT / 2 + y_placement);
    }

    /*
     * Calculates the X coordinate of where the pipe should be placed
     */
    private double calculateX() {
        if (pipe_images.size() == 0) { // This is only for the initial case when they're no images in the list yet. Spawns pipe offscreen
            return Constants.PIPE_STARTING_LOCATION_X;
        }
        return (pipe_images.get(pipe_images.size() - 1).getCenter().getX() + Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES);
    }

    /*
     * Moves all the pipes, and removes them as they move off the screen
     */
    public void move() {
        pipe_images.forEach(x -> x.moveBy(-Constants.GAMESPEED, 0));
        if (pipe_images.get(0).getCenter().getX() < -Constants.PIPE_WIDTH) {
            Image lower_pipe = pipe_images.remove(0); // Remove lower pipe
            Image upper_pipe = pipe_images.remove(0); // Remove upper pipe
            calculatePlacement(lower_pipe, upper_pipe); // Calculate new placement
            pipe_images.add(lower_pipe); // Add them back in, but at then end of the arraylist
            pipe_images.add(upper_pipe);
            tracker -= 2; // Shifts the index by two
        }
        // Updates which pipe is currently the potential one for birds to collide into. If the birds successfully cross, then we update the score
        if (pipe_images.get(tracker).getCenter().getX() + Constants.PIPE_WIDTH / 2 < Constants.STARTING_BIRD_X - Constants.BIRD_SIZE_X / 2) {
            tracker += 2; // Shift the index to the next series of pipes
        }
    }

    /*
     * Returns Y location of the gap for the next 2 pipe sets
     */
    public double[] getCurrentPipesY() {
        return new double[]{
            pipe_images.get(tracker).getCenter().getY() - Constants.PIPE_HEIGHT / 2, // Lower pipe of the upcoming set
            pipe_images.get(tracker + 1).getCenter().getY() + Constants.PIPE_HEIGHT / 2, // Upper pipe of the upcoming set
            pipe_images.get(tracker + 2).getCenter().getY() - Constants.PIPE_HEIGHT / 2, // Lower pipe for the next incoming set
            pipe_images.get(tracker + 3).getCenter().getY() + Constants.PIPE_HEIGHT / 2, // Upper pipe for the next incoming set
        };
    }

    /*
     * Returns the distance between the bird and the next two sets of pipes
     */
    public double[] getCurrentPipesX() {
        return new double[] {
            pipe_images.get(tracker).getCenter().getX() - Constants.PIPE_WIDTH / 2 - Constants.STARTING_BIRD_X - Constants.BIRD_SIZE_X / 2, // Distance between the edge of the pipe to the edge of the bird
            pipe_images.get(tracker + 2).getCenter().getX() - Constants.PIPE_WIDTH / 2 - Constants.STARTING_BIRD_X - Constants.BIRD_SIZE_X / 2 // Distance between the edge of the next set of pipes to the edge of the bird
        };
    }
}
