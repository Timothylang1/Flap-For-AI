package NEAT;

import java.util.ArrayList;
import java.util.Comparator;

public class Species {
    private final Genome FOUNDATION; // The primary genome that will be used to see if potential genomes get added to this species
    private ArrayList<Genome> genomes = new ArrayList<>();

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
     * Create offspring, and include the parents of the child into the new population
     */
    public ArrayList<Genome> createOffspring(int num_of_offspring) {
        ArrayList<Genome> next_generation = new ArrayList<>();
        ArrayList<Genome> bestFit = getBestFit();
        Genome child = Genome.crossover(bestFit.get(0), bestFit.get(1));
        while (num_of_offspring != 0) {
            if (!bestFit.isEmpty()) next_generation.add(bestFit.remove(0));
            else next_generation.add(child.mutate());
            num_of_offspring -= 1;
        }
        return next_generation;

    }

    /*
     * Returns true if successfully added genome to the species
     */
    public boolean add(Genome to_add) {
        if (Genome.similar(FOUNDATION, to_add) == 1) {
            genomes.add(to_add);
            return true;
        }
        return false;
    }

    /*
     * Helper class for sorting genomes from highest to lowest
     */
    class SortGenomes implements Comparator<Genome> {
        public int compare(Genome a, Genome b) {
            return b.score - a.score;
        }
    }

    /*
     * Returns top two genomes of best fit
     */
    private ArrayList<Genome> getBestFit() {
        if (genomes.size() == 1) genomes.add(genomes.get(0).copy()); // If there is only one genome in this list, then we create a copy of it for crossover and mutations
        else genomes.sort(new SortGenomes()); // Otherwise we sort the genomes from highest fitness to lowest fitness
        return new ArrayList<>(genomes.subList(0, 2));
    }

    public static void main(String[] args) {
        Species species = new Species(new Genome());
        long test1 = 1;
        System.out.println((int) test1);
        double[] test = new double[100];
        for (int i = 0; i < test.length; i++) {
            System.out.println(test[i]);
        }
    }
}
