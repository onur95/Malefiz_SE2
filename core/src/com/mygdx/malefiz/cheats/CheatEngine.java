package com.mygdx.malefiz.cheats;

import com.mygdx.malefiz.UpdateHandler;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.networking.Network;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

public class CheatEngine {
    private BoardToPlayboard view;
    private Board board;
    private Dice dice;
    private UpdateHandler handler;

    public CheatEngine(BoardToPlayboard view, Board board, Dice dice){
        this.board = board;
        this.view = view;
        this.dice = dice;
    }

    public void getUpdateHandler(){

    }

    public void relayCheater(int confirmedCheater){
        handler = view.getUpdateHandler();
        handler.getClient().sendCheater(confirmedCheater);
    }

    // Pseudo-Interface for calling right cheat.
    public void cheatCaller(String code) {
        // Call via ce in CheatEngineObserver
        if(code == null || code.length() == 0){
            return;
        }

        if(code.equals("1")){
            view.setCheatEnabled(true);
            setBlocks();
        }
        else if(code.equals("2")) {
            view.setCheatEnabled(true);
            resetPlayer();

        }else if(code.equals("3")){
            view.setCheatEnabled(true);
            moveToAnyField();

        }else if (code.equals("4")){
            view.setCheatEnabled(true);
            instantWin();
        }else if (code.equals("5")) {
            dice.setCheatEnabled(true);
            dice.setResult(5); //testdata; user should enter number between 1 and 6
            view.setPlayerFiguresHighlighted(true);
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
        dice.setShaked(true);
        view.setPlayerFiguresHighlighted(true);
    }

    /**
     * Just for experiments. moveToAnyField() bugs, when moving straight into the goal.
     * Remove at own discretion.
     * Descr.: Instant win for early terminations
     */
    private void instantWin() {

    }
}