import java.util.ArrayList;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

public class Background {

    private final CanvasWindow CANVAS;
    private ArrayList<Image> Images = new ArrayList<Image>();
    private final double BGIMAGEWIDTH = new Image(0, 0, "Final/UpperBackground.png").getWidth() * Constants.BACKGROUND_SCALE;

    public Background(CanvasWindow canvas) {
        CANVAS = canvas;
        double x = 0;
        while (x < Constants.CANVAS_WIDTH) {
            generateBackground();
            x += BGIMAGEWIDTH;
        }
        generateBackground(); // Generates one more background image so that we can't see the background being generated as it slides past
    }

    private void generateBackground() {
        Image UpperBg = new Image(0, 0, "Final/UpperBackground.png");
        UpperBg.setScale(Constants.BACKGROUND_SCALE);
        UpperBg.setCenter(
            calculateX(),
            Constants.UPPER_BACKGROUND_HEIGHT / 2);
        Image LowerBg = new Image(0, 0, "Final/LowerBackground.png");
        LowerBg.setScale(Constants.BACKGROUND_SCALE);
        LowerBg.setCenter(
            calculateX(), 
            LowerBg.getHeight() * Constants.BACKGROUND_SCALE / 2 + Constants.UPPER_BACKGROUND_HEIGHT);
        Images.add(UpperBg);
        Images.add(LowerBg);
        CANVAS.add(UpperBg);
        CANVAS.add(LowerBg);
    }

    private double calculateX() {
        if (Images.size() == 0) { // This is only for the initial case when they're no images in the list yet
            return BGIMAGEWIDTH / 2;
        }
        return (Images.get(Images.size() - 1).getCenter().getX() + BGIMAGEWIDTH) / 1.001; // The 1.001 is a random number that helps eliminate weird white space between background iamges (try removing to understand)
    }

    public void move() {
        if (Images.get(0).getPosition().getX() < -BGIMAGEWIDTH) {
            Images.remove(0);
            Images.remove(0);
            generateBackground();
        }
        Images.forEach(x -> x.moveBy(-Constants.GAMESPEED * 0.5, 0));
    }
}
