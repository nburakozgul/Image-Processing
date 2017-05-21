import vpt.ByteImage;
import vpt.Image;

import java.awt.*;

/**
 * Created by burakozgul on 14.05.2017.
 */
public class GreyToColor {
    private static final double purple = 270.0;
    private static Color[] rgb = new Color[256];

    /* Converter from internet */
    private static Color HSVToRGB(Double[] hsv){
        double c = hsv[1]*hsv[2];   //s*v
        double x = c* ( 1.0 - Math.abs( ( hsv[0]/60)%2 -1 ));
        double m = hsv[2] - c ;
        double r=0.0, g=0.0,b=0.0;

        if( hsv[0]>=0 && hsv[0]<60){
            r = c;
            g = x;
            b = 0.0;
        }else if(  hsv[0]<120){
            r = x;
            g = c;
            b = 0.0;
        }else if( hsv[0]<180){
            r = 0.0;
            g = c;
            b = x;
        }else if( hsv[0]<240){
            r = 0.0;
            g = x;
            b = c;
        }else if( hsv[0]<300){
            r = x;
            g = 0.0;
            b = c;
        }else if( hsv[0]<360){
            r = c;
            g = 0.0;
            b = x;
        }
        r = (r+m)*255;
        g = (g+m)*255;
        b = (b+m)*255;

        return new Color((int)r,(int)g,(int)b);
    }


    /* Color datasının bulunduğu image'i doldurur */
    private static void fillRGBArray(){
        double step = (Math.PI/2)/rgb.length;

        for(int i=0; i< rgb.length; ++i){
            rgb[i] = HSVToRGB(new Double[]{ (purple+Math.cos(i*step)*121) %360,1.0,1.0}); //scala da 121 tane renk var
        }
    }

    public static Image pseudoColorer(Image image){
        Image result = new ByteImage(image.getXDim(), image.getYDim(), 3);
        fillRGBArray();

        for(int x =0; x < image.getXDim(); ++x){
            for(int y =0; y < image.getYDim(); ++y){
                Color c = rgb[image.getXYByte(x, y)];
                result.setXYCByte(x, y, 0, c.getRed());
                result.setXYCByte(x, y, 1, c.getGreen());
                result.setXYCByte(x, y, 2, c.getBlue());
            }
        }

        return result;
    }
}
