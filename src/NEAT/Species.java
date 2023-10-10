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
     * Returns true if successfully added genome to the species
     */
    public boolean add(Genome to_add) {
        if (Genome.similar(FOUNDATION, to_add)) {
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
     * Returns top two genomes of best fit TEST THIS METHOD TO SEE IF SORT WORKS
     */
    public ArrayList<Genome> getBestFit() {
        genomes.sort(new SortGenomes());

        return ;
    }

}
