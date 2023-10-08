package NEAT_FROM_VIDEO_CODE;

public class ConnectionGene {
    public NodeGene from;
    public NodeGene to;

    public double weight;
    public boolean enabled = true;

    public int innovation_number;

    public ConnectionGene(NodeGene from, NodeGene to) {
        this.from = from;
        this.to = to;
    }

    public ConnectionGene copy() {
        ConnectionGene copy = new ConnectionGene(from, to);
        copy.weight = weight;
        copy.enabled = enabled;
        return copy;
    }

    /*
     * Returns true if the from and to genes match. The genes will only match if they have the same innovation number
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConnectionGene)) return false;
        return from.equals(((ConnectionGene) o).from) && to.equals(((ConnectionGene) o).to);
    }

    /*
     * Will always return a new hashcode as long as we don't go over the max number of nodes from the NEAT class
     */
    @Override
    public int hashCode() {
        return from.innovation_number * NEAT.MAX_NODES + to.innovation_number;
    }
}
