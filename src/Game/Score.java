package Game;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;

public class Score extends GraphicsText {

    private int score = 0;
    private PipeHandler pipes;
    private Image previouspipe = null;
    
    public Score(PipeHandler pipes) {
        super("0", 0, 0);
        this.pipes = pipes;
        setCenter(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT / 8);
        setFontSize(50);
    }

    public void updateScore() {
        Image currentPipe = pipes.getCurrentPipe();
        if (currentPipe.getCenter().getX() <= Constants.STARTING_BIRD_X && !currentPipe.equals(previouspipe)) { // The second check is to ensure that we're not updating the score continuously after the pipes pass
            score += 1;
            setText(Integer.toString(score));
            previouspipe = currentPipe;
        }
    }

    public void addScore(CanvasWindow canvas) {
        canvas.add(this);
    }

    public int getScore() {
        return score;
    }
}
