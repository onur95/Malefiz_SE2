package com.mygdx.malefiz.tests;

import com.mygdx.malefiz.Player;
import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.playboard.Board;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.testng.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PlayerTests {
    private Board board;
    private Player player;
    private boolean isPlayer;
    private int expected;

    public PlayerTests(int input, int expected, boolean isPlayer) {
        this.player= new Player(input);
        this.expected = expected;
        this.isPlayer = isPlayer;
    }

    @Before
    public void setBoard(){
        board = new Board();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0, false }, { 1, 1, true }, { 2, 2, true }, { 3, 3, true }, { 4, 4, true }, { 5, 5, false }
        });
    }

    @Test
    public void test() {
        assertEquals(player.getNumber(), expected);
        assertEquals(board.isPlayer(player.getNumber()), isPlayer);
    }

    @Test
    public void testLists(){
        player.addFigurePosition(expected, expected);
        player.addHighlightFigure(expected);
        player.addHighlightFigure(expected+1);

        assertEquals(player.getFiguresPosition().size(),1);
        assertEquals(player.getHighlightedFiguresIndizes().size(),2);
        assertEquals(player.getHighlightedFiguresIndizes().contains(expected), true);
        assertEquals(player.getHighlightedFiguresIndizes().contains(expected+1), true);
        assertEquals(player.getFiguresPosition().contains(new FieldPosition(expected, expected)), true);

        assertEquals(player.getHighlightedFiguresIndizes().contains(expected-1), false);
        assertEquals(player.getFiguresPosition().contains(new FieldPosition(expected+1, expected+1)), false);

    }
}