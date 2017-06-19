package com.mygdx.malefiz.tests;

import com.mygdx.malefiz.UpdateHandler;
import com.mygdx.malefiz.cheats.CheatEngine;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testng.Assert;

import javax.swing.text.View;

/**
 * Created by tom on 19.06.17.
 */

public class CheatEngineTests {
    private View view;
    private Board board;
    private Dice dice;
    private UpdateHandler handler;

    CheatEngine ce;

    @Before
    public void createCheatEngine(){
        this.board = new Board();
        this.view = Mockito.mock(View.class);
        this.dice = Mockito.mock(Dice.class);
        this.handler = Mockito.mock(UpdateHandler.class);
        ce = new CheatEngine(Mockito.mock(BoardToPlayboard.class), this.board, this.dice);
    }

    @Test
    public void testExistenceOfCheatEngine(){
        Assert.assertNotNull(ce);
        Assert.assertNotNull(this.dice);
    }

    @Test
    public void cheatCallerTest(){
        String code = "";
        Assert.assertEquals(ce.cheatCaller(code), 0);

        code = "bLoCkS";
        Assert.assertEquals(ce.cheatCaller(code), 1);

        code = "r4";
        Assert.assertEquals(ce.cheatCaller(code), 2);
        code = "R3";
        Assert.assertEquals(ce.cheatCaller(code), 2);
        code = "Rr5";
        Assert.assertEquals(ce.cheatCaller(code), 0);
        code = "R5r3";
        Assert.assertEquals(ce.cheatCaller(code), 0);

        code = "fREEdoM";
        Assert.assertEquals(ce.cheatCaller(code), 3);

        code = "w1";
        Assert.assertEquals(ce.cheatCaller(code), 4);
        code = "W6";
        Assert.assertEquals(ce.cheatCaller(code), 4);
        code = "Ww3";
        Assert.assertEquals(ce.cheatCaller(code), 0);
        code = "w3W1";
        Assert.assertEquals(ce.cheatCaller(code), 0);
    }
}
