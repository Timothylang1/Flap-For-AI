import java.util.ArrayList;
import java.util.Random;

import Game.Bird;
import Game.Constants;
import Game.PipeHandler;
import NEAT.ActivationFunction;
import NEAT.Neural_Constants;
import NEAT.Neuron;
import edu.macalester.graphics.CanvasWindow;

public class NeuralNetwork {
    private Bird bird;
    private PipeHandler pipes;
    private boolean isAlive = true;
    private int fitness = 0;
    public ArrayList<Neuron> neurons = new ArrayList<>();
    private static final Random RAND = new Random();

    private static final ActivationFunction middle_function = (x) -> Math.max(0, x);
    private static final ActivationFunction output_function = (x) -> x;

    /*
     * Used to create initialization of neural network
     */
    public NeuralNetwork(PipeHandler pipes, CanvasWindow canvas) {
        this.pipes = pipes;
        bird = new Bird(pipes);
        bird.addBird(canvas);

        addConnection(); // Default add one random connection

        // Create input neurons
        for (int inputs = 0; inputs < Neural_Constants.NUM_OF_INPUTS; inputs++) {
            Neuron neuron = new Neuron();
            neurons.add(neuron);
        }

        // Create output layer
        for (int output = 0; output < Neural_Constants.NUM_OF_OUTPUTS; output++) {
            Neuron neuron = new Neuron();
            neurons.add(neuron);
        }
    }

    /*
     * Takes an existing neural network, copies it's value for the nodes, then mutates slightly
     */
    public void copyAndMutate(ArrayList<Neuron> network) {
        // Mutate all slightly except for input neurons
        for (int neuron_index = Neural_Constants.NUM_OF_INPUTS; neuron_index < neurons.size(); neuron_index++) {
            neurons.get(neuron_index).mutate(network.get(neuron_index));
        }
    }

    /*
     * Adds in random neuron in network
     */
    private void addNeuron() {
        // First, choose random connection to severe


    }

    /*
     * Adds in connection into network
     */
    private void addConnection() {

    }

    /*
     * Returns true if bird moved successfully, false otherwise
     */
    public boolean moveBird() {
        if (isAlive) {
            neurons.get(0).inputs.put(neurons.get(0), bird.getCenter().getY()); // Bird Y Location
            neurons.get(1).inputs.put(neurons.get(1), pipes.getCurrentPipe()[0]); // Lower Pipe Y Location
            neurons.get(2).inputs.put(neurons.get(2), pipes.getCurrentPipe()[1]); // Upper Pipe Y Location
            // neurons.get(3).inputs.add(bird.getSpeed()); // Bird speed

            neurons.forEach(Neuron::calculateOutput); // Goes through the neural network tree
            double jump = neurons.get(neurons.size() - 1).output; // The last neuron holds the final output for jump
            double not_jump = neurons.get(neurons.size() - 2).output; // The second to last neuron in the network holds the final output for not jump

            if (jump > not_jump) bird.jump();
            isAlive = bird.move(); // Returns true if bird moved successfully, 
            fitness += 1;
        }
        else {
            bird.moveBy(-Constants.GAMESPEED, 0); // Moves the bird to the left until it's off the screen after it has died
        }
        return isAlive;
    }

    /*
     * Returns and resets fitness as well as the bird
     */
    public int reset() {
        // Reset bird
        bird.reset();
        isAlive = true;

        // Reset fitness
        int to_return = fitness;
        fitness = 0;
        return to_return;
    }

    public void removeBird(CanvasWindow canvas) {
        canvas.remove(bird);
    }
}
