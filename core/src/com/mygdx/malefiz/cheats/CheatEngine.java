package com.mygdx.malefiz.cheats;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

public class CheatEngine {
    private BoardToPlayboard view;
    private Board board;
    private Dice dice;

    public CheatEngine(BoardToPlayboard view, Board board, Dice dice){
        this.board = board;
        this.view = view;
        this.dice = dice;
    }

    // Pseudo-Interface for calling right cheat.
    public void cheatCaller(String code) {
        // Call via ce in CheatEngineObserver
        BoardToPlayboard.setCheatEnabled(true);
        if(code.equals("1")){
            setBlocks();
        }
        else if(code.equals("2")) {
            resetPlayer();

        }else if(code.equals("3")){
            moveToAnyField();

        }else if (code.equals("4")){
            instantWin();
        }else{

        }
    }

    /**
     *  Set a block on the field
     */
    private void setBlocks(){
        int start = 0;
        int max = 1;                                // For starters, set one block.
        for(start = 0; start < max; start++){
            if(board.getFieldActive() == null){

            }
        }
    }

    /**
     * Currently resets player 2.
     */
    private void resetPlayer() {
        for(int x = 0; x < view.getBoard().getBoardArray().length; x++) {
            for (int y = 0; y < view.getBoard().getBoardArray()[x].length; y++) {
                if(view.getBoard().getBoardArray()[x][y].getFieldState() == FieldStates.PLAYER2){
                    view.getBoard().movePlayerToStart(x,y);
                }
            }
        }
        // TODO: Send within message to clients


    }

    /**
     * Enable movement to any field wanted.
     */
    private void moveToAnyField(){
        view.setPlayerFiguresHighlighted(true);
        dice.setShaked(true);

    }

    /**
     * Just for experiments. moveToAnyField() bugs, when moving straight into the goal.
     * Remove at own discretion.
     * Descr.: Instant win for early terminations
     */
    private void instantWin() {

    }
}