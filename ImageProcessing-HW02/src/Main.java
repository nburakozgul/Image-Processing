import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;

public class Main {
    public static void main(String[] args) {
        ConvexHull CH = new ConvexHull();
        int[][] filter = {{0,0,0},{1,-1,-1},{0,0,0}};


        // Goruntu belgesini diskten bellege aktar
        Image img = Load.invoke("perfectly_painted_cat.png");

        // goruntule
        Display2D.invoke(img);

        // 100, 100 konumuna yeni deger ata
        img = CH.applyFilterPublic(img, filter);

        // yeniden goruntule
        Display2D.invoke(img);
    }
}
