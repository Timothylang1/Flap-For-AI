import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

public class Bird extends Image {

    private double speed = 0;
    private PipeHandler pipes;

    public Bird(PipeHandler pipes, CanvasWindow canvas) {
        super(0, 0, "Final/Bird.png");
        this.pipes = pipes;
        setScale(Constants.BIRD_SCALE);
        setCenter(Constants.STARTING_BIRD_X, Constants.STARTING_BIRD_Y);
        canvas.add(this);
    }

    /*
     * Returns true if successfully moved, false otherwise
     */
    public boolean move() {
        if (checkInBounds() && !pipes.checkCollisionPipe(getCenter().getX(), getCenter().getY())) {
            moveBy(0, speed);
            speed += Constants.GRAVITY;
            return true;
        }
        return false;
    }

    /*
     * Returns true if bird is within bounds 
     */
    private boolean checkInBounds() {
        if (getCenter().getY() <= 0 || getCenter().getY() + Constants.BIRD_SIZE_Y / 2 >= Constants.UPPER_BACKGROUND_HEIGHT) return false;
        return true;
    }

    public void rise() {
        speed = -Constants.JUMPSPEED;
        moveBy(0, speed);
    }
}
