package GraphVisualization;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;

public class InfoText extends GraphicsGroup {
    private int generation_num = 0;
    private int best_score_num = 0;
    GraphicsText generation_label = new GraphicsText("Generation:  ");
    GraphicsText num_of_species_label = new GraphicsText("Species:  ");
    GraphicsText best_score_label = new GraphicsText("Best score:  ");
    GraphicsText generation = new GraphicsText("0");
    GraphicsText num_of_species = new GraphicsText("1");
    GraphicsText best_score = new GraphicsText("0");

    public InfoText(CanvasWindow canvas) {
        super();

        // Add all labels
        add(generation_label);
        add(num_of_species_label);
        add(best_score_label);
        add(generation);
        add(num_of_species);
        add(best_score);

        // Place everything
        System.out.println(generation_label.getHeight());
        generation_label.setPosition(0, 20);
        generation.setPosition(100, 20);
        num_of_species_label.setPosition(0, 40);
        num_of_species.setPosition(100, 40);
        best_score_label.setPosition(0, 60);
        best_score.setPosition(100, 60);

        canvas.add(this);

    }

    public void update(int species, int score) {
        // Update the current generation
        generation_num += 1;
        generation.setText(Integer.toString(generation_num));

        // Update the number of species
        num_of_species.setText(Integer.toString(species));

        // Update the best score
        if (score > best_score_num) {
            best_score.setText(Integer.toString(score));
            best_score_num = score;
        }
    }
}
