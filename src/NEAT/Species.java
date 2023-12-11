package NEAT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Class that takes care of species, which is essentailly a list of genomes that are closely related to each other.
 */
public class Species {
    private final Genome FOUNDATION; // The primary genome that will be used to see if potential genomes get added to this species
    private ArrayList<Genome> genomes = new ArrayList<>();
    private final Random RAND = new Random();

    /*
     * Takes in one genome as a foundation genome. This will be the one genome used to determine if a genome being added is similar enough to
     * join the species
     */
    public Species(Genome foundation) {
        FOUNDATION = foundation;
        genomes.add(foundation);
    }

    /*
     * Calculates the total species fitness
     */
    public double calculateSpeciesFitness() {
        double total_fitness = 0;
        for (Genome genome : genomes) {
            total_fitness += genome.adjusted_score;
        }
        return total_fitness;
    }

    /*
     * Create offspring by doing roulette (each child has a percentage to be selected based on its score)
     * We then crossover all the parents in pairs, then mutate them
     */
    public ArrayList<Genome> createOffspring(int num_of_offspring) {
        // Get the total_score as well as the list of scores for this genome
        int total_score = genomes.stream().mapToInt(x -> x.score).sum();
        List<Integer> scores = genomes.stream().map(x -> x.score).toList();

        // Choose parents using roulette
        Genome[] parents = new Genome[num_of_offspring];
        for (int i = 0; i < num_of_offspring; i++) {
            parents[i] = genomes.get(roulette(scores, total_score));
        }

        // Mate parents (except if we have an odd number, then the last one gets chosen anyways)
        ArrayList<Genome> children = new ArrayList<>();
        for (int i = 1; i < num_of_offspring; i += 2) {
            
            // Whichever genome has the better score gets priority during the crossover, so we pass that in first for the crossover
            Genome new_gene;
            if (parents[i].score > parents[i - 1].score) new_gene = parents[i].crossover(parents[i - 1]);
            else new_gene = parents[i - 1].crossover(parents[i]);
            children.add(new_gene.mutate());
            children.add(new_gene.mutate());
        }
        // If we have an odd number, then we will choose the last parent and add it in after mutating it
        if (num_of_offspring % 2 == 1) children.add(parents[num_of_offspring - 1].mutate());
        
        return children;
    }

    /*
     * Returns true if successfully added genome to the species
     */
    public boolean add(Genome to_add) {
        if (FOUNDATION.similar(to_add) == 1) {
            genomes.add(to_add);
            return true;
        }
        return false;
    }

    /*
     * Returns index of next genome to pick using roulette style
     */
    private int roulette(List<Integer> scores, int total_sum) {
        double score = RAND.nextDouble() * total_sum;
        double tracker = 0;
        for (int i = 0; i < scores.size(); i++) {
            tracker += scores.get(i);
            if (tracker >= score) {
                return i;
            }
        }
        return scores.size() - 1; // On the off chance of weird rounding errors (RAND.nextDouble could hit 0.999 repeating), we return the last index

    }
}
