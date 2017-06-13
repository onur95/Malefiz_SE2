package com.mygdx.malefiz.field;

/**
 * Created by kstri on 03.04.2017.
 */

public class FieldPosition {
    private int column;
    private int row;

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof FieldPosition) {
            FieldPosition rhs = (FieldPosition) o;
            return this.getRow() == rhs.getRow() && this.getColumn() == rhs.getColumn();
        }
        return false;
    }

    @Override
    public String toString(){
        return "column: "+this.column + " row: "+this.row;
    }
}
