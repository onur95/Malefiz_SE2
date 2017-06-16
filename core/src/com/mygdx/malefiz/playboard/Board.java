package com.mygdx.malefiz.playboard;

import com.mygdx.malefiz.field.Field;
import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.field.FieldStates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Klaus on 02.04.2017.
 */

public class Board {
    private Field[][] boardArray; //Das aktuelle Spielfeld
    private static final String[] meta =   //Das Grundgerüst des Spielfeldes
            {       "........G........",
                    "ooooooooBoooooooo",
                    "o...............o",
                    "ooooooooBoooooooo",
                    "........B........",
                    "......ooBoo......",
                    "......o...o......",
                    "....ooBoooBoo....",
                    "....o.......o....",
                    "..ooooooooooooo..",
                    "..o...o...o...o..",
                    "BoooBoooBoooBoooB",
                    "o...o...o...o...o",
                    "ooooooooooooooooo",
                    "..1...2...3...4..",
                    ".1.1.2.2.3.3.4.4.",
                    ".1.1.2.2.3.3.4.4."};       // Leftside: 0,0

    private final String[] boardMeta = reverseBoardMeta(meta);
    private static final Logger LOGGER = Logger.getLogger( Board.class.getName() );
    private FieldPosition fieldActive;
    private FieldPosition newPlayerPosition;
    private List<List<FieldPosition>> highlights;


    public Board(){
        this.highlights = new ArrayList<>();
        initHighlights();
        initBoard();
    }

    public void initHighlights(){
        highlights.clear();
        for(int i = 0; i < 5; i++){
            highlights.add(new ArrayList<FieldPosition>());
        }
    }

    private void initBoard(){
        boardArray = new Field[17][17];
        for(int column = boardMeta.length-1; column>=0; column--){
            for(int char_index = 0; char_index < boardMeta[column].length(); char_index++){
                boardArray[column][char_index] = new Field(boardMeta[column].charAt(char_index));
            }
        }
    }

    private  String[] reverseBoardMeta(String [] defaultMeta){
        String[] tempMeta = defaultMeta.clone();
        for(int i=0;i<tempMeta.length/2;i++){
            String a = tempMeta[tempMeta.length-i-1];
            tempMeta[tempMeta.length-i-1] = tempMeta[i] ;
            tempMeta[i] = a;
        }
        return tempMeta;
    }

    public  Field[][] getBoardArray(){
        return boardArray;
    }

    public  void setFieldActive(int column, int row){
        fieldActive = new FieldPosition(column, row);
    }

    public  void moveTo(int column, int row, boolean isBlock){
        if(isBlock){
            boardArray[column][row] = new Field('B');
        }
        else {
            boardArray[column][row] = boardArray[fieldActive.getColumn()][fieldActive.getRow()];
            //Feld, auf dem der Spieler war, wird auf FIELD gesetzt
            boardArray[fieldActive.getColumn()][fieldActive.getRow()] = fieldActive.getColumn() <= 2 ? new Field('.') : new Field('o');
        }
        fieldActive = null;
    }

    public FieldPosition getRealFieldActive(){
        FieldPosition fieldTemp = new FieldPosition(fieldActive.getColumn(), fieldActive.getRow()); // wenn =  fieldActive dann Referenz!!!!!
        if(fieldTemp.getColumn() < 2){
            for(int k=0;k<boardMeta[2].length();k++){
                char field = boardMeta[2].charAt(k);
                if(Character.isDigit(field) && (new Field(field)).getFieldState() == boardArray[fieldActive.getColumn()][fieldActive.getRow()].getFieldState()){
                    fieldTemp.setColumn(2);
                    fieldTemp.setRow(k);
                    break;
                }
            }
        }
        return fieldTemp;
    }

    public void higlightPositionsMovement (int dice, FieldPosition field, FieldPosition positionBefore, int figureIndex, FieldStates playerFieldState) {
        checkFieldStates(field.getColumn()+1,field.getRow(),dice,positionBefore, field, figureIndex, playerFieldState); //above
        checkFieldStates(field.getColumn()-1,field.getRow(),dice,positionBefore, field, figureIndex, playerFieldState); //below
        checkFieldStates(field.getColumn(),field.getRow()-1,dice,positionBefore, field, figureIndex, playerFieldState); //left
        checkFieldStates(field.getColumn(),field.getRow()+1,dice,positionBefore, field, figureIndex, playerFieldState); //right
    }

    private void checkFieldStates(int column, int row, int dice, FieldPosition positionBefore, FieldPosition positionBeforeAfter, int figureIndex, FieldStates playerFieldState){
        if(checkOutOfBounds(column, row) &&(positionBefore == null || !(column ==positionBefore.getColumn() && row==positionBefore.getRow()))){
            FieldStates state = boardArray[column][row].getFieldState();
            checkDiceField(state, column, row, dice, positionBeforeAfter, figureIndex, playerFieldState);
        }
    }

    private boolean checkOutOfBounds(int column, int row){
        return column>2 && row >=0 && column<boardArray.length && row<boardArray[column].length;
    }

    private void checkDiceField(FieldStates myState, int column, int row, int diceBefore, FieldPosition positionBefore, int figureIndex, FieldStates playerFieldState){
        int dice = diceBefore - 1;
        boolean playerNotKickable = dice == 0 && myState == playerFieldState;
        boolean blockNotKickable = myState == FieldStates.BLOCK && dice > 0;
        if(!(myState == FieldStates.NOFIELD || playerNotKickable || blockNotKickable)){
            if(dice == 0){
                LOGGER.log(Level.INFO, "Board: setHighlight("+column+" "+row+")");
                highlights.get(figureIndex).add(new FieldPosition(column, row));
            }
            else{
                higlightPositionsMovement(dice, new FieldPosition(column,row),positionBefore, figureIndex, playerFieldState);
            }
        }
    }

    public  boolean isPlayer(int ordinal){
        return ordinal >= 1 && ordinal <= 4;
    }

    public  boolean isPlayer(int column, int row){
        return isPlayer(boardArray[column][row].getFieldState().ordinal());
    }

    public  boolean isBlock(int column, int row){
        return boardArray[column][row].getFieldState() == FieldStates.BLOCK;
    }

    public  boolean isField(int column, int row){
        return boardArray[column][row].getFieldState() == FieldStates.FIELD;
    }

    public FieldPosition getFieldActive(){
        return fieldActive;
    }

    public  void movePlayerToStart(int column, int row){
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < boardArray[x].length; y++){
                if(Character.isDigit(boardMeta[x].charAt(y)) && Character.getNumericValue(boardMeta[x].charAt(y)) == boardArray[column][row].getFieldState().ordinal() && boardArray[x][y].getFieldState() == FieldStates.NOFIELD){
                    newPlayerPosition = new FieldPosition(x,y);
                    boardArray[x][y] = new Field(boardMeta[x].charAt(y));
                    return;
                }
            }
        }
    }

    public FieldPosition getNewPlayerPosition(){
        return newPlayerPosition;
    }

    /**
     * @param enteredCheat -- boolean used only for cheatentry. Enables *all* fields
     */
    public  void setAllHighlighted(boolean enteredCheat){
        initHighlights();
        for(int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                if(boardArray[x][y].getFieldState() == FieldStates.FIELD || enteredCheat && (boardArray[x][y].getFieldState() == FieldStates.GOAL || boardArray[x][y].getFieldState() == FieldStates.BLOCK)){
                    highlights.get(0).add(new FieldPosition(x,y));
                }
            }
        }
    }

    public void removePlayer(int player){
        for(int column = 0; column < boardArray.length; column++) {
            for(int row = 0; row < boardArray[column].length; row++){
                if(boardArray[column][row].getFieldState().ordinal() == player){
                    boardArray[column][row] = new Field('.');
                }
            }
        }
    }

    public List<List<FieldPosition>> getHighlights(){
        return highlights;
    }

    public String[] getBoardMeta() {
        return boardMeta;
    }
}
