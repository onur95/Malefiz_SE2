package com.mygdx.malefiz.networking;


// Schnittstelle zu Networking

import java.io.Serializable;

public class BoardUpdate implements Serializable{
    private int actorIndex;
    private int column;
    private int row;

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
