import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;

/**
 * Created by brk_o on 3/12/2017.
 */

public class Main {
    public static void main(String[] args) {
        ConvexHull CH = new ConvexHull(); //class that calculates the convex hull
        Image img = Load.invoke("perfectly_painted_cat.png");
        Display2D.invoke(img);
        Display2D.invoke(CH.convexHull(img));
    }
}
