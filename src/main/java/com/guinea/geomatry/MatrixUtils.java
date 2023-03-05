package com.guinea.geomatry;

public class MatrixUtils {

    public static int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.round(Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1,2)));
    }
}
