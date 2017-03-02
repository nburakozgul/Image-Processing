package com.ip.hw01;


import vpt.Image;
import vpt.algorithms.display.Display2D;
import vpt.algorithms.io.Load;

public class Main {
    public static void main(String[] args) {
        ImageProcessingMethod ipm = new ImageProcessingMethod();
        // Goruntu belgesini diskten bellege aktar
        Image img = Load.invoke("valve2.png");
        Display2D.invoke(img);
        Display2D.invoke(ipm.scale(img, 2.0, 2.0 , 0));
        Display2D.invoke(ipm.scale(img, 2.0, 2.0 , 1));

    }
}

