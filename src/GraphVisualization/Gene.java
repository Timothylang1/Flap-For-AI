package GraphVisualization;

public class Gene {
    public final int INITIAL_NODE;
    public final int END_NODE;
    public double weight;

    public Gene(int initial_node, int end_node, double weight) {
        INITIAL_NODE = initial_node;
        END_NODE = end_node;
        this.weight = weight;
    }

    public boolean equals(Gene x) {
        return (x.INITIAL_NODE == INITIAL_NODE) && (x.END_NODE == END_NODE);
    }

    @Override
    public String toString() {
        return "\nCurrent Gene\nInitial node: " + Integer.toString(INITIAL_NODE) + "\nEnd node: " + Integer.toString(END_NODE) + "\n";
    }
}