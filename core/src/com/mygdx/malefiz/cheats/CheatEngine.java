package com.mygdx.malefiz.cheats;

import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

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

        }else{
            // Just resume game.
        }
    }

    /**
     *
     */
    private void setBlocks(){
        int start = 0;
        int max = 1;                                // For starters, set one block.
        for(start = 0; start < max; start++){


        }
    }

    /**
     * Currently resets player 2.
     */
    private void resetPlayer() {
        for(int x = 0; x < board.getBoardArray().length; x++) {
            for (int y = 0; y < board.getBoardArray()[x].length; y++) {
                if(board.getBoardArray()[x][y].getFieldState() == FieldStates.PLAYER2){
                    board.movePlayerToStart(x,y);
                }
            }
        }

    }

    /**
     * Enable movement to any field wanted.
     */
    private void moveToAnyField(){
        if(board.getFieldActive() != null){
            view.setAllHighlighted(true);
        }
    }
}