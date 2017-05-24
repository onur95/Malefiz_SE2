package com.mygdx.malefiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kstri on 04.04.2017.
 */

public class Player {
    private int number;
    private List<Integer> highlightedFiguresIndizes = new ArrayList<Integer>();

    public Player(int number){
        this.number = number;
    }

    public void setNumber(int number1){
        number = number1;
    }

    public int getNumber(){
        return number;
    }

    public void addHighlightFigure(int index){
        highlightedFiguresIndizes.add(index);
    }

    public List<Integer> getHighlightedFiguresIndizes() {
        return highlightedFiguresIndizes;
    }
}
