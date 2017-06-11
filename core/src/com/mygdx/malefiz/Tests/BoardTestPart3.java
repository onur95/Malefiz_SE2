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
public class BoardTestPart3 {
    private Board board;
    private int column;
    private int row;
    private int moveToColumn;
    private int moveToRow;
    private FieldStates expectedFieldState;

    public BoardTestPart3(int column, int row, int moveToColumn, int moveToRow, Object expectedFieldState) {
        this.row = row;
        this.column = column;
        this.moveToColumn = moveToColumn;
        this.moveToRow = moveToRow;
        this.board = new Board(new Player(1),new BoardToPlayboard());
        this.expectedFieldState = (FieldStates) expectedFieldState;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 1, 5, 4, FieldStates.PLAYER1},
                { 0, 3, 5, 5, FieldStates.PLAYER1},
                { 1, 5, 7, 2, FieldStates.PLAYER2},
                { 1, 7, 7, 1, FieldStates.PLAYER2},
                { 2, 10, 8, 8, FieldStates.PLAYER3},
                { 1, 9, 8,7, FieldStates.PLAYER3},
                { 1, 13, 14, 1, FieldStates.PLAYER4},
                { 1, 15, 14, 2, FieldStates.PLAYER4}
        });
    }

    @Test
    public void movePlayerToStart(){
        Field[][] boardArray = board.getBoardArray();
        board.setFieldActive(column, row);

        board.moveTo(moveToColumn, moveToRow, false);
        assertEquals(boardArray[column][row].getFieldState(), FieldStates.NOFIELD);

        board.movePlayerToStart(moveToColumn, moveToRow);
        assertEquals(boardArray[column][row].getFieldState(), expectedFieldState);

        FieldPosition expectedPosition = board.getNewPlayerPosition();
        assertEquals(expectedPosition.getColumn(), column);
        assertEquals(expectedPosition.getRow(), row);

    }
}
