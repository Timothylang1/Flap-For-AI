package NEAT;

/*
 * Gene class only holds the end connection as well as the weight. We didn't need to include the starter neuron in Gene because
 * the Genome class has a hashmap where the key is the starter value and the value is the gene itself.
 */
public class Gene {
    public final int END_NODE;
    public double weight;

    public Gene(int end_node, double weight) {
        END_NODE = end_node;
        this.weight = weight;
    }

    /*
     * Returns a copy of this genome
     */
    public Gene copy() {
        return new Gene(END_NODE, weight);
    }

    @Override
    public boolean equals(Object o) {
        return ((Gene) o).END_NODE == END_NODE;
    }

    @Override
    public int hashCode() {
        return END_NODE;
    }
}
