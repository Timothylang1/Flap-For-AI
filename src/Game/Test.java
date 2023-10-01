package Game;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        // CanvasWindow canvas = new CanvasWindow("test", 200, 200);
        // Rectangle red = new Rectangle(10, 10, 10, 10);
        // red.setFillColor(new Color(255, 0, 0));
        // GraphicsGroup redGroup = new GraphicsGroup(0, 0);
        // redGroup.add(red);
        // Rectangle blue = new Rectangle(10, 10, 10, 10);
        // blue.setFillColor(new Color(0, 0, 255));
        // GraphicsGroup blueGroup = new GraphicsGroup(0, 0);
        // blueGroup.add(blue);
        // canvas.add(blueGroup);
        // canvas.add(redGroup);
        // // blueGroup.remove(blue);

        // canvas.animate(() -> {

        // });
        // canvas.onDrag(pos -> {
        //     red.setCenter(pos.getPosition());
        //     if (red.getElementAt(blue.getCenter().getX(), blue.getCenter().getY()) != null) System.out.println("Y");
        //     else System.out.println("N");
        // });
        // ArrayList<Integer> test = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        // test.replaceAll(x -> {return x * 2;});
        // System.out.println(test);

        ArrayList<Integer> test = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 5, 6));
        test.add(test.size() - 2, 4);
        System.out.println(test);
        System.out.println(test.get(test.size() - 2 - 1));
    }
}
