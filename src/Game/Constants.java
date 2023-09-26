package Game;
import edu.macalester.graphics.Image;

public class Constants {

    // Ulitimate scale for size
    public static final double ULTIMATE_SCALE = 1;

    // All variables related to the speed/hardness of the game
    public static final double GAMESPEED = 1.7; // How fast pipes are incoming
    public static final double GRAVITY = 0.5; // How fast the bird falls
    public static final double JUMPSPEED = 6; // How fast the bird rises

    // Difficulty
    public static final double VERTICAL_DISTANCE_BETWEEN_PIPES = 90 * ULTIMATE_SCALE;    
    public static final double HORIZONTAL_DISTANCE_BETWEEN_PIPES = 145 * ULTIMATE_SCALE;

    // Bird
    public static final double STARTING_BIRD_X = 100;
    public static final double STARTING_BIRD_Y = 100;
    public static final double BIRD_SCALE = 0.12 * ULTIMATE_SCALE;
    public static final double BIRD_SIZE_Y = new Image(0, 0, "Final/Bird.png").getHeight() * BIRD_SCALE;
    public static final double BIRD_SIZE_X = new Image(0, 0, "Final/Bird.png").getWidth() * BIRD_SCALE;

    // Background
    public static final double BACKGROUND_SCALE = ULTIMATE_SCALE;
    public static final double UPPER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/UpperBackground.png").getHeight() * BACKGROUND_SCALE;
    public static final double LOWER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE;

    // Pipe
    public static final double PIPE_SCALE = 0.2 * ULTIMATE_SCALE;
    public static final double PIPE_WIDTH = new Image(0, 0, "Final/Pipe.png").getWidth() * PIPE_SCALE;

    // Canvas
    public static final int CANVAS_WIDTH = 400;
    public static final int CANVAS_HEIGHT = (int) (UPPER_BACKGROUND_HEIGHT + new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE);

}

