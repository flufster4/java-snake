package com.guinea.geomatry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix<E> {

    private List<List<E>> elements;

    @SafeVarargs
    public Matrix(List<E>...elements) {
        this.elements = Arrays.asList(elements);
    }

    public int getSize() {
        int length = 0;
        for (List<E> element : elements) {
            length += element.size();
        }
        return length;
    }

    public List<E> getRow(int index) {
        return elements.get(index);
    }

    public void setRow(List<E> row, int rowIndex) {
        if (rowIndex > elements.size()) throw new IndexOutOfBoundsException("row index specified is larger then total amount of rows. To add a row use the add row function");
        elements.set(rowIndex, row);
    }

    public void addRow(List<E> row) {
        elements.add(row);
    }

}
