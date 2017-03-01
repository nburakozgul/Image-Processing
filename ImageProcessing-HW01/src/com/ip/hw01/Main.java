package com.ip.hw01;


import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;

public class Main {
    public static void main(String[] args) {
        // Goruntu belgesini diskten bellege aktar
        Image img = Load.invoke("valve.png");
        double scale = 2.0;

        // genisligi ogren
        int width = img.getXDim();
        // yuksekligi ogren
        int height = img.getYDim();

        int wCopy = (int) (width*scale);
        int hCopy = (int) (height*scale);


        Image copy = img.newInstance(wCopy, hCopy,img.getCDim());

        for (int i = 0 ; i < hCopy ; i++)
            for (int j = 0 ; j < wCopy ; j++) {
                int x = (int) Math.floor(j/scale);
                int y = (int) Math.floor(i/scale);

                copy.setXYByte(j , i , img.getXYByte(x,y));

            }

        // 100, 100 konumundaki degeri oku
        int p = img.getXYByte(100, 100);
        System.err.println(p);

        // goruntule
        Display2D.invoke(img);

        // 100, 100 konumuna yeni deger ata


        // yeniden goruntule
        Display2D.invoke(copy);
    }
}

