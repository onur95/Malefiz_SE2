package com.mygdx.malefiz.sauce.tests;

import com.mygdx.malefiz.sauce.field.Field;
import com.mygdx.malefiz.sauce.field.FieldStates;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.testng.Assert.assertEquals;

/**
 * Created by Klaus on 11.06.2017.
 */

@RunWith(Parameterized.class)
public class FieldStateTest {

    private char input;
    private FieldStates expected;

    public FieldStateTest(char input, Object expected) {
        this.input = input;
        this.expected = (FieldStates) expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { '.', FieldStates.NOFIELD},
                { 'A', FieldStates.NOFIELD},
                { 'B', FieldStates.BLOCK},
                { 'G', FieldStates.GOAL},
                { '1', FieldStates.PLAYER1},
                { '2', FieldStates.PLAYER2},
                { '3', FieldStates.PLAYER3},
                { '4', FieldStates.PLAYER4},
                { '5', FieldStates.NOFIELD},
                { '6', FieldStates.NOFIELD},
                { 'o', FieldStates.FIELD},
                { 'O', FieldStates.NOFIELD},
                { 'Z', FieldStates.NOFIELD},
                { '0', FieldStates.NOFIELD},
        });
    }

    @Test
    public void test() {
        Field field = new Field(input);
        assertEquals(field.getFieldState(),expected);
    }
}
