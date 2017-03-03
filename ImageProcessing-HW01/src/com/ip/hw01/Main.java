package com.ip.hw01;

import vpt.Image;
import vpt.algorithms.io.Load;
import vpt.algorithms.io.Save;

public class Main {
    public static void main(String[] args) {
        ImageProcessingMethod ipm = new ImageProcessingMethod();
        HistogramEqualization he = new HistogramEqualization();

        // Goruntu belgesini diskten bellege aktar
        Image img = Load.invoke("valve.png");

        Save.invoke(ipm.scale(img, 2.0, 2.0 , 0),"Result1-Nearest.png");
        Save.invoke(ipm.scale(img, 2.0, 2.0 , 1),"Result1-Bilinear.png");
        Save.invoke(ipm.scale(img, 2.0, 2.0 , 2),"Result1-Bicubic.png");
        Save.invoke(he.equalize(img, img.getXDim(), img.getYDim()),"Result2-Equalize.png");
        Save.invoke(he.equalizeAdaptively(img),"Result3-EqualizeAdaptively.png");
        Save.invoke(he.equalizeAdaptivelyTogether(img),"Result4-EqualizeAdaptivelyTogether.png");

        System.out.print("Calculation process finish..");

    }
}

