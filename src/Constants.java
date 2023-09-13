import edu.macalester.graphics.Image;

public class Constants {
    
    // All variables related to the speed/hardness of the game

    public static final double GAMESPEED = 1; // How fast pipes are incoming
    public static final double GRAVITY = 0.8; // How fast the bird falls
    public static final double JUMPSPEED = 8; // How fast the bird rises

    // All variables related to size and scale

    public static final double ULTIMATE_SCALE = 1;

    public static final int CANVAS_WIDTH = 800;
    public static final double BACKGROUND_SCALE = ULTIMATE_SCALE;
    public static final double UPPER_BACKGROUND_HEIGHT = new Image(0, 0, "Final/UpperBackground.png").getHeight() * BACKGROUND_SCALE;
    public static final int CANVAS_HEIGHT = (int) (UPPER_BACKGROUND_HEIGHT + new Image(0, 0, "Final/LowerBackground.png").getHeight() * BACKGROUND_SCALE);
    public static final double PIPE_SCALE = 0.2 * ULTIMATE_SCALE;
    public static final double BIRD_SCALE = 0.12 * ULTIMATE_SCALE;

    // Calculated values

    public static final double WIGGLEROOM = 0; // Percent of wiggle room allowed between pipes for bird to flap

    public static final double VERTICAL_DISTANCE_BETWEEN_PIPES = (WIGGLEROOM + 1) * (JUMPSPEED * JUMPSPEED / (2 * GRAVITY) + BIRD_SCALE * new Image(0, 0, "Final/Bird.png").getHeight()); // v^2/(2g) + height of bird = maximum height bird will flap
    // public static final double HORIZONTAL_DISTANCE_BETWEEN_PIPES = ;
}
