package com.ip.hw01;

import vpt.Image;

/**
 * Created by bozgul on 02.03.2017.
 */
public class ImageProcessingMethod {
    public Image scale(Image input, double scaleX , double scaleY  , int strat) {
        if (scaleX < 1.0 || scaleY < 1.0) {
            System.out.println("Scales must be greater than 1.0");
            return null;
        }

        int width = input.getXDim();
        int height = input.getYDim();

        int wCopy = (int) (width*scaleX);
        int hCopy = (int) (height*scaleY);

        Image copy = input.newInstance(wCopy, hCopy,input.getCDim());

        if (strat == 0) {
            int x , y ;

            for (int i = 0 ; i < hCopy ; i++ )
                for (int j = 0 ; j < wCopy ; j++) {
                    x = (int) Math.floor(j/scaleX);
                    y = (int) Math.floor(i/scaleY);

                    copy.setXYByte(j , i , input.getXYByte(x,y));

                }


            return copy;
        } else if (strat == 1) {
            int x, y;
            int P1, P2, P3, P4;
            for (int i=0; i<hCopy; i++) {
                for (int j=0; j<wCopy; j++) {
                    x = (int) Math.floor(j/scaleX);
                    y = (int) Math.floor(i/scaleY);

                    double w = (j % scaleX) / scaleX;
                    double h = (i % scaleY) / scaleY;


                    if ( x == width - 1 || y == height -1 )
                        copy.setXYByte(j , i , input.getXYByte(x,y));
                    else {
                        P1 = input.getXYByte(x, y);
                        P2 = input.getXYByte(x + 1, y);
                        P3 = input.getXYByte(x, y + 1);
                        P4 = input.getXYByte(x + 1, y + 1);

                        int pixel = (int) (P1 * (1 - w) * (1 - h) + P2 * (w) * (1 - h) + P3 * (h) * (1 - w) + P4 * (w) * (h));

                        copy.setXYByte(j, i, pixel);

                    }
                }
            }

            return copy;
        } else {
            System.out.println("Strat must be one of these {0,1,2}");
            return null;
        }
    }
}
