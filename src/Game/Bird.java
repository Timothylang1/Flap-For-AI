package Game;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;
import NEAT.Genome;
import NEAT.Neural_Constants;

public class Bird extends Image {

    private double speed;
    private PipeHandler pipes;
    public Genome gene;
    public boolean isAlive; // Tells if the bird is still alive

    public Bird(PipeHandler pipes, Genome gene) {
        super(0, 0, "Final/Bird.png");
        this.pipes = pipes;
        setScale(Constants.BIRD_SCALE);
        reset(gene);
    }

    /*
     * Resets bird with new genome
     */
    public void reset(Genome gene) {
        this.gene = gene;
        isAlive = true;
        speed = 0;
        setCenter(Constants.STARTING_BIRD_X, Constants.STARTING_BIRD_Y);
    }

    /*
     * Returns true if successfully moved, false otherwise
     */
    public boolean move(int score) {
        if (isAlive) {
            jump(); // Checks if it should jump
            if (checkInBounds() && !pipes.checkCollisionPipe(getCenter().getX(), getCenter().getY())) {
                moveBy(0, speed);
                speed += Constants.GRAVITY;
                return true;
            }
            isAlive = false; // If we reach this point, then that means the bird collided with a pipe or wasn't within bounds which means we switch it to not alive
            gene.score = score; // It also means we set the gene score to be right when the bird dies
        }
        else {
            // If the bird is dead, then we continously move it to the left of the screen
            moveBy(-Constants.GAMESPEED, 0);
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

    /*
     * Gives inputs to neural network, then checks if it should jump or not based on the output of the neural network
     * First output represents jump, second represents not jump
     */
    private void jump() {
        double[] output = gene.output(getInputs());
        if (output[0] > output[1]) speed = -Constants.JUMPSPEED;
    }

    /*
     * Returns an array of all the inputs we use for the neural network
     */
    private double[] getInputs() {
        // Create inputs
        double inputs[] = new double[Neural_Constants.NUM_OF_INPUTS];
        inputs[0] = getCenter().getY(); // Bird's Y Location
        inputs[1] = pipes.getCurrentPipesY()[0]; // Lower pipe y location
        inputs[2] = pipes.getCurrentPipesY()[1]; // Upper pipe y location
        return inputs;
    }

    public void addBird(CanvasWindow canvas) {
        canvas.add(this);
    }
}
