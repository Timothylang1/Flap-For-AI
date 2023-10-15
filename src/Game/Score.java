package Game;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;

/*
 * Keeps track of the birds score
 */
public class Score extends GraphicsText {

    public int score = 0;
    private double current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;

    public Score() {
        super("0", 0, 0);
        setFontSize(50);
        setCenter(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT / 8);
        setFontSize(50);
    }

    /*
     * Resets score of bird
     */
    public void reset() {
        current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;
        score = 0;
        setText(Integer.toString(score));
        setCenter(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT / 8); // Reset the center each time in case the score expanded (ex. 100 -> 0)
    }

    /*
     * Updates the score
     */
    public void updateScore() {
        current_pipe_location -= Constants.GAMESPEED;
        if (current_pipe_location <= Constants.STARTING_BIRD_X) { // The second check is to ensure that we're not updating the score continuously after the pipes pass
            score += 1;
            setText(Integer.toString(score));
            setCenter(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT / 8); // Reset the center each time in case the score expanded (ex. 9 -> 10)
            current_pipe_location += Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES;
        }
    }

    /*
     * Adds the score to the canvas
     */
    public void addScore(CanvasWindow canvas) {
        canvas.add(this);
    }
}
