package com.guinea.geomatry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixUtils {

    public static List toList(Object[] object) {
        return Arrays.stream(object).collect(Collectors.toList());
    }

}
