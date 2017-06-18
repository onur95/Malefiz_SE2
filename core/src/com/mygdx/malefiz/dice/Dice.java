package com.mygdx.malefiz.dice;


import com.mygdx.malefiz.playboard.BoardToPlayboard;

import java.util.Random;

/**
 * Created by Lilibeth on 25.04.2017.
 */

public class Dice {
    private int result = 1;  //dfeault Wert
    private boolean shaked;
    private BoardToPlayboard view;
    private DiceAnimation diceAnimation;
    private boolean renderRunning = false;
    private boolean playerSet = false;
    private boolean cheatEnabled = false;

    public Dice(boolean shakedStatus, BoardToPlayboard view){
        this.diceAnimation = new DiceAnimation();
        this.shaked = shakedStatus;
        this.view = view;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public boolean getCheatEnabled() {
        return cheatEnabled;
    }

    public void setCheatEnabled(boolean cheatEnabled) {
        this.cheatEnabled = cheatEnabled;
    }


    //Zufallszahl zw 1 & 6
    public int randomNumber() {
        Random r = new Random();
        result = r.nextInt(6) + 1;
        return result;
    }



    public  int getResultNumber() {return result;}

    //wenn das gerät geschüttelt wird wird boolean auf true gesetzt & dadurch die animation gestartet.
    public void shake(float force){
        if(!shaked && force > 15.0) {
                shaked = true;
                diceAnimation.create("Dice ("+randomNumber()+").png");
                diceAnimation.render();
                renderRunning = true;
                playerSet = false;
        }
        else if(renderRunning){
            diceAnimation.render();

            if(diceAnimation.renderFinished() && !playerSet){
                view.setPlayerFiguresHighlighted(true);
                playerSet = true;
            }
        }
    }

    public void setShaked(boolean status){
        shaked = status;
    }

    public void setRenderRunning(boolean renderRunning){
        this.renderRunning = renderRunning;
    }

    public void setPlayerSet (boolean playerSet){
        this.playerSet = playerSet;
    }


}

