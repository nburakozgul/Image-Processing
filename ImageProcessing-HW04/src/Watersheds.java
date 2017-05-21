import vpt.ByteImage;
import vpt.Image;
import vpt.algorithms.display.Display2D;

import java.awt.*;
import java.util.*;


/**
 * Created by burakozgul on 17.05.2017.
 */
public class Watersheds {
    private static final int INIT = -1; //initial value of lab image
    private static final int MASK = -2; //initial value at each level
    private static final int WSHED = 0; //label of the watershed pixels
    private static final int INQUEUE = -3; //
    private int curlab = 0; // curlab is the current label
    private Queue<Point> fifo = null;
    private int minPixel = 255;
    private int maxPixel = 0;
    private boolean flag ;


    public int[][] waterSheds(Image input) {
        fifo = new Queue<Point>();
        int lab[][] = new int[input.getYDim()][input.getXDim()];
        System.out.println(input.getXDim() + "," + input.getYDim());

        for(int y = 0 ; y < input.getYDim()  ; y++) {
            for (int x = 0; x < input.getXDim() ; x++) {
                lab[y][x] = INIT;
            }
        }

        // Sort pixel increasing order of grey values
        ArrayList<ArrayList<Point>> sortedPixel = sort(input);

        for(int h = minPixel; h < maxPixel ; h++) {
            for (int i = 0 ; i < sortedPixel.get(h).size() ; i++) {
                int x = sortedPixel.get(h).get(i).x;
                int y = sortedPixel.get(h).get(i).y;
                //System.out.println("x" + x + ",y" + y );
                lab[y][x] = MASK;

                if ( x != 0 && y != 0 && x != input.getXDim() && y != input.getYDim()) {
                    for (int xDim = -1 ; xDim <= 1 ; xDim++)
                        for(int yDim = -1 ; yDim <= 1 ; yDim++) {
                            if (lab[y + yDim][x + xDim] > 0 || lab[y + yDim][x + xDim] == WSHED) {
                                lab[y][x] = INQUEUE;
                                fifo.enqueue(new Point(x + xDim, y + yDim));
                            }
                        }
                }
            }

            while(!fifo.isEmpty()) { //26 loop
                Point p = fifo.dequeue();

                if ( p.x != 0 && p.y != 0 && p.x != input.getXDim() && p.y != input.getYDim())
                    for (int xDim = -1 ; xDim <= 1 ; xDim++)
                        for(int yDim = -1 ; yDim <= 1 ; yDim++) {
                            if (lab[p.y + yDim][p.x + xDim] > 0) {
                                if (lab[p.y][p.x] == INQUEUE || (lab[p.y][p.x] == WSHED && flag)) {
                                    lab[p.y][p.x] = lab[p.y + yDim][p.x + xDim];
                                } else if (lab[p.y][p.x] > 0  && lab[p.y][p.x] != lab[p.y + yDim][p.x + xDim]) {
                                    lab[p.y][p.x] = WSHED ;
                                    flag = false;
                                }
                            } else if (lab[p.y + yDim][p.x + xDim] == WSHED) {
                                if (lab[p.y][p.x] == INQUEUE) {
                                    lab[p.y][p.x] = WSHED;
                                    flag = true;
                                }
                            } else if (lab[p.y + yDim][p.x + xDim] == MASK) {
                                lab[p.y + yDim][p.x + xDim] = INQUEUE;
                                fifo.enqueue(new Point(p.x +xDim, p.y + yDim));
                            }
                        }// 51 end for
            } // 52 end loop


            //53 detect and process new minima at level h
            for (int i = 0 ; i < sortedPixel.get(h).size() ; i++) { //54
                int x = sortedPixel.get(h).get(i).x;
                int y = sortedPixel.get(h).get(i).y;
                if (lab[y][x] == MASK) { //56
                    curlab += 1; // 57
                    fifo.enqueue(new Point(x, y));
                    lab[y][x] = curlab;
                    while (!fifo.isEmpty()) { //59
                        Point q = fifo.dequeue();
                        if (q.x != 0 && q.y != 0 && q.x != input.getXDim() && q.y != input.getYDim()) {
                            for (int xDim = -1; xDim <= 1; xDim++)
                                for (int yDim = -1; yDim <= 1; yDim++) {
                                    if (lab[q.y + yDim][q.x + xDim] == MASK) {//62
                                        fifo.enqueue(new Point(q.x + xDim, q.y + yDim));
                                        lab[q.y + yDim][q.x + xDim] = curlab; //63
                                    }
                                }
                        }
                    }
                }

            }
        }

        ArrayList pixelCount = new ArrayList();

        for (int i = 0 ; i <= 200 ; i++)
            pixelCount.add(0);

        for (int i = 0 ; i < input.getYDim() ; i++)  {
            for (int j = 0 ; j < input.getXDim() ; j++)
                if (lab[i][j] >= 0 )
                    pixelCount.add(lab[i][j],(int)pixelCount.get(lab[i][j]) + 1);

            //System.out.println();
        }

        Image color = new ByteImage(input.getXDim(), input.getYDim(), 3);

        for(int k = 0 ; k < 200 ; k++) {
                for (int i = 0 ; i < input.getYDim() ; i++)  {
                    for (int j = 0 ; j < input.getXDim() ; j++)
                            if (lab[i][j] == k) {
                                if (k == 1) {
                                    color.setXYCByte(j,i,0,100);
                                    color.setXYCByte(j,i,1,5);
                                    color.setXYCByte(j,i,2,250);
                                } else if (k == 2) {
                                    color.setXYCByte(j,i,0,255);
                                    color.setXYCByte(j,i,1,0);
                                    color.setXYCByte(j,i,2,0);
                                } else if (k >=3 && k <= 5) {
                                    color.setXYCByte(j,i,0,0);
                                    color.setXYCByte(j,i,1,255);
                                    color.setXYCByte(j,i,2,0);
                                } else if (k == 10) {
                                    color.setXYCByte(j,i,0,204);
                                    color.setXYCByte(j,i,1,0);
                                    color.setXYCByte(j,i,2,255);
                                } else if (k == 44) {
                                    color.setXYCByte(j,i,0,255);
                                    color.setXYCByte(j,i,1,204);
                                    color.setXYCByte(j,i,2,0);
                                }else if (k == 50) {
                                    color.setXYCByte(j,i,0,102);
                                    color.setXYCByte(j,i,1,255);
                                    color.setXYCByte(j,i,2,255);
                                }else if (k >= 51 && k <= 70) {
                                    color.setXYCByte(j,i,0,255);
                                    color.setXYCByte(j,i,1,179);
                                    color.setXYCByte(j,i,2,102);
                                }else if (k >= 120 && k <= 130) {
                                    color.setXYCByte(j,i,0,255);
                                    color.setXYCByte(j,i,1,102);
                                    color.setXYCByte(j,i,2,255);
                                }else if (k >= 142 && k <= 150) {
                                    color.setXYCByte(j,i,0,102);
                                    color.setXYCByte(j,i,1,179);
                                    color.setXYCByte(j,i,2,255);
                                }else if (k >= 151 && k <= 160) {
                                    color.setXYCByte(j,i,0,255);
                                    color.setXYCByte(j,i,1,255);
                                    color.setXYCByte(j,i,2,102);
                                }

                            }
                }
        }


        //Display2D.invoke(HoughTransformCircle.invoke(input));

        Display2D.invoke(color,true);
        System.out.println("gelmedi");

        return lab;
    }


    private ArrayList<ArrayList<Point>> sort (Image input) {
        ArrayList<ArrayList<Point>> list = new ArrayList<>();

        for (int i = 0 ; i < 256 ; i++)
            list.add(new ArrayList<Point>());


        for(int y = 0 ; y < input.getYDim() ; y++) {
            for (int x = 0; x < input.getXDim() ; x++) {
                if ( x != 0 && y != 0 && x != input.getXDim() -1  && y != input.getYDim() -1 ) {
                    int value = input.getXYByte(x,y);

                    if (value < minPixel)
                        minPixel = value;
                    if (value > maxPixel)
                        maxPixel = value;

                    list.get(value).add(new Point(x,y));
                }
            }
        }
        return list;
    }


}
