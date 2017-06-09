package com.mygdx.malefiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kstri on 04.04.2017.
 */

public class Player {
    private int number;
    private List<Integer> highlightedFiguresIndizes = new ArrayList<Integer>();
    private List<FieldPosition> figurePositions = new ArrayList<FieldPosition>();

    public Player(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

    public void addHighlightFigure(int index){
        highlightedFiguresIndizes.add(index);
    }

    public void addFigurePosition(int column, int row){
        figurePositions.add(new FieldPosition(column, row));
    }

    public List<Integer> getHighlightedFiguresIndizes() {
        return highlightedFiguresIndizes;
    }

    public List<FieldPosition> getFiguresPosition(){
        return figurePositions;
    }
}
