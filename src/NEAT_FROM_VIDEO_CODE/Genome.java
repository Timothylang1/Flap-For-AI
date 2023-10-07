package NEAT_FROM_VIDEO_CODE;

import java.util.ArrayList;

public class Genome {
    public ArrayList<ConnectionGene> connections = new ArrayList<>();
    public ArrayList<NodeGene> nodes = new ArrayList<>();

    private NEAT neat;

    public Genome(NEAT neat) {
        this.neat = neat;
    }

    /*
     * Compares how similar the genes are to each other
     */
    public double distance(Genome g2) {
        int similar_genes = 0;
        int excess_disjoint_genes = 0;
        double weight_diff = 0; // Stores the average difference in weights of the genes
        ArrayList<ConnectionGene> connections_copy = new ArrayList<>(connections); // Copy of this genomes connections
        for (ConnectionGene gene : g2.connections) {
            boolean removed_gene = false;
            for (int i = 0; i < connections_copy.size(); i++) {
                if (gene.equals(connections_copy.get(i))) {
                    similar_genes += 1;
                    weight_diff += Math.abs(gene.weight - connections_copy.get(i).weight);
                    connections_copy.remove(i);
                    removed_gene = true;
                    break;
                }
            }
            if (!removed_gene) {
                excess_disjoint_genes += 1;
            }
        }

        // Any genes leftover from the connections_copy are added to the excess / disjoint genes
        excess_disjoint_genes += connections_copy.size();

        // Then we take the average of the difference of weights
        
    }

    public static Genome crossover(Genome g1, Genome g2) {
        return null;
    }

    public void mutate() {

    }
}
