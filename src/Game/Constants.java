package Game;

import edu.macalester.graphics.Image;

/*
 * Constants class that controls all of the dimensions of Flappy Bird
 */
public class Constants {
    
    // Frame speed
    public static final int FRAMESPEEDSCALAR = 100; // Determines how fast the simulation runs

    // All variables related to the speed/hardness of the game
    public static final double GAMESPEED = 1.7; // How fast pipes are incoming
    public static final double GRAVITY = 0.5; // How fast the bird falls
    public static final double JUMPSPEED = 6; // How fast the bird rises

    // Difficulty
    public static final double VERTICAL_DISTANCE_BETWEEN_PIPES = 90;    
    public static final double HORIZONTAL_DISTANCE_BETWEEN_PIPES = 145;

    // Bird
    public static final double STARTING_BIRD_X = 100;
    public static final double STARTING_BIRD_Y = 100;
    public static final double BIRD_SCALE = 0.12;
    public static final double BIRD_SIZE_Y = new Image(0, 0, "Final/Bird.png").getHeight() * BIRD_SCALE;
    public static final double BIRD_SIZE_X = new Image(0, 0, "Final/Bird.png").getWidth() * BIRD_SCALE;

    // Background
    public static final double BACKGROUND_SCALE = 1;
    public static final double UPPER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/UpperBackground.png").getHeight() * BACKGROUND_SCALE;
    public static final double LOWER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE;

    // Canvas
    public static final int CANVAS_WIDTH = 400;
    public static final int CANVAS_HEIGHT = (int) (UPPER_BACKGROUND_HEIGHT + new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE);
    public static final int CANVAS2_WIDTH = 800;
    public static final int CANVAS2_HEIGHT = 800;    

     // Pipe
    public static final double PIPE_SCALE = 0.2;
    public static final double PIPE_WIDTH = new Image(0, 0, "Final/Pipe.png").getWidth() * PIPE_SCALE;
    public static final double PIPE_HEIGHT = new Image(0, 0, "Final/Pipe.png").getHeight() * PIPE_SCALE;
    public static final double PIPE_STARTING_LOCATION_X = CANVAS_WIDTH + PIPE_WIDTH;

}

