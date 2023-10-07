package NEAT_FROM_VIDEO_CODE;

public class NodeGene {
    public int innovation_number;
    public double x, y;
    public NodeGene(int innovation_number) {
        this.innovation_number = innovation_number;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodeGene)) return false;
        return ((NodeGene) o).innovation_number == innovation_number;
    }

    @Override
    public int hashCode() {
        return innovation_number;
    }
}
