package com.mygdx.malefiz.Tests;

import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.view.BoardToPlayboard;
import com.mygdx.malefiz.field.Field;
import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.testng.Assert.assertEquals;

/**
 * Created by Klaus on 10.06.2017.
 */

@RunWith(Parameterized.class)
public class BoardTestPart1 {
    private Board board;
    private int column;
    private int moveToColumn;
    private int row;
    private int moveToRow;
    private FieldStates expectedFieldState;
    private boolean blockMoving;

    public BoardTestPart1(int column, int row, Object expectedFieldState, int moveToColumn, int moveToRow, boolean blockMoving) {
        this.expectedFieldState =(FieldStates) expectedFieldState;
        this.row = row;
        this.column = column;
        this.moveToColumn = moveToColumn;
        this.moveToRow = moveToRow;
        this.blockMoving = blockMoving;
        this.board = new Board();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 12, 7, FieldStates.NOFIELD, 10, 15, false},
                { 0, 1, FieldStates.PLAYER1, 5, 5, false },
                { 1, 5, FieldStates.PLAYER2, 7, 2, false },
                { 2, 10, FieldStates.PLAYER3, 8, 8, false },
                { 1, 15, FieldStates.PLAYER4, 14, 1, false },
                { 16, 8, FieldStates.GOAL, 2, 3, false },
                { 15, 8, FieldStates.BLOCK, 0,0, true },
                { 5, 4, FieldStates.BLOCK,10, 3, true},
                { 3, 5, FieldStates.FIELD, 6, 14, false }
        });
    }

    @Test
    public void testGeneratedBoard(){
        Field[][] boardArray = board.getBoardArray();
        assertEquals(boardArray[column][row].getFieldState(), expectedFieldState);

    }

    @Test
    public void testMove(){
        Field[][] boardArray = board.getBoardArray();
        board.setFieldActive(column, row);
        FieldPosition expectedField = new FieldPosition(column, row);
        assertEquals(board.getFieldActive().getColumn(), expectedField.getColumn());
        assertEquals(board.getFieldActive().getRow(), expectedField.getRow());

        board.moveTo(moveToColumn, moveToRow, blockMoving);

        if(blockMoving) {
            assertEquals(boardArray[moveToColumn][moveToRow].getFieldState(), expectedFieldState);
        }
        else{
            assertEquals(boardArray[expectedField.getColumn()][expectedField.getRow()].getFieldState(), expectedField.getColumn() <= 2 ? FieldStates.NOFIELD : FieldStates.FIELD);
            assertEquals(boardArray[moveToColumn][moveToRow].getFieldState(), expectedFieldState);
        }
    }
}
