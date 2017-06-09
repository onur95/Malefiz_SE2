package com.mygdx.malefiz;


import java.util.Random;

/**
 * Created by Lilibeth on 25.04.2017.
 */

public class Dice {
    private int result = -1;  //fehler bzw. dfeault Wert
    private boolean shaked;
    private BoardToPlayboard view;
    private DiceAnimation diceAnimation;
    private boolean renderRunning = false;
    private boolean playerSet = false;

    public Dice(boolean shakedStatus){
        this.shaked = shakedStatus;
    }


    //Zufallszahl zw 1 & 6
    public int randomNumber() {
        Random r = new Random();
        result = r.nextInt(6) + 1;
        return result;
    }

    //Pfad für animation je nach augenzahl wird festgelegt
    public String getResult(int result){
        return "Dice ("+result+").png";
    }

    public  int getResultNumber() {return result;}

    //wenn das gerät geschüttelt wird wird boolean auf true gesetzt & dadurch die animation gestartet.
    public void shake(float force){
        if(diceAnimation == null){
            return;
        }

        if(!shaked && force > 11.0) {
                shaked = true;
                diceAnimation.create(getResult(randomNumber()));
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

    public void setView(BoardToPlayboard view){
        this.view = view;
    }

    public void setDiceAnimation(){
        DiceAnimation animation = new DiceAnimation();
        animation.create(getResult(randomNumber()));
        this.diceAnimation = animation;
    }

    public void setRenderRunning(boolean renderRunning){
        this.renderRunning = renderRunning;
    }

    public void setPlayerSet (boolean playerSet){
        this.playerSet = playerSet;
    }


}

