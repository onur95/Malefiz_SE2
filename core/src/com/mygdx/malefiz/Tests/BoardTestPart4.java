package com.mygdx.malefiz.Tests;

import com.mygdx.malefiz.field.Field;
import com.mygdx.malefiz.playboard.Board;

import org.junit.Before;
import org.junit.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * Created by Klaus on 10.06.2017.
 */

public class BoardTestPart4 {
    private Board board;

    @Before
    public void setBoard(){
        this.board = new Board();
    }


    @Test
    public void removePlayer(){
        remove(3, board);
        remove(2, board);
        remove(4, board);
    }

    public void remove(int removePlayer, Board board){
        board.removePlayer(removePlayer);
        Field[][] boardArray = board.getBoardArray();
        for(int column = 0; column < boardArray.length; column++){
            for(int row = 0; row < boardArray[column].length; row++){
                assertNotEquals(boardArray[column][row].getFieldState().ordinal(), removePlayer);
            }
        }
    }

    @Test
    public void testIsBlock(){
        assertEquals(board.isBlock(15,8), true);
        assertEquals(board.isBlock(5,4), true);
        assertEquals(board.isBlock(5,0), true);
        assertEquals(board.isBlock(15,7), false);
        assertEquals(board.isBlock(0,1), false);
        assertEquals(board.isBlock(5,1), false);
    }

    @Test
    public void testIsPlayer(){
        assertEquals(board.isPlayer(0), false);
        assertEquals(board.isPlayer(-1), false);
        assertEquals(board.isPlayer(50), false);
        assertEquals(board.isPlayer(10), false);
        assertEquals(board.isPlayer(5), false);
        assertEquals(board.isPlayer(1), true);
        assertEquals(board.isPlayer(2), true);
        assertEquals(board.isPlayer(3), true);
        assertEquals(board.isPlayer(4), true);

        assertEquals(board.isPlayer(0,1), true);
        assertEquals(board.isPlayer(1,5), true);
        assertEquals(board.isPlayer(2,10), true);
        assertEquals(board.isPlayer(1,4), false);
        assertEquals(board.isPlayer(0,2), false);
        assertEquals(board.isPlayer(5,1), false);
    }

    @Test
    public void testIsField(){
        assertEquals(board.isField(0,1), false);
        assertEquals(board.isField(16,8), false);
        assertEquals(board.isField(2,10), false);
        assertEquals(board.isField(1,4), false);
        assertEquals(board.isField(0,2), false);
        assertEquals(board.isField(5,0), false);
        assertEquals(board.isField(6,1), false);
        assertEquals(board.isField(15,7), true);
        assertEquals(board.isField(3,8), true);
        assertEquals(board.isField(3,5), true);
        assertEquals(board.isField(3,8), true);
        assertEquals(board.isField(6,2), true);
    }
}
