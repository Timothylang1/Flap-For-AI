package Game;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;

public class Score extends GraphicsText {

    private int score = 0;
    private double current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;

    public Score() {
        super("0", 0, 0);
        setCenter(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT / 8);
        setFontSize(50);
    }

    public void reset() {
        current_pipe_location = Constants.PIPE_STARTING_LOCATION_X;
        score = 0;
        setText(Integer.toString(score));
    }

    public void updateScore() {
        current_pipe_location -= Constants.GAMESPEED;
        if (current_pipe_location <= Constants.STARTING_BIRD_X) { // The second check is to ensure that we're not updating the score continuously after the pipes pass
            score += 1;
            setText(Integer.toString(score));
            current_pipe_location += Constants.HORIZONTAL_DISTANCE_BETWEEN_PIPES;
        }
    }

    public void addScore(CanvasWindow canvas) {
        canvas.add(this);
    }
}
