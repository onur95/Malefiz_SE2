package com.mygdx.malefiz;

/**
 * Created by kstri on 03.04.2017.
 */

public class FieldPosition {
    private int column, row;

    public FieldPosition(int column, int row){
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
