package com.mygdx.malefiz;


import java.util.Random;

/**
 * Created by Lilibeth on 25.04.2017.
 */

public class Dice {
    private static int result = -1;  //fehler bzw. dfeault Wert
   // public static Button btnDice;

    //static Malefiz game;



   /* public static void init() {

        btnDice = new TextButton("Würfeln", new TextButton.TextButtonStyle());
        btnDice.setVisible(true);
        btnDice.setBounds(100, 100, 100, 50);
        /*btnDice.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new DiceScreen(game));
            }
        });

    }*/

    //Zufallszahl zw 1 & 6
    public static int randomNumber() {
        Random r = new Random();
        result = r.nextInt(6) + 1;
        return result;
    }

    //Pfad für animation je nach augenzahl wird festgelegt
    public static String getResult(int result){
        String res="badlogic.jpg";
        switch (result) {
            case 1:
                res="Dice (1).png";
                break;
            case 2:
                res="Dice (2).png";
                break;
            case 3:
                res="Dice (3).png";
                break;
            case 4:
                res="Dice (4).png";
                break;
            case 5:
                res="Dice (5).png";
                break;
            case 6:
                res="Dice (6).png";
                break;
        }
        return res;
    }

    public static  int getResultNumber() {return result;}



}

