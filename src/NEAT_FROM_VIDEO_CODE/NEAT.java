package NEAT_FROM_VIDEO_CODE;

import java.util.ArrayList;
import java.util.HashMap;

public class NEAT {
    public static final int MAX_NODES = (int) Math.pow(2, 20); // Ridiculously high number
    private HashMap<ConnectionGene, Integer> all_connections = new HashMap<>(); // Stores connection gene and it's corresponding innovation number
    private ArrayList<NodeGene> all_nodes = new ArrayList<>();
    private int input_size, output_size, clients;

    public NEAT(int input_size, int output_size, int clients) {
        reset(input_size, output_size, clients);
    }

    public Genome emptyGenome() {
        Genome g = new Genome(this);
        for (int i = 0; i < input_size + output_size; i++) {
            g.nodes.add(getNode(i + 1));
        }
        return g;
    }

    public void reset(int input_size, int output_size, int clients) {
        this.input_size = input_size;
        this.output_size = output_size;
        this.clients = clients;

        all_connections.clear();
        all_nodes.clear();

        for (int i = 0; i < input_size; i++) {
            NodeGene n = getNode();
            n.x = 0.1;
            n.y = (i + 1) / (double) (input_size + 1);
        }

        for (int i = 0; i < output_size; i++) {
            NodeGene n = getNode();
            n.x = 0.9;
            n.y = (i + 1) / (double) (output_size + 1);
        }
    }

    public static ConnectionGene getConnection(ConnectionGene to_copy) {
        ConnectionGene copy = new ConnectionGene(to_copy.from, to_copy.to);
        copy.weight = to_copy.weight;
        copy.enabled = to_copy.enabled;
        return copy;
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
        NodeGene n = new NodeGene(all_nodes.size() + 1);
        all_nodes.add(n);
        return n;
    }

    /*
     * Returns node at given location, otherwise create a brand new node and add it to end of list
     */
    public NodeGene getNode(int id) {
        if (id <= all_nodes.size()) return all_nodes.get(id - 1);
        return getNode();
    } 

    public static void main(String[] args) {
        NEAT neat = new NEAT(3, 2, 100);
        Genome g = neat.emptyGenome();
        System.out.println(g.nodes.size());
    }

}
