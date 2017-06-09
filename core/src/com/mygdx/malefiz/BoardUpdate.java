package com.mygdx.malefiz;


// Schnittstelle zu Networking

import java.io.Serializable;

public class BoardUpdate implements Serializable{
    private int actorIndex, column, row;

    public BoardUpdate(){
        //Wird für die Deserialization benötigt
    }

    public BoardUpdate(int actorIndex, int column, int row) {
        this.actorIndex = actorIndex;
        this.column = column;
        this.row = row;
    }

    public int getActorIndex() {
        return actorIndex;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
