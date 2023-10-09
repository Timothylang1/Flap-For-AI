package NEAT_FROM_VIDEO_CODE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Genome {
    public ArrayList<ConnectionGene> connections = new ArrayList<>();
    public Set<NodeGene> nodes = new HashSet<>();

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
        double average_weight_diff = 0; // Stores the average difference in weights of the genes
        ArrayList<ConnectionGene> connections_copy = new ArrayList<>(connections); // Copy of this genomes connections
        for (ConnectionGene gene : g2.connections) {
            boolean removed_gene = false;
            for (int i = 0; i < connections_copy.size(); i++) {
                if (gene.equals(connections_copy.get(i))) {
                    similar_genes += 1;
                    average_weight_diff += Math.abs(gene.weight - connections_copy.get(i).weight);
                    connections_copy.remove(i);
                    removed_gene = true;
                    break;
                }
            }
            if (!removed_gene) {
                excess_disjoint_genes += 1;
            }
        }

        // Any genes leftover from the connections_copy are added to the excess / disjoint genes because we couldn't find a match
        excess_disjoint_genes += connections_copy.size();

        // Then we take the average of the difference of weights
        average_weight_diff /= similar_genes;

        // Calcualte N
        int N = Math.max(connections.size(), g2.connections.size());
        if (N < 20) {
            N = 1;
        }

        // Then calculate difference
        return Neural_Constants.EXCESS_DISJOINT_COEFFICIENT * excess_disjoint_genes / N + average_weight_diff * Neural_Constants.AVERAGE_WEIGHT_COEFFICIENT;
    }

    /*
     * Crossesover two genomes. Rules:
     * 1. If we have a matching gene, then we randomly pick one from either gene except we take the disjoint gene as priority
     * 2. All excess + disjoint genes are combined
     * 3. Preserve order of genes
     */
    public static Genome crossover(Genome g1, Genome g2) {
        Genome new_genome = g1.neat.emptyGenome();
        int index_g1 = 0;
        int index_g2 = 0;
        while (index_g1 < g1.connections.size() || index_g2 < g2.connections.size()) {
            ConnectionGene gene1 = g1.connections.get(index_g1);
            ConnectionGene gene2 = g2.connections.get(index_g2);
            if (gene1.equals(gene2)) {
                // Prioritize disabled genes
                if (!g1.connections.get(index_g1).enabled) {
                    new_genome.connections.add(g1.connections.get(index_g1).copy());
                }
                else if (!g2.connections.get(index_g2).enabled) {
                    new_genome.connections.add(g2.connections.get(index_g1).copy());
                }
                // 50% chance of copying from the first genome
                else if (Math.random() > 0.5) {
                    new_genome.connections.add(g1.connections.get(index_g1).copy());
                }
                else {
                    new_genome.connections.add(g2.connections.get(index_g2).copy());
                }
                index_g1 += 1;
                index_g2 += 1;
            }
            else if (gene1.innovation_number > gene2.innovation_number) {
                new_genome.connections.add(gene2.copy());
                index_g2 += 1;
            }
            else {
                new_genome.connections.add(gene1.copy());
                index_g1 += 1;
            }
        }

        // Add all the nodes in
        for (NodeGene gene : g1.nodes) {
            new_genome.nodes.add(gene);
        }
        for (NodeGene gene : g2.nodes) {
            new_genome.nodes.add(gene);
        }
        return new_genome;
    }

    public void mutate() {

    }
}
