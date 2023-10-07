package NEAT_FROM_VIDEO_CODE;

import java.util.ArrayList;

public class Genome {
    public ArrayList<ConnectionGene> connections = new ArrayList<>();
    public ArrayList<NodeGene> nodes = new ArrayList<>();

    private NEAT neat;

    public Genome(NEAT neat) {
        this.neat = neat;
    }

    public double distance(Genome g2) {
        return 0;
    }

    public static Genome crossover(Genome g1, Genome g2) {
        return null;
    }

    public void mutate() {

    }
}
