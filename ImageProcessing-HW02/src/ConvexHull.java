import vpt.Image;
import vpt.algorithms.display.Display2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brk_o on 3/12/2017.
 */
public class ConvexHull {

    private Image createOneChannelImage(Image input){
        boolean hit = true;
        Image resultImg = input.newInstance(input.getXDim(), input.getYDim(),1);

        for(int y = 0 ; y < input.getYDim() ; y++) {
            for(int x = 0; x < input.getXDim() ; x++) {
                for (int c = 0 ; c < input.getCDim(); c++)  {
                    if (input.getXYCByte(x, y, c) == 0) {
                        hit = false;
                        break;
                    }
                }

                if (hit)
                    resultImg.setXYByte(x, y, 255);
                else
                    resultImg.setXYByte(x, y, 0);

                hit = true;
            }
        }
        return resultImg;
    }

    private Image applyFilter(Image input, int[][] filter ) {
        Image resultImg = input.newInstance(false);
        boolean hit = true;
        int filterValue;
        int imageValue;


        for(int y = 1 ; y < input.getYDim() -1 ; y++) {
            for(int x = 1; x < input.getXDim() -1 ; x++) {

                for(int j = -1 ; j < 2 ; j++) {
                    for (int i = -1 ; i < 2 ; i++) {
                        imageValue = input.getXYByte(x + i, y + j);
                        filterValue = filter[j+1][i+1];

                        if (filterValue == 0)
                            continue;
                        else if (filterValue == 1 && imageValue != 255) {
                            hit = false;
                            break;
                        } else if (filterValue == -1 && imageValue != 0) {
                            hit = false;
                            break;
                        }
                    }

                    if (!hit)
                        break;
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

    private Image applyFilterMultipleTime(Image input, int[][] filter) {
        input = createOneChannelImage(input);
        Image resultImage;
        Image inputCopy = input;

        resultImage = applyFilter(inputCopy,filter);

        while(!this.equal(resultImage,inputCopy)) {
            inputCopy = resultImage;
            resultImage = applyFilter(inputCopy,filter);
        }

        return resultImage;
    }

    private boolean equal(Image img1, Image img2) {
        if (img1.getXDim() != img2.getXDim())
            return false;
        if (img1.getYDim() != img2.getYDim())
            return false;

        for(int y = 0 ; y < img1.getYDim(); y++)
            for(int x = 0 ; x < img1.getXDim(); x++) {
                if (img1.getXYByte(x,y) != img2.getXYByte(x,y))
                    return false;
            }
        return true;
    }

    private Image conbineImages(List<Image> imageList) {
        Image resultImage = imageList.get(0).newInstance(false);
        int H = imageList.get(0).getYDim();
        int W = imageList.get(0).getXDim();

        boolean hit = false;

        for (int y = 0 ; y < H ; y++) {
            for (int x  = 0 ; x < W ; x++) {

                for (Image img : imageList) {
                    if (img.getXYByte(x, y) == 255) {
                        hit = true;
                        break;
                    }
                }

                if (hit) {
                    resultImage.setXYByte(x, y, 255);
                    hit = false;
                } else {
                    resultImage.setXYByte(x, y, 0);
                }
            }
        }

        return resultImage;
    }

    public Image convexHull(Image input) {
        input = createOneChannelImage(input);
        List<Image> imageList = new ArrayList<Image>();

        int[][] filter = {{1,0,0},{1,-1,0},{1,0,0}};
        Image resultImage = applyFilterMultipleTime(input, filter);
        imageList.add(resultImage);

        filter = new int[][]{{1, 1, 1}, {0, -1, 0}, {0, 0, 0}};
        resultImage = applyFilterMultipleTime(input, filter);
        imageList.add(resultImage);

        filter = new int[][]{{0, 0, 1}, {0, -1, 1}, {0, 0, 1}};
        resultImage = applyFilterMultipleTime(input, filter);
        imageList.add(resultImage);

        filter = new int[][]{{0, 0, 0}, {0, -1, 0}, {1, 1, 1}};
        resultImage = applyFilterMultipleTime(input, filter);
        imageList.add(resultImage);

        return conbineImages(imageList);
    }
}
