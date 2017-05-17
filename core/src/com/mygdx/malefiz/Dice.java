package com.mygdx.malefiz;


import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by Lilibeth on 25.04.2017.
 */

public class Dice {
    private static int result = -1;  //fehler bzw. dfeault Wert
    private static boolean shaked;


    //static Malefiz game;


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

//wenn das gerät geschüttelt wird wird boolean auf true gesetzt & dadurch die animation gestartet.
    public static void shake(){
        float force= (float)Math.sqrt((Gdx.input.getAccelerometerX()* Gdx.input.getAccelerometerX()) + (Gdx.input.getAccelerometerY() * Gdx.input.getAccelerometerY()) + (Gdx.input.getAccelerometerZ() * Gdx.input.getAccelerometerZ()));

        if(force>10){
            shaked=true;}

        if(shaked)DiceAnimation.render();
    }


}

