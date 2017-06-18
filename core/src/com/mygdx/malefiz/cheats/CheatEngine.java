package com.mygdx.malefiz.cheats;

import com.mygdx.malefiz.UpdateHandler;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CheatEngine {
    private BoardToPlayboard view;
    private Board board;
    private Dice dice;
    private UpdateHandler handler;
    private static final Logger LOGGER = Logger.getLogger( CheatEngine.class.getName() );



    public CheatEngine(BoardToPlayboard view, Board board, Dice dice){
        this.board = board;
        this.view = view;
        this.dice = dice;
    }

    // Pseudo-Interface for calling right cheat.
    public int cheatCaller(String code) {
        if(code == null || code.length() == 0){
            return 0;
        }

        if(code.toLowerCase().equals("blocks")){
            LOGGER.log(Level.SEVERE, "CheatEngine: Blocks");
            view.setCheatEnabled(true);
            setBlocks();
            return 1;
        }
        else if(code.matches("^[rR][1-4]$")) {
            // Samples: r1, R2, R3, r4 :: Not allowed: Rr2, RRRRR4, r4r3R1
            LOGGER.log(Level.SEVERE, "CheatEngine: Reset Player"+code.charAt(1));
            view.setCheatEnabled(true);
            resetPlayer(code.charAt(1));
            return 2;

        }else if(code.equalsIgnoreCase("Freedom")){
            LOGGER.log(Level.SEVERE, "CheatEngine: Free movement");
            view.setCheatEnabled(true);
            moveToAnyField();
            return 3;

        }else if(code.matches("^[wW][1-6]$")){
            LOGGER.log(Level.SEVERE, "CheatEngine: Set dice to"+code.charAt(1));
            // Samples: W4, w3, W2, W1
            dice.setCheatEnabled(true);
            dice.setResult(code.charAt(1));
            view.setPlayerFiguresHighlighted(true);
            return 4;
        }
        return 0;
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
    private void resetPlayer(int nr) {
        /*for(int x = 0; x < view.getBoard().getBoardArray().length; x++) {
            for (int y = 0; y < view.getBoard().getBoardArray()[x].length; y++) {
                if(view.getBoard().getBoardArray()[x][y].getFieldState() == getFieldStateOfAffectedPlayer(nr)){
                    view.getBoard().movePlayerToStart(x,y);
                }
            }
        }
        // TODO: Send within message to clients
        */
    }

    public FieldStates getFieldStateOfAffectedPlayer(int nr){
        if(nr == 1) {
            return FieldStates.PLAYER1;
        }
        if(nr == 2) {
            return FieldStates.PLAYER2;
        }
        if(nr == 3){
            return FieldStates.PLAYER3;
        }
        if(nr == 4){
            return FieldStates.PLAYER4;
        }
        return null;
    }

    /**
     * Enable movement to any field wanted.
     */
    private void moveToAnyField(){
        dice.setShaked(true);
        view.setPlayerFiguresHighlighted(true);
    }
}