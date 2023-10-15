package NEAT;

import java.util.ArrayList;
import java.util.List;

import Game.Bird;
import Game.PipeHandler;
import edu.macalester.graphics.CanvasWindow;

/*
 * The main class that connectes species, genomes, and birds all together.
 */
public class NEAT {
    ArrayList<Species> species = new ArrayList<>(); // Holds all of the currently active species
    ArrayList<Genome> genomes = new ArrayList<>(); // Holds all of the currently active genomes
    ArrayList<Bird> birds = new ArrayList<>(); // Holds the birds
    private int score = 0; // How many frames the birds survived

    /*
     * Creates all birds and empty genomes. Since all the genomes are empty, they are the same, so we put them in a single species
     * that we also create to begin the game.
     */
    public NEAT(PipeHandler pipes) {
        // First, create a new species to begin with
        Genome gene = new Genome();
        Bird bird = new Bird(pipes, gene);
        Species spec = new Species(gene);
        genomes.add(gene);
        birds.add(bird);
        species.add(spec);

        // Then create the rest of the genomes and add it to the species
        for (int i = 0; i < Neural_Constants.POPULATION - 1; i++) {
            gene = new Genome();
            bird = new Bird(pipes, gene);
            genomes.add(gene);
            birds.add(bird);
            spec.add(gene);
        }
    }

    /*
     * Adds all birds to canvas
     */
    public void addBirds(CanvasWindow canvas) {
        birds.forEach(x -> x.addToCanvas(canvas));
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

    /*
     * Resets NEAT by first creating all the new genomes, assigning genomes to birds, then assigning genomes to species
     */
    public void reset() {
        createOffspring(); // Create new genomes
        for (int i = 0; i < birds.size(); i++) { // Reset birds and reconnect to a new genome
            birds.get(i).reset(genomes.get(i));
        } 
        createSpecies(); // Create new species
        System.out.println(species.size());
        score = 0; // Reset score
    }

    /*
     * Calculates how much offspring each species should produce, then swaps out the current genomes to the new ones that the offspring produced
     */
    private void createOffspring() {
        // First adjust fitnesses
        adjustFitness();

        // Second, figure out roughly how many birds per species using the abjusted fitness
        List<Double> fitness = species.stream().map(x -> x.calculateSpeciesFitness()).toList();
        double total_fitness = fitness.stream().mapToDouble(Double::doubleValue).sum();
        List<Double> birds_per_species = fitness.stream().map(x -> Neural_Constants.POPULATION * x / total_fitness).toList(); // Calculate how many genomes per species required
        
        // Third, round species to an integer, but maintain population size
        ArrayList<Integer> birds_per_species_rounded = new ArrayList<>();
        double tracker = 0;
        for (Double num : birds_per_species) {
            tracker += num;
            birds_per_species_rounded.add((int) tracker);
            tracker -= (int) tracker;
        }

        // If the tracker had a little bit of a rounding error (i.e. it's currently sitting at 0.9999999 at the end), then we can add one to the last population
        if (tracker > 0.5) birds_per_species_rounded.add(birds_per_species_rounded.remove(birds_per_species_rounded.size() - 1) + 1);

        // Have each species create its offspring genomes and add it to the list        
        genomes.clear();
        for (int i = 0; i < species.size(); i++) {
            genomes.addAll(species.get(i).createOffspring(birds_per_species_rounded.get(i)));
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
            for (int g2 = g1; g2 < genomes.size(); g2++) {
                int difference = Genome.similar(genomes.get(g1), genomes.get(g2));
                total_differences[g1] += difference;
                total_differences[g2] += difference;
            }
            // Take the total sum difference of this genome with all other genomes, and divide its score by that difference. That is the adjusted fitness
            genomes.get(g1).adjusted_score = (double) genomes.get(g1).score / total_differences[g1];
        }
    }

    /*
     * Gets most fit genome out of all genomes
     */
    public Genome getBest() {
        int maxScore = 0;
        Genome fittest_genome = null;
        for (Genome genome : genomes) {
            if (genome.score > maxScore) {
                maxScore = genome.score;
                fittest_genome = genome;
            }
        }
        return fittest_genome;
    }
}
