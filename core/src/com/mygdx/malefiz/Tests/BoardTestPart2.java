package com.mygdx.malefiz.Tests;

import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.view.BoardToPlayboard;
import com.mygdx.malefiz.field.FieldPosition;
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
public class BoardTestPart2 {
    private Board board;
    private Player player = new Player(0);
    private int column;
    private int expectedColumn;
    private int row;
    private int expectedRow;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {12, 7, 12, 7},
                {0, 1, 2,2},
                {1, 5, 2, 6},
                {2, 10, 2,10 },
                {1, 15, 2,14 },
                {16, 8, 16,8 },
                {15, 8, 15,8 },
                {5, 4, 5,4},
                {3, 5, 3,5 }
        });
    }

    public BoardTestPart2(int column, int row, int expectedColumn, int expectedRow) {
        player = new Player(player.getNumber()+1);
        this.row = row;
        this.column = column;
        this.expectedRow = expectedRow;
        this.expectedColumn = expectedColumn;
        this.board = new Board(player,new BoardToPlayboard());
    }

    @Test
    public void getRealPosition(){
        board.setFieldActive(column, row);
        FieldPosition expectedField = new FieldPosition(expectedColumn, expectedRow);

        assertEquals(board.getRealFieldActive().getColumn(), expectedField.getColumn());
        assertEquals(board.getRealFieldActive().getRow(), expectedField.getRow());
    }
}
