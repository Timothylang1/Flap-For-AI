package Game;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

public class Bird extends Image {

    private double speed;
    private PipeHandler pipes;

    public Bird(PipeHandler pipes) {
        super(0, 0, "Final/Bird.png");
        this.pipes = pipes;
        setScale(Constants.BIRD_SCALE);
        reset();
    }

    public void reset() {
        speed = 0;
        setCenter(Constants.STARTING_BIRD_X, Constants.STARTING_BIRD_Y);
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

    public void jump() {
        speed = -Constants.JUMPSPEED;
    }

    public void addBird(CanvasWindow canvas) {
        canvas.add(this);
    }

    public double getSpeed() {
        return speed;
    }
}