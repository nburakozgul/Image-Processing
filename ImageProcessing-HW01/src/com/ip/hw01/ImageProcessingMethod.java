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
            int  x, y;
            for (int i=0; i<hCopy; i = (int) (i + scaleY)) {
                for (int j=0; j<wCopy; j++) {
                    x = (int) Math.floor(j/scaleX) ;
                    y = (int) Math.floor(i/scaleY);

                    if ( x == width - 1 )
                        copy.setXYByte(j , i , input.getXYByte(x,y));
                    else
                        copy.setXYByte(j , i , (int) (((input.getXYByte(x,y) * (scaleX - (j % scaleX)))
                                                + (input.getXYByte(x+1,y) * (j % scaleX))) / scaleX));
                }
            }

            for (int i=0; i<hCopy; i++) {
                for (int j=0; j<wCopy; j++) {
                    x = (int) Math.floor(j/scaleX) ;
                    y = (int) Math.floor(i/scaleY);

                    if ( y != height -1 && i % scaleY != 0) {
                        copy.setXYByte(j , i , (int) (((input.getXYByte(x,y) * (scaleY - (i % scaleY)))
                                + (input.getXYByte(x,y+1) * (i % scaleY))) / scaleY));
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
