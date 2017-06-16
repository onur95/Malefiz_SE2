package com.mygdx.malefiz.sauce.cheats;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.malefiz.sauce.field.FieldStates;
import com.mygdx.malefiz.sauce.playboard.Board;
import com.mygdx.malefiz.sauce.playboard.BoardToPlayboard;

public class CheatEngine {
    private BoardToPlayboard view;
    private Board board;

    public CheatEngine(BoardToPlayboard view, Board board){
        this.board = board;
        this.view = view;
    }

    // Pseudo-Interface for calling right cheat.
    public void cheatCaller(String code) {
        // Call via ce in CheatEngineObserver
        // Using switch is not too good btw.
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
     *  Set a block on the com.mygdx.malefiz.sauce.field
     */
    private void setBlocks(){
        int start = 0;
        int max = 1;                                // For starters, set one block.
        for(start = 0; start < max; start++){
            if(board.getFieldActive() == null){
                // compliantBottel(white, Tom)

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
     * Enable movement to any com.mygdx.malefiz.sauce.field wanted.
     */
    private void moveToAnyField(){
        if(view.getActorActive() != -1){
            // Player chosen >> move
            view.setAllHighlighted(true);
            view.getBoard().setAllHighlighted(true);
            // Rest should be executed by HighLightClickListener
        }
        else{
            // Wait until figure chosen >> move
            // Select some Figure to move >> move

        }
    }

    /**
     * Just for experiments. moveToAnyField() bugs, when moving straight into the goal.
     * Remove at own discretion.
     * Descr.: Instant win for early terminations
     */
    private void instantWin() {
        // Get position of Goal & move there
        for(int x = 0; x < view.getBoard().getBoardArray().length; x++) {
            for (int y = 0; y < view.getBoard().getBoardArray()[x].length; y++) {
                if(view.getBoard().getBoardArray()[x][y].getFieldState() == FieldStates.GOAL){
                    view.getBoard().setFieldActive(x, y);   // Sufficient?
                }
            }
        }
    }
}