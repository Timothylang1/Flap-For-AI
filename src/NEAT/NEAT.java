package NEAT;

import java.util.ArrayList;
import java.util.List;
import Game.Bird;
import Game.PipeHandler;
import edu.macalester.graphics.CanvasWindow;

public class NEAT {
    ArrayList<Species> species = new ArrayList<>(); // Holds all of the currently active species
    ArrayList<Genome> genomes = new ArrayList<>(); // Holds all of the currently active genomes
    ArrayList<Bird> birds = new ArrayList<>(); // Holds the birds
    private int score = 0; // How many frames the birds survived

    public NEAT(PipeHandler pipes, CanvasWindow canvas) {
        // First, create a new species to begin with
        Genome gene = new Genome();
        Bird bird = new Bird(pipes, gene, canvas);
        Species spec = new Species(gene);
        genomes.add(gene);
        birds.add(bird);
        species.add(spec);

        // Then create the rest of the genomes and add it to the species
        for (int i = 0; i < Neural_Constants.POPULATION - 1; i++) {
            gene = new Genome();
            bird = new Bird(pipes, gene, canvas);
            genomes.add(gene);
            birds.add(bird);
            spec.add(gene);
        }
    }

    /*
     * Moves all of the birds each frame. If all birds are dead, then return false, otherwise if we're still evaluating one bird, return true
     */
    public boolean move() {
        boolean bird_still_alive = false;
        for (Bird bird : birds) {
            if (bird.move(score)) bird_still_alive = true;
        }
        score += 1;
        return bird_still_alive;
    }

    public void reset() {
        createOffspring(); // Create new genomes
        for (int i = 0; i < birds.size(); i++) { // Reset birds and reconnect to a new genome
            birds.get(i).reset(genomes.get(0));
        } 
        createSpecies(); // Create new species
        score = 0; // Reset score
    }

    /*
     * Calculates how much offspring each species should produce, then swaps out the current genomes to the new ones that the offspring produced
     */
    private void createOffspring() {
        adjustFitness(); // First, adjust fitnesses
        List<Double> fitness = species.stream().map(x -> x.calculateSpeciesFitness()).toList();
        double total_fitness = fitness.stream().mapToDouble(Double::doubleValue).sum();
        List<Integer> birds_per_species = fitness.stream().map(x -> (int) Math.round(Neural_Constants.POPULATION * x / total_fitness)).toList(); // Calculate how many genomes per species required
        
        // Have each species create its offspring genomes and add it to the list        
        genomes.clear();
        for (int i = 0; i < species.size(); i++) {
            genomes.addAll(species.get(i).createOffspring(birds_per_species.get(i)));
        }
    }

    /*
     * Creates a new set of species based on the current genomes.
     * Essentially, we loop through each species and attempt to add the genome to it. If none of the species take it, we create a new species
     * with this genome as the FOUNDATION
     */
    private void createSpecies() {
        species.clear();
        genomes.forEach(gene -> {
            boolean match = false;
            for (Species spec : species) {
                if (spec.add(gene)) {
                    match = true;
                    break;
                }
            }
            if (!match) species.add(new Species(gene));
        });
    }

    /*
     * The purpose of this calculation is to encourage unique birds to have a stronger fitness because they're adjustment won't be that big
     * 1. total_difference = For each bird, calculate its similarity to all other birds, then sum those differences up
     * 2. adjusted_fitness = Divide the birds fitness by the total_difference
     */
    private void adjustFitness() {
        int[] total_differences = new int[genomes.size()]; // Holds all of the total differences for all the genomes
        for (int g1 = 0; g1 < genomes.size(); g1++) {
            for (int g2 = g1 + 1; g2 < genomes.size(); g2++) {
                int difference = Genome.similar(genomes.get(g1), genomes.get(g2));
                total_differences[g1] += difference;
                total_differences[g2] += difference;
            }
            // Take the total sum difference of this genome with all other genomes, and divide its score by that difference. That is the adjusted fitness
            genomes.get(g1).adjusted_score = (float) genomes.get(g1).score / total_differences[g1];
        }
        
    }

    public static void main(String[] args) {
        ArrayList<Double> fitness = new ArrayList<>();
        double total_fitness = fitness.stream().mapToDouble(Double::doubleValue).sum();
        System.out.println(total_fitness);

    }
}
