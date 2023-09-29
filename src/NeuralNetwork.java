import java.util.ArrayList;

import Game.Bird;
import Game.Constants;
import Game.PipeHandler;
import NEAT.ActivationFunction;
import NEAT.Neuron;
import edu.macalester.graphics.CanvasWindow;

public class NeuralNetwork {
    private Bird bird;
    private PipeHandler pipes;
    private boolean isAlive = true;
    private int fitness = 0;
    public ArrayList<Neuron> neurons = new ArrayList<>();
    private static final int NUM_OF_INPUTS = 3; // For simplicity, this will be equal to the number of neurons per layer
    private static final int NUM_OF_LAYERS = 0;

    private static final ActivationFunction middle_function = (x) -> Math.max(0, x);
    private static final ActivationFunction output_function = (x) -> x;

    /*
     * Used to create initialization of neural network
     */
    public NeuralNetwork(PipeHandler pipes, CanvasWindow canvas) {
        this.pipes = pipes;
        bird = new Bird(pipes);
        bird.addBird(canvas);

        // This arraylists helps with connecting the neurons together
        ArrayList<Neuron> to_connect = new ArrayList<>();
        ArrayList<Neuron> middle_connect = new ArrayList<>();

        // Create input neurons
        for (int inputs = 0; inputs < NUM_OF_INPUTS; inputs++) {
            Neuron neuron = new Neuron();
            neurons.add(neuron);
            to_connect.add(neuron);
        }

        // Create middle layer
        for (int layer = 0; layer < NUM_OF_LAYERS; layer++) {
            for (int inputs = 0; inputs < NUM_OF_INPUTS; inputs++) {
                Neuron neuron = new Neuron(NUM_OF_INPUTS, middle_function);
                to_connect.forEach(x -> x.connected_neurons.add(neuron));
                neurons.add(neuron);
                middle_connect.add(neuron);
            }
            to_connect = new ArrayList<>(middle_connect);
            middle_connect.clear();
        }

        // Create output layer
        for (int output = 0; output < 2; output++) {
            Neuron neuron = new Neuron(NUM_OF_INPUTS, output_function);
            to_connect.forEach(x -> x.connected_neurons.add(neuron));
            neurons.add(neuron);
        }
    }

    /*
     * This takes an existing neural network and mutates it slightly
     */
    public NeuralNetwork(PipeHandler pipes, CanvasWindow canvas, NeuralNetwork network) {
        this.pipes = pipes;
        bird = new Bird(pipes);
        bird.addBird(canvas);

        // This arraylists helps with connecting the neurons together
        ArrayList<Neuron> to_connect = new ArrayList<>();
        ArrayList<Neuron> middle_connect = new ArrayList<>();

        // Arraylist of neurons we're trying to copy from and mutate
        ArrayList<Neuron> to_mutate = network.neurons;

        // Tracker to keep track what neuron we currently are on
        int tracker = 0;

        // Create input neurons
        for (int inputs = 0; inputs < NUM_OF_INPUTS; inputs++) {
            Neuron neuron = new Neuron();
            neurons.add(neuron);
            to_connect.add(neuron);
            tracker += 1;
        }

        // Create middle layer
        for (int layer = 0; layer < NUM_OF_LAYERS; layer++) {
            for (int inputs = 0; inputs < NUM_OF_INPUTS; inputs++) {
                Neuron neuron = new Neuron(new ArrayList<>(to_mutate.get(tracker).weights), to_mutate.get(tracker).bias, middle_function);
                to_connect.forEach(x -> x.connected_neurons.add(neuron));
                neurons.add(neuron);
                middle_connect.add(neuron);
                tracker += 1;
            }
            to_connect = new ArrayList<>(middle_connect);
            middle_connect.clear();
        }

        // Create output layer
        for (int output = 0; output < 2; output++) {
            Neuron neuron = new Neuron(new ArrayList<>(to_mutate.get(tracker).weights), to_mutate.get(tracker).bias, output_function);
            to_connect.forEach(x -> x.connected_neurons.add(neuron));
            neurons.add(neuron);
            tracker += 1;
        }

    }

    /*
     * Returns true if bird moved successfully, false otherwise
     */
    public boolean moveBird() {
        if (isAlive) {
            neurons.get(0).inputs.add(bird.getCenter().getY()); // Bird Y Location
            neurons.get(1).inputs.add(pipes.getCurrentPipe()[0]); // Lower Pipe Y Location
            neurons.get(2).inputs.add(pipes.getCurrentPipe()[1]); // Upper Pipe Y Location
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
