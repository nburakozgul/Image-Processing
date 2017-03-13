import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;

public class Main {
    public static void main(String[] args) {
        ConvexHull CH = new ConvexHull();
        Image img = Load.invoke("perfectly_painted_cat.png");
        Display2D.invoke(img);
        Display2D.invoke(CH.convexHull(img));
    }
}
