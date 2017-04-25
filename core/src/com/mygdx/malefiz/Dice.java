package com.mygdx.malefiz;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.Screens.DiceScreen;

import java.util.Random;

/**
 * Created by Lilibeth on 25.04.2017.
 */

public class Dice {
    private static int result = -1;  //fehler bzw. dfeault Wert
    public static Button btnDice;

    static Malefiz game;



    public static void init() {

        btnDice = new TextButton("Würfeln", new TextButton.TextButtonStyle());
        btnDice.setVisible(true);
        btnDice.setBounds(100, 100, 100, 50);
        btnDice.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new DiceScreen(game));
            }
        });

    }

    public static int randomNumber() {
        Random r = new Random();
        result = r.nextInt(6) + 1;
        return result;
    }


    public int getResult() {return result;}


    }

