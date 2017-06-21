package com.mygdx.malefiz.cheats;

import com.badlogic.gdx.Gdx;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CheatEngine {
    private BoardToPlayboard view;
    private Dice dice;
    private static final Logger LOGGER = Logger.getLogger( CheatEngine.class.getName() );

    public CheatEngine(BoardToPlayboard view, Dice dice){
        this.view = view;
        this.dice = dice;
    }

    // Pseudo-Interface for calling right cheat.
    public void cheatCaller(String code) {
        Gdx.input.setOnscreenKeyboardVisible(false);
        if(code == null || code.length() == 0){
            dice.setShaked(false);
            return;
        }
        boolean cheated = true;
        if("freedom".equalsIgnoreCase(code)){
                moveToAnyField();
        }
        else if(code.matches("^[wW][1-6]$")) {
            setDiceCheat(Character.getNumericValue(code.charAt(1)));
        }
        else{
            cheated = false;
        }
        dice.setShaked(cheated);
    }

    private void setDiceCheat(int number){
        LOGGER.log(Level.SEVERE, "Set dice to "+number);
        dice.setCheatEnabled(true);
        dice.setResult(number);
        view.setPlayerFiguresHighlighted(true);
    }

    private void moveToAnyField(){
        LOGGER.log(Level.SEVERE, "Free movement");
        view.setCheatEnabled(true);
        dice.setShaked(true);
        view.setPlayerFiguresHighlighted(true);
    }


}