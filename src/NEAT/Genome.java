package NEAT;

import java.util.ArrayList;
import java.util.HashMap;

public class Genome {
    public final int INITIAL_NODE;
    public final int END_NODE;
    public boolean enabled;
    public double weight;

    public Genome(int initial_node, int end_node, double weight) {
        INITIAL_NODE = initial_node;
        END_NODE = end_node;
        this.weight = weight;
        enabled = true;
    }

    public boolean equals(Genome x) {
        return (x.INITIAL_NODE == INITIAL_NODE) && (x.END_NODE == END_NODE);
    }


    @Override
    public String toString() {
        return "\nCurrent Gene\nInitial node: " + Integer.toString(INITIAL_NODE) + "\nEnd node: " + Integer.toString(END_NODE) + "\n";
    }

    public static void main(String[] args) {
        HashMap<Genome, Integer> map = new HashMap<>();
        Genome gene1 = new Genome(20, 12, 1);
        map.put(gene1, 1);
        Genome gene2 = new Genome(20, 12, 0);
        System.out.println(gene2.equals(gene1));
    }
}
