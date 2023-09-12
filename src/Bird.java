import edu.macalester.graphics.Image;

public class Bird extends Image {

    private static final double GRAVITY = 0.8;
    private double speed = 0;

    public Bird() {
        super(0, 0, "Final/Bird.png");
        setScale(0.12);
        setCenter(25, 100);
    }

    public void rise() {
        speed = -10;
        moveBy(0, speed);
    }

    public void fall() {
        moveBy(0, speed);
        speed += GRAVITY;
    }


    
}
