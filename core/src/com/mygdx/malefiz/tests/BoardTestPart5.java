package com.mygdx.malefiz.tests;



import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.playboard.Board;

import org.junit.Before;
import org.junit.runners.Parameterized;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Klaus on 13.06.2017.
 */

public class BoardTestPart5 {

    @Test
    public void testGeneratedHighlights(){
        Board board = new Board();
        List<List<FieldPosition>> highlights = new ArrayList<>();
        List<FieldPosition> moveFigure = new ArrayList<>();

        board.setFieldActive(0, 1); //Figur von Spieler 1
        board.moveTo(8, 4, false);

        board.setFieldActive(0, 3); //Figur von Spieler 1
        board.moveTo(4, 4, false);

        board.setFieldActive(1, 1); //Figur von Spieler 1
        board.moveTo(3, 2, false);

        board.setFieldActive(0, 5); //Figur von Spieler 2
        board.moveTo(7, 6, false);

        board.setFieldActive(1, 5); //Figur von Spieler 2
        board.moveTo(3, 5, false);

        board.setFieldActive(1, 7); //Figur von Spieler 2
        board.moveTo(3, 3, false);

        /*entspricht
                    "........G........",
                    "ooooooooBoooooooo",
                    "o...............o",
                    "ooooooooBoooooooo",
                    "........B........",
                    "......ooBoo......",
                    "......o...o......",
                    "....ooBoooBoo....",
                    "....1.......o....",
                    "..oooo2oooooooo..",
                    "..o...o...o...o..",
                    "BoooBoooBoooBoooB",
                    "o...1...o...o...o",
                    "oo12o2ooooooooooo",
                    "..1...2...3...4..",
                    "...1.....3.3.4.4.",
                    ".......2.3.3.4.4."};
         */

        //Setzen der Highlights
        board.setFieldActive(8,4);
        board.higlightPositionsMovement(3, board.getRealFieldActive(), null, 0, FieldStates.PLAYER1);

        board.setFieldActive(4,4);
        board.higlightPositionsMovement(3, board.getRealFieldActive(), null, 1, FieldStates.PLAYER1);

        board.setFieldActive(3,2);
        board.higlightPositionsMovement(3, board.getRealFieldActive(), null, 2, FieldStates.PLAYER1);

        board.setFieldActive(1,3);
        board.higlightPositionsMovement(3, board.getRealFieldActive(), null, 3, FieldStates.PLAYER1);

        board.setFieldActive(2,2);
        board.higlightPositionsMovement(3, board.getRealFieldActive(), null, 4, FieldStates.PLAYER1);


        //moves von Figur 1 (8,4)
        moveFigure.add(new FieldPosition(9,6));
        moveFigure.add(new FieldPosition(7,2));
        moveFigure.add(new FieldPosition(7,6));

        highlights.add(moveFigure);
        moveFigure = new ArrayList<>();

        //moves von Figur 2 (4,4)
        moveFigure.add(new FieldPosition(3,6));

        highlights.add(moveFigure);
        moveFigure = new ArrayList<>();

        //moves von Figur 3 (3,2)
        moveFigure.add(new FieldPosition(3,5));
        moveFigure.add(new FieldPosition(4,0));

        highlights.add(moveFigure);
        moveFigure = new ArrayList<>();

        //moves von Figur 4 (1,3)
        moveFigure.add(new FieldPosition(3,0));
        moveFigure.add(new FieldPosition(3,4));

        highlights.add(moveFigure);
        moveFigure = new ArrayList<>();

        //moves von Figur 5 (2,2)
        moveFigure.add(new FieldPosition(3,0));
        moveFigure.add(new FieldPosition(3,4));
        highlights.add(moveFigure);


        assertEquals(highlights.size(), board.getHighlights().size());
        for(int i=0; i<5; i++){
            assertEquals(highlights.get(i).size(), board.getHighlights().get(i).size());
            for(FieldPosition position : highlights.get(i)){
                assertEquals(board.getHighlights().get(i).contains(position), true);
            }
        }

    }

}
