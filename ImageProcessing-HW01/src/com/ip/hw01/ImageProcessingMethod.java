package com.ip.hw01;

import vpt.Image;

/**
 * Created by bozgul on 02.03.2017.
 */

public class ImageProcessingMethod {

    /**
     * Scales picture
     * @param inputImg input image
     * @param scaleX scale factor on horizontal
     * @param scaleY scale factor on vertical
     * @param strat scale mod
     * @return scaled image
     */
    public Image scale(Image inputImg, double scaleX , double scaleY  , int strat) {
        if (scaleX < 1.0 || scaleY < 1.0) {
            System.out.println("Scales must be greater than 1.0");
            return null;
        }

        if (strat == 0) {
            return nearestNeighborInterpolation(inputImg, scaleX,scaleY);
        } else if (strat == 1) {
            return bilinearInterpolation(inputImg,  scaleX, scaleY);
        } else if (strat == 2 ) {
            return bicubicInterpolation(inputImg, scaleX, scaleY);
        } else {
            System.out.println("Strat must be one of these {0,1,2}");
            return null;
        }
    }

    /**
     * scales image with nearest neighbor interpolation
     * @param img
     * @param scaleX
     * @param scaleY
     * @return
     */
    private Image nearestNeighborInterpolation(Image img, double scaleX , double scaleY) {
        int x , y ;

        int width = img.getXDim();
        int height = img.getYDim();

        // scaled sizes
        int wCopy = (int) (width*scaleX);
        int hCopy = (int) (height*scaleY);

        Image resultImg = img.newInstance(wCopy, hCopy,img.getCDim());

        for (int i = 0 ; i < hCopy ; i++ )
            for (int j = 0 ; j < wCopy ; j++) {

                // with dividing the index to scale we are mapping index to input location
                x = (int) Math.floor(j/scaleX);
                y = (int) Math.floor(i/scaleY);

                resultImg.setXYByte(j , i , img.getXYByte(x,y));

            }
        return resultImg;
    }

    /**
     * image with bilinear interpolation
     * @param img
     * @param scaleX
     * @param scaleY
     * @return
     */
    private Image bilinearInterpolation(Image img, double scaleX, double scaleY) {
        int x, y;
        int P1, P2, P3, P4;

        int width = img.getXDim();
        int height = img.getYDim();

        int wCopy = (int) (width*scaleX);
        int hCopy = (int) (height*scaleY);

        Image resultImg = img.newInstance(wCopy, hCopy,img.getCDim());

        for (int i=0; i<hCopy; i++) {
            for (int j=0; j<wCopy; j++) {
                x = (int) Math.floor(j/scaleX);
                y = (int) Math.floor(i/scaleY);

                // we are calculating pixel distance to real index between 0 - 1
                double w = (j % scaleX) / scaleX;
                double h = (i % scaleY) / scaleY;


                if ( x == width - 1 || y == height -1 ) // if we are on the bound set same intensity
                    resultImg.setXYByte(j , i , resultImg.getXYByte(x,y));
                else {
                    P1 = img.getXYByte(x, y);
                    P2 = img.getXYByte(x + 1, y);
                    P3 = img.getXYByte(x, y + 1);
                    P4 = img.getXYByte(x + 1, y + 1);

                    // calculate average of four pixel value
                    int pixel = (int) (P1 * (1 - w) * (1 - h) + P2 * (w) * (1 - h) + P3 * (h) * (1 - w) + P4 * (w) * (h));

                    resultImg.setXYByte(j, i, pixel);

                }
            }
        }

        return resultImg;
    }

    /**
     * not working as expected
     * image with bicubic interpolation
     * @param img
     * @param scaleX
     * @param scaleY
     * @return
     */
    private Image bicubicInterpolation(Image img, double scaleX, double scaleY) {
        int x, y;
        int P1, P2, P3, P4; // points that in the same horizontal line
        double intPoints[] = new double[4]; //interpolation points

        int width = img.getXDim();
        int height = img.getYDim();

        int wCopy = (int) (width*scaleX);
        int hCopy = (int) (height*scaleY);

        Image resultImg = img.newInstance(wCopy, hCopy,img.getCDim());

        for (int i=0; i<hCopy; i++) {
            for (int j=0; j<wCopy; j++) {
                x = (int) Math.floor(j/scaleX);
                y = (int) Math.floor(i/scaleY);

                if ( x >= width - 3 || y >= height - 3 || x == 0 || y == 0) // on bounds we ignore
                    continue;
                else {
                    for (int k = 0 ; k < 4 ; k++) { // calculating on horizontal line
                        P1 = img.getXYByte(x - 1, y + k);
                        P2 = img.getXYByte(x , y + k);
                        P3 = img.getXYByte(x + 1, y + k);
                        P4 = img.getXYByte(x + 2, y + k);

                        intPoints[k] = (P2 + 0.5 * scaleX*(P3 - P1 + scaleX*(2.0*P1 - 5.0*P2 + 4.0*P3 - P4 + scaleX*(3.0*(P2 - P3) + P4 - P1))));
                    }
                    // we are interpolate on the vertical line using values which we calculated before on horizontal line
                    int pixel = (int) (intPoints[1] + 0.5 * scaleY*(intPoints[2] - intPoints[0] + scaleY*(2.0*intPoints[0] - 5.0*intPoints[1] + 4.0*intPoints[2] - intPoints[3] + scaleY*(3.0*(intPoints[1] - intPoints[2]) + intPoints[3] - intPoints[0]))));

                    resultImg.setXYByte(j, i, pixel);

                }
            }
        }
        return resultImg;
    }
}
