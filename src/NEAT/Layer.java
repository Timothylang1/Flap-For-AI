package NEAT;

import java.util.ArrayList;

public class Layer {
    private ArrayList<Double> outputs;
    private ArrayList<Neuron> neurons;

    public Layer() {
        outputs = new ArrayList<Double>();
        neurons = new ArrayList<Neuron>();

    }

    public void addNeuron(Neuron n) {
        neurons.add(n);

        // After adding neuron, put their outputs in outputList
        outputs.add(n.getOutput());
    }

    public ArrayList<Double> getOutputs() {
        return outputs;
    }

    public static void main(String[] args) {
        ArrayList<Double> inputs = new ArrayList<Double>();
        ArrayList<Double> weights1 = new ArrayList<Double>();
        ArrayList<Double> weights2 = new ArrayList<Double>();
        ArrayList<Double> weights3 = new ArrayList<Double>();


        inputs.add(1.0);
        inputs.add(2.0);
        inputs.add(3.0);
        inputs.add(2.5);

        weights1.add(0.2);
        weights1.add(0.8);
        weights1.add(-0.5);
        weights1.add(1.0);

        weights2.add(0.5);
        weights2.add(-0.91);
        weights2.add(0.26);
        weights2.add(-0.5);

        weights3.add(-0.26);
        weights3.add(-0.27);
        weights3.add(0.17);
        weights3.add(0.87 );

        Neuron n1 = new Neuron(inputs, weights1, 2.0);
        Neuron n2 = new Neuron(inputs, weights2, 3.0);
        Neuron n3 = new Neuron(inputs, weights3, 0.5);
        Layer l = new Layer();
        
        l.addNeuron(n1);
        l.addNeuron(n2);
        l.addNeuron(n3);

        l.getOutputs();
    }
}
