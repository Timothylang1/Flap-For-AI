package Game;
import java.util.ArrayList;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;

/*
 * Class that handles background images moving across the screen
 */
public class Background {

    private GraphicsGroup upperImages = new GraphicsGroup(0, 0);
    private GraphicsGroup lowerImages = new GraphicsGroup(0, 0);
    private ArrayList<Image> upimages = new ArrayList<Image>();
    private ArrayList<Image> lowimages = new ArrayList<Image>(); 
    private final double BG_IMAGE_WIDTH = new Image(0, 0, "Final/UpperBackground.png").getWidth() * Constants.BACKGROUND_SCALE;

    public Background() {
        reset();
    }

    /*
     * Removes all images and adds them back in at their proper location
     */
    public void reset() {
        upperImages.removeAll();
        lowerImages.removeAll();
        upimages.clear();
        lowimages.clear();
        double x = 0;
        while (x < Constants.CANVAS_WIDTH) {
            // Generate upper background
            generateBackground("Final/UpperBackground.png", upimages, upperImages, Constants.UPPER_BACKGROUND_HEIGHT / 2);
            generateBackground("Final/LowerBackground.png", lowimages, lowerImages, Constants.UPPER_BACKGROUND_HEIGHT + Constants.LOWER_BACKGROUND_HEIGHT / 2);
            x += BG_IMAGE_WIDTH;
        }
        // Generates one more background image so that we can't see the background being generated as it slides past
        generateBackground("Final/UpperBackground.png", upimages, upperImages, Constants.UPPER_BACKGROUND_HEIGHT / 2);
        generateBackground("Final/LowerBackground.png", lowimages, lowerImages, Constants.UPPER_BACKGROUND_HEIGHT + Constants.LOWER_BACKGROUND_HEIGHT / 2);
    }

    /*
     * Creates the image
     */
    private void generateBackground(String filepath, ArrayList<Image> list, GraphicsGroup group, double y_location) {
        Image image = new Image(0, 0, filepath);
        image.setScale(Constants.BACKGROUND_SCALE);
        image.setCenter(
            calculateX(list),
            y_location);
        group.add(image);
        list.add(image);        
    }

    /*
     * Calculates the X coordinate of the image
     */
    private double calculateX(ArrayList<Image> list) {
        if (list.size() == 0) { // This is only for the initial case when they're no images in the list yet
            return BG_IMAGE_WIDTH / 2;
        }
        return (list.get(list.size() - 1).getCenter().getX() + BG_IMAGE_WIDTH) / 1.001; // The 1.001 is a random number that helps eliminate weird white space between background iamges (try removing to understand)
    }

    /*
     * Moves all the images, and gradually removes them as they exit the screen as well as add back in images to have a smooth flow
     */
    public void move() {
        if (upimages.get(0).getCenter().getX() < -BG_IMAGE_WIDTH) {
            upperImages.remove(upimages.remove(0));
            generateBackground("Final/UpperBackground.png", upimages, upperImages, Constants.UPPER_BACKGROUND_HEIGHT / 2);
        }
        if (lowimages.get(0).getCenter().getX() < -BG_IMAGE_WIDTH) {
            lowerImages.remove(lowimages.remove(0));
            generateBackground("Final/LowerBackground.png", lowimages, lowerImages, Constants.UPPER_BACKGROUND_HEIGHT + Constants.LOWER_BACKGROUND_HEIGHT / 2);
        }
        upimages.forEach(x -> x.moveBy(-Constants.GAMESPEED * 0.5, 0));
        lowimages.forEach(x -> x.moveBy(-Constants.GAMESPEED, 0));
    }

    /*
     * Adds upper images
     */
    public void addUpperBackground(CanvasWindow canvas) {
        canvas.add(upperImages);
    }

    /*
     * Adds lower images
     */
    public void addLowerBackground(CanvasWindow canvas) {
        canvas.add(lowerImages);
    }
}
