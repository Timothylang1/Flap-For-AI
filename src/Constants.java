import edu.macalester.graphics.Image;

public class Constants {

    public static final double ULTIMATE_SCALE = 1;

    
    // All variables related to the speed/hardness of the game

    public static final double GAMESPEED = 1.5; // How fast pipes are incoming
    public static final double GRAVITY = 0.6; // How fast the bird falls
    public static final double JUMPSPEED = 8; // How fast the bird rises

    // Starting locations

    public static final double STARTING_BIRD_X = 100;
    public static final double STARTING_BIRD_Y = 100;

    // All variables related to size and scale

    public static final int CANVAS_WIDTH = 400;
    public static final double BACKGROUND_SCALE = ULTIMATE_SCALE;
    public static final double UPPER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/UpperBackground.png").getHeight() * BACKGROUND_SCALE;
    public static final double LOWER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE;
    public static final int CANVAS_HEIGHT = (int) (UPPER_BACKGROUND_HEIGHT + new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE);
    public static final double PIPE_SCALE = 0.2 * ULTIMATE_SCALE;
    public static final double BIRD_SCALE = 0.12 * ULTIMATE_SCALE;
    public static final double BIRD_SIZE_Y = new Image(0, 0, "Final/Bird.png").getHeight() * BIRD_SCALE;
    public static final double BIRD_SIZE_X = new Image(0, 0, "Final/Bird.png").getWidth() * BIRD_SCALE;

    // Calculated values
    public static final double WIGGLEROOM_Y = 0.0; // 0 to 1 percent of wiggle room allowed between pipes for bird to flap
    public static final double WIGGLEROOM_X = 0.0;
    
    // v^2/(2g) + height of bird = maximum height bird will flap
    public static final double VERTICAL_DISTANCE_BETWEEN_PIPES = (WIGGLEROOM_Y + 1) * (JUMPSPEED * JUMPSPEED / (2 * GRAVITY) + BIRD_SIZE_Y);
    
    // -vy/vx - sqrt((vy/vx)^2 - 4(1/2 * g / vx^2) * dy) / (2 * (1/2) * (g / vx^2)) Physics equation for calculating minimum distance needed for bird to fall using projectile motion. Also added in width of bird and pipe
    public static final double HORIZONTAL_DISTANCE_BETWEEN_PIPES = (WIGGLEROOM_X + 1) * (((-(JUMPSPEED/GAMESPEED) - Math.sqrt(Math.pow(JUMPSPEED/GAMESPEED, 2) - 2 * -GRAVITY / Math.pow(GAMESPEED, 2) * (UPPER_BACKGROUND_HEIGHT - VERTICAL_DISTANCE_BETWEEN_PIPES))) / (-GRAVITY / Math.pow(GAMESPEED, 2))) + BIRD_SIZE_X + new Image(0, 0, "Final/Pipe.png").getWidth() * PIPE_SCALE);
}
