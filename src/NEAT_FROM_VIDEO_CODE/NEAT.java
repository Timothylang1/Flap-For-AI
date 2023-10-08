package NEAT_FROM_VIDEO_CODE;

import java.util.ArrayList;
import java.util.HashMap;

public class NEAT {
    public static final int MAX_NODES = (int) Math.pow(2, 20); // Ridiculously high number
    private HashMap<ConnectionGene, Integer> all_connections = new HashMap<>(); // Stores connection gene and it's corresponding innovation number
    private ArrayList<NodeGene> all_nodes = new ArrayList<>();

    public NEAT() {
        reset();
    }

    /*
     * Creates empty genome with all input and output nodes
     */
    public Genome emptyGenome() {
        Genome g = new Genome(this);
        for (int i = 0; i < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; i++) {
            g.nodes.add(getNode(i));
        }
        return g;
    }

    public void reset() {
        all_connections.clear();
        all_nodes.clear();

        for (int i = 0; i < Neural_Constants.NUM_OF_INPUTS; i++) {
            NodeGene n = getNode();
            n.x = 0.1;
            n.y = (i + 1) / (double) (Neural_Constants.NUM_OF_INPUTS + 1);
        }

        for (int i = 0; i < Neural_Constants.NUM_OF_OUTPUTS; i++) {
            NodeGene n = getNode();
            n.x = 0.9;
            n.y = (i + 1) / (double) (Neural_Constants.NUM_OF_OUTPUTS + 1);
        }
    }

    public ConnectionGene getConnection(NodeGene node1, NodeGene node2) {
        ConnectionGene connectionGene = new ConnectionGene(node1, node2);
        if (all_connections.containsKey(connectionGene)) {
            connectionGene.innovation_number = all_connections.get(connectionGene);
        }
        else {
            int innovation_number = all_connections.size();
            connectionGene.innovation_number = innovation_number;
            all_connections.put(connectionGene, innovation_number);
        }

        return connectionGene;
    }

    public NodeGene getNode() {
        NodeGene n = new NodeGene(all_nodes.size());
        all_nodes.add(n);
        return n;
    }

    /*
     * Returns node at given location, otherwise create a brand new node and add it to end of list
     */
    public NodeGene getNode(int id) {
        if (id < all_nodes.size()) return all_nodes.get(id);
        return getNode();
    } 

    public static void main(String[] args) {
        NEAT neat = new NEAT();
        Genome g = neat.emptyGenome();
        System.out.println(g.nodes.size());
    }

}
