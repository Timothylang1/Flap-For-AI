package NEAT;
import java.util.ArrayList;

public class Neuron {
    private ArrayList<Double> inputs;
    private ArrayList<Double> weights;
    private double bias;
    private double output;

    public Neuron(ArrayList<Double> inputs, ArrayList<Double> weights, Double bias) {
        output = 0.0;
        if (inputs.size() == weights.size()) {
            for (int i = 0; i < inputs.size(); i++) {
                output += inputs.get(i) * weights.get(i);
            }
            output += bias;
            System.out.println(output);
        } else {
            System.out.println("!!!!YOUR NUMBER OF INPUTS DON'T MATCH NUMBER OF WEIGHTS!!!!");
        }
    }

    public Double getOutput() {
        return output;
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
        
    }
}
