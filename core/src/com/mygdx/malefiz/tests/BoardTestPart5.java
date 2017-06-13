package com.mygdx.malefiz.tests;


import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.playboard.Board;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Klaus on 13.06.2017.
 */

public class BoardTestPart5 {
    private int playerCount = 0;
    private List<FieldPosition> expectedMove;
    private List<List<FieldPosition>> highlights;

    @Test
    public void testGeneratedHighlights(){
        Board board = new Board();
        highlights = new ArrayList<>();
        expectedMove = new ArrayList<>();

        moveFigure(board,0,1,8,4); //Figur von Spieler 1
        moveFigure(board,0,3,4,4); //Figur von Spieler 1
        moveFigure(board,1,1,3,2); //Figur von Spieler 1
        moveFigure(board,0,5,7,6); //Figur von Spieler 2
        moveFigure(board,1,5,3,5); //Figur von Spieler 2
        moveFigure(board,1,7,3,3); //Figur von Spieler 2

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
                    ".......2.3.3.4.4."
         */

        //Setzen der Highlights
        setHighlight(board, 8,4);
        setHighlight(board, 4,4);
        setHighlight(board, 3,2);
        setHighlight(board, 1,3);
        setHighlight(board, 2,2);

        //moves von Figur 1 (8,4)
        expectedMoveOfFigure(9,6, false);
        expectedMoveOfFigure(7,2, false);
        expectedMoveOfFigure(7,6, true);


        //moves von Figur 2 (4,4)
        expectedMoveOfFigure(3,6, true);

        //moves von Figur 3 (3,2)
        expectedMoveOfFigure(3,5, false);
        expectedMoveOfFigure(4,0, true);

        //moves von Figur 4 (1,3)
        //moves von Figur 5 (2,2)
        for(int i = 0; i < 2; i++) {
            startFieldMove();
        }


        assertEquals(highlights.size(), board.getHighlights().size());
        for(int i=0; i<5; i++){
            assertEquals(highlights.get(i).size(), board.getHighlights().get(i).size());
            for(FieldPosition position : highlights.get(i)){
                assertEquals(board.getHighlights().get(i).contains(position), true);
            }
        }

    }

    private void moveFigure(Board board, int column, int row, int toColumn, int toRow){
        board.setFieldActive(column, row);
        board.moveTo(toColumn, toRow, false);
    }

    private void setHighlight(Board board, int column, int row){
        board.setFieldActive(column,row);
        board.higlightPositionsMovement(3, board.getRealFieldActive(), null, playerCount, FieldStates.PLAYER1);
        playerCount++;
    }

    private void expectedMoveOfFigure(int column, int row, boolean clearList){
        expectedMove.add(new FieldPosition(column,row));
        if(clearList){
            addExcpectedMove();
        }
    }

    private void addExcpectedMove(){
        highlights.add(expectedMove);
        expectedMove = new ArrayList<>();
    }

    private void startFieldMove(){
        expectedMoveOfFigure(3,0, false);
        expectedMoveOfFigure(3,4, true);
    }

}
