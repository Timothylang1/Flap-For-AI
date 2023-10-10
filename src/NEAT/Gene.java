package NEAT;

public class Gene {
    public final int END_NODE;
    public double weight;

    public Gene(int end_node, double weight) {
        END_NODE = end_node;
        this.weight = weight;
    }

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
