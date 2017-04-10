package com.mygdx.malefiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kstri on 04.04.2017.
 */

public class Player {
    private static int number;
    private static List<Integer> highlightedFiguresIndizes = new ArrayList<Integer>();

    public static void setNumber(int number1){
        number = number1;
    }

    public static int getNumber(){
        return number;
    }

    public static void addHighlightFigure(int index){

        highlightedFiguresIndizes.add(index);
    }

    public static List<Integer> getHighlightedFiguresIndizes() {
        return highlightedFiguresIndizes;
    }
}
