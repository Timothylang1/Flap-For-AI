package NEAT;

import java.util.ArrayList;

public class Layer {
    private ArrayList<Neuron> neurons = new ArrayList<>();

    public Layer(int numOfNeuronsCurrentLayer, int numOfNeuronsPreviousLayer, ActivationFunction function) {
        for (int neuron = 0; neuron < numOfNeuronsCurrentLayer; neuron++) {
            neurons.add(new Neuron(function, numOfNeuronsPreviousLayer));
        }
    }

    public ArrayList<Double> calculateOutputs(ArrayList<Double> inputs) {
        ArrayList<Double> outputs = new ArrayList<>();
        neurons.forEach(neuron -> outputs.add(neuron.getOutput(inputs)));
        return outputs;
    }
}
