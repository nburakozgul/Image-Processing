import vpt.GlobalException;
import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;

public class Main {

    public static void main(String[] args) throws GlobalException {
        /* Part 1 */
        GreyToColor greyToColor = new GreyToColor();
        Image input = Load.invoke("question_1_kaplumbaga_terbiyecisi_osman_hamdi_bey.jpg");
        Image result1 = greyToColor.pseudoColorer(input);
        Display2D.invoke(result1,true);

        /* Part 2 */
        EdgeDetector edgeDetector = new EdgeDetector();
        Image image = edgeDetector.execute();

        Watersheds watersheds = new Watersheds(); // Loads image in the class

        /* threshold */
        for (int j = 0 ; j < image.getYDim() ; j++)
            for(int i = 0 ; i < image.getXDim(); i++) {
                if (image.getXYByte(i,j) <= 254)
                    image.setXYByte(i,j,0);
            }

        watersheds.waterSheds(image);
    }
}
