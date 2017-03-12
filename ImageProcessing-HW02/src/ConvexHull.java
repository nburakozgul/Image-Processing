import vpt.Image;

/**
 * Created by brk_o on 3/12/2017.
 */
public class ConvexHull {

    private Image applyFilter(Image input, int[][] filter ) {
        Image resultImg = input.newInstance(input.getXDim(), input.getYDim(),input.getCDim());
        boolean hit = true;
        int filterValue;
        int imageValue;

        for(int y = 1 ; y < input.getYDim() - 1 ; y++) {
            for(int x = 1; x < input.getXDim() - 1 ; x++) {

                for(int j = -1 ; j < 2 ; j++) {
                    for (int i = -1 ; i < 2 ; i++) {
                        imageValue = input.getXYByte(x + i, y + j);
                        filterValue = filter[j+1][i+1];

                        if (filterValue == 1 && imageValue != 255) {
                            hit = false;
                        } else if (filterValue == -1 && imageValue != 0) {
                            hit = false;
                        } else if (filterValue == 0)
                            continue;
                    }
                }

                if (hit)
                    resultImg.setXYByte(x, y, 255);
                else
                    resultImg.setXYByte(x, y, input.getXYByte(x, y));

                hit = true;
            }
        }

        return resultImg;
    }

    public Image applyFilterPublic(Image img, int[][] filter) {
        return applyFilter( img , filter );
    }
}
