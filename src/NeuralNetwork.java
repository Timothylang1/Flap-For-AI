import java.util.ArrayList;

import Game.Bird;
import Game.Constants;
import Game.PipeHandler;
import NEAT.ActivationFunction;
import NEAT.Layer;
import edu.macalester.graphics.CanvasWindow;

public class NeuralNetwork {
    private Bird bird;
    private static PipeHandler pipes;
    private ArrayList<Layer> Layers = new ArrayList<>();
    private boolean isDead = false;
    private int fitness = 0;

    public NeuralNetwork(PipeHandler pipes, CanvasWindow canvas) {
        this.pipes = pipes;

        // Create bird assigned to this neural network
        Bird bird = new Bird(pipes);
        bird.addBird(canvas);


        // Create neural network
        // Define initial function
        ActivationFunction middle_function = (x) -> Math.max(0, x);
        Layers.add(new Layer(5, 4, middle_function));
        Layers.add(new Layer(5, 5, middle_function));
        // Define exit function, then create output layer
        ActivationFunction output_function = (x) -> x;
        Layers.add(new Layer(2, 4, output_function));
    }

    public boolean moveBird() {
        if (!isDead) {
            ArrayList<Double> inputs = new ArrayList<>();
            inputs.add(bird.getCenter().getX());
            inputs.add(bird.getCenter().getY());
            inputs.add(bird.getSpeed());
            inputs.add(pipes.getCurrentPipe().getX());
            inputs.add(pipes.getCurrentPipe().getY());
            for (Layer layer : Layers) {
                inputs = layer.calculateOutputs(inputs);
            }
            if (inputs.get(0) > inputs.get(1)) {
                bird.rise();
            }
            isDead = bird.move();
            fitness += 1;
        }
        else {
            bird.moveBy(-Constants.GAMESPEED, 0);
        }
        return isDead;
    }

    public double getFitness() {
        return fitness;
    }

}
