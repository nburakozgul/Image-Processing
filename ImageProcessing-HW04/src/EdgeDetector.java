/**
 * Created by burakozgul on 19.05.2017.
 */

import vpt.Image;
import vpt.algorithms.io.Load;

public class EdgeDetector {

    // truncate color component to be between 0 and 255
    public static int truncate(int a) {
        if      (a <   0) return 0;
        else if (a > 255) return 255;
        else              return a;
    }



    public Image execute() {

        int[][] filter1 = {
                { -1,  0,  1 },
                { -2,  0,  2 },
                { -1,  0,  1 }
        };

        int[][] filter2 = {
                {  1,  2,  1 },
                {  0,  0,  0 },
                { -1, -2, -1 }
        };

        Image picture0 = Load.invoke("question_3_watershed_coins.jpg");
        int width    = picture0.getXDim();
        int height   = picture0.getYDim();
        Image picture1 = picture0.newInstance(picture0.getXDim(), picture0.getYDim(),1);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                // get 3-by-3 array of colors in neighborhood
                int[][] gray = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray[i][j] = picture0.getXYByte(x-1+i, y-1+j);
                    }
                }

                // apply filter
                int gray1 = 0, gray2 = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray1 += gray[i][j] * filter1[i][j];
                        gray2 += gray[i][j] * filter2[i][j];
                    }
                }
                int magnitude = truncate((int) Math.sqrt(gray1*gray1 + gray2*gray2));
                picture1.setXYByte(x, y, magnitude);
            }
        }

        return picture1;
    }
}
