package com.ip.hw01;

import vpt.Image;

/**
 * Created by bozgul on 03.03.2017.
 * help link = http://fourier.eng.hmc.edu/e161/lectures/HistogramEqualization.pdf
 */
public class HistogramEqualization {

    //

    /**
     * get histogram of an image
     * @param img input image
     * @return array of count of pixel intensities
     */
    private int[] histogramOfImage(Image img) {

        int[] histogram = new int[256];

        for(int i=0; i < histogram.length; i++)
            histogram[i] = 0;

        for(int j=0; j < img.getYDim(); j++) {
            for(int i=0; i < img.getXDim(); i++) {
                int index = img.getXYByte(i,j);
                histogram[index]++;
            }
        }
        return histogram;
    }


    /**
     * create a lookup table with histogram
     * @param img
     * @return lookup table of image which calculated with image histogram
     */
    private int[] createLUT(Image img) {

        int[] histogram = histogramOfImage(img);
        int[] lookup = new int[256];

        for(int i=0; i<lookup.length; i++) //empty table
            lookup[i] = 0;

        long sum = 0;

        float scale = (float) (255.0 / (img.getXDim() * img.getYDim()));

        for(int i=0; i < lookup.length; i++) { // filling the lookup table
            sum += histogram[i];
            int val = (int) (sum * scale + 0.5);
            if( val > 255)
                lookup[i] = 255;
            else
                lookup[i] = val;
        }

        return lookup;
    }

    /**
     * appling histogram equalization to image
     * @param img
     * @param sizeX
     * @param sizeY
     * @return
     */
    public Image equalize(Image img , int sizeX , int sizeY) {
        int[] lut = createLUT(img);

        Image resultImg = img.newInstance(img.getXDim(), img.getYDim(),img.getCDim());

        for(int j=0; j < sizeY; j++)
            for(int i=0; i < sizeX; i++)
                resultImg.setXYByte(i,j,lut[img.getXYByte(i,j)]); // we set the lookup table values

        return resultImg;

    }

    /**
     * histogram equalization adaptively
     * @param img
     * @return image
     */
    public Image equalizeAdaptively(Image img ) {
        int tileXDim = 80;
        int tileYDim = 60;
        int tileSizeX = img.getXDim() / tileXDim;
        int tileSizeY = img.getYDim() / tileYDim;

        Image resultImg = img.newInstance(img.getXDim(), img.getYDim(), img.getCDim());
        Image tile = img.newInstance(tileXDim, tileYDim , img.getCDim()); // we create tile image


        for (int y = 0 ; y < tileSizeY ; y++)
            for (int x = 0 ; x < tileSizeX ; x++) {
                for(int j=0; j < tileYDim; j++)
                    for(int i=0; i < tileXDim; i++) {
                        tile.setXYByte(i, j, img.getXYByte(i + x * tileXDim, j + y * tileYDim));
                    } // copy the pixels to tile

                tile = equalize(tile, tileXDim, tileYDim); // run histogram equalization on tile

                for(int j=0; j < tileYDim; j++)
                    for(int i=0; i < tileXDim; i++) {
                        resultImg.setXYByte(i + x * tileXDim, j + y * tileYDim, tile.getXYByte(i,j));
                    } // write back tile to result image
            }

        return resultImg;
    }

    /**
     * histogram equalization adaptively, calculated with the connected tiles
     * @param img
     * @return image
     */
    public Image equalizeAdaptivelyTogether(Image img) {
        int tileXDim = 80;
        int tileYDim = 60;
        int tileSizeX = img.getXDim() / tileXDim;
        int tileSizeY = img.getYDim() / tileYDim;

        Image resultImg = img.newInstance(img.getXDim(), img.getYDim(), img.getCDim());

        // area that have four tiles which will use for histogram equalization
        Image tile = img.newInstance(tileXDim*2, tileYDim*2 , img.getCDim());

        // area that we will write the histogram equalization result
        Image tileForBorders = img.newInstance(tileXDim, tileYDim, img.getCDim());


        for (int y = 0 ; y < tileSizeY ; y++)
            for (int x = 0 ; x < tileSizeX ; x++) {
                if ( y == tileSizeY -1 || x == tileSizeX -1 ){ // if we are on the image bound
                    // we are not using area equalization result, just using equalization result of one tile
                    for(int j=0; j < tileYDim; j++)
                        for(int i=0; i < tileXDim; i++) {
                            tileForBorders.setXYByte(i, j, img.getXYByte(i + x * tileXDim, j + y * tileYDim));
                        }

                    tileForBorders = equalize(tileForBorders, tileXDim, tileYDim);

                    for(int j=0; j < tileYDim; j++)
                        for(int i=0; i < tileXDim; i++) {
                            resultImg.setXYByte(i + x * tileXDim, j + y * tileYDim, tileForBorders.getXYByte(i,j));
                        }

                } else { // not on the bound
                    // copying the pixels to area image
                    for(int j=0; j < tileYDim*2; j++)
                        for(int i=0; i < tileXDim*2; i++) {
                            tile.setXYByte(i, j, img.getXYByte(i + x * tileXDim, j + y * tileYDim));
                        }

                    tile = equalize(tile, tileXDim, tileYDim); // equalization on area image return on tile image
                    // area image (160 * 120) to tile image ( 80 * 60 )

                    for(int j=0; j < tileYDim; j++)
                        for(int i=0; i < tileXDim; i++) {
                            resultImg.setXYByte(i + x * tileXDim, j + y * tileYDim, tile.getXYByte(i,j));
                        } // write back to result image
                }
            }

        return resultImg;
    }
}
