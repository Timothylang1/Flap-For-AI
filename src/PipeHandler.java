import java.util.ArrayList;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

public class PipeHandler {

    private final CanvasWindow CANVAS;
    private final Random RAND = new Random();
    private ArrayList<Image> pipes = new ArrayList<Image>();
    // private final double HORIZONTAL_DISTANCE_BETWEEN_PIPES;
    
    public PipeHandler(CanvasWindow canvas) {
        CANVAS = canvas;
        createPipes();
    }

    private void createPipes() {
        Image lowerPipe = new Image(0, 0, "Final/Pipe.png");
        lowerPipe.setScale(Constants.PIPE_SCALE);
        Image upperPipe = new Image(0, 0, "Final/Pipe.png");
        upperPipe.setScale(Constants.PIPE_SCALE);
        upperPipe.rotateBy(180);
        double placement = RAND.nextDouble() * (Constants.UPPER_BACKGROUND_HEIGHT - Constants.VERTICAL_DISTANCE_BETWEEN_PIPES);
        upperPipe.setCenter(100, -upperPipe.getHeight() * Constants.PIPE_SCALE / 2 + placement);
        lowerPipe.setCenter(100, placement + Constants.VERTICAL_DISTANCE_BETWEEN_PIPES + lowerPipe.getHeight() * Constants.PIPE_SCALE / 2);
        CANVAS.add(upperPipe);
        CANVAS.add(lowerPipe);
    }
}
