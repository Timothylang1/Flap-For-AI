package NEAT;

public class Gene {
    public final int INITIAL_NODE;
    public final int END_NODE;
    public double weight;

    public Gene(int initial_node, int end_node, double weight) {
        INITIAL_NODE = initial_node;
        END_NODE = end_node;
        this.weight = weight;
    }

    public Gene copy() {
        return new Gene(INITIAL_NODE, END_NODE, weight);
    }

    @Override
    public boolean equals(Object o) {
        return (((Gene) o).INITIAL_NODE == INITIAL_NODE) && (((Gene) o).END_NODE == END_NODE);
    }

    @Override
    public int hashCode() {
        return INITIAL_NODE * Neural_Constants.MAX_CONNECTIONS + END_NODE;
    }

    @Override
    public String toString() {
        return "\nCurrent Gene\nInitial node: " + Integer.toString(INITIAL_NODE) + "\nEnd node: " + Integer.toString(END_NODE) + "\n";
    }

    public static void main(String[] args) {
        Gene gene1 = new Gene(0, 0, 0);
        Gene gene2 = new Gene(0, 1, 0);
        // ArrayList<Gene> test = new ArrayList<>();
        // test.add(gene1);
        // System.out.println(test.contains(gene2));
    }
}
