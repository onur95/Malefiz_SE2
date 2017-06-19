package com.mygdx.malefiz.tests;

import com.mygdx.malefiz.dice.Dice;

import org.junit.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by kstri on 14.06.2017.
 */

public class DiceTest {

    @Test
    public void test(){
        Dice dice = new Dice(true, null);
        assertEquals(dice.getResultNumber(), 1);
        for(int i = 0; i < 20; i++) {
            dice.randomNumber();
            assertEquals(dice.getResultNumber() >= 1 && dice.getResultNumber() <= 6, true);
        }
    }
}
