package com.mygdx.malefiz;

import com.mygdx.malefiz.field.Field;
import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.field.FieldStates;
import com.mygdx.malefiz.view.BoardToPlayboard;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Klaus on 02.04.2017.
 */

public class Board {
    private  Player player;
    private BoardToPlayboard view;
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
    //G->Goal
    //B->Block
    //o->Normales Feld
    //.->kein benutzbares Feld
    //1-4->Player 1-4
    private FieldPosition fieldActive;
    private FieldPosition newPlayerPosition;


    public Board(Player play, BoardToPlayboard view){
        this.view = view;
        player = play;
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

    public boolean higlightPositionsMovement (int dice, FieldPosition field, FieldPosition positionBefore, boolean addHighlight) {
        boolean val1 = checkFieldStates(field.getColumn()+1,field.getRow(),dice,positionBefore, field, addHighlight); //above
        boolean val2 = checkFieldStates(field.getColumn()-1,field.getRow(),dice,positionBefore, field, addHighlight); //below
        boolean val3 = checkFieldStates(field.getColumn(),field.getRow()-1,dice,positionBefore, field, addHighlight); //left
        boolean val4 = checkFieldStates(field.getColumn(),field.getRow()+1,dice,positionBefore, field, addHighlight); //right
        return val1 || val2 || val3 || val4;
    }

    private  boolean checkFieldStates(int column, int row, int dice, FieldPosition positionBefore, FieldPosition positionBeforeAfter, boolean addHighlight){
        boolean status = false;
        if(checkOutOfBounds(column, row) &&(positionBefore == null || !(column ==positionBefore.getColumn() && row==positionBefore.getRow()))){
            FieldStates state = boardArray[column][row].getFieldState();
            status = checkDiceField(state, column, row, dice, positionBeforeAfter, addHighlight);
        }
        return status;
    }

    private boolean checkOutOfBounds(int column, int row){
        return column>2 && row >=0 && column<boardArray.length && row<boardArray[column].length;
    }

    private  boolean checkDiceField(FieldStates myState, int column, int row, int diceBefore, FieldPosition positionBefore, boolean addHighlight){
        int dice = diceBefore - 1;
        boolean status = false;
        boolean secondStatus = false;
        boolean playerNotKickable = dice == 0 && myState.ordinal() == player.getNumber();
        boolean blockNotKickable = myState == FieldStates.BLOCK && dice > 0;
        if(!(myState == FieldStates.NOFIELD || playerNotKickable || blockNotKickable)){
            if(dice == 0){
                LOGGER.log(Level.FINE, "Board: setHighlight("+column+" "+row+")");
                status = true;
                if(addHighlight) {
                    view.setHighlight(column, row);
                }
            }
            else{
                secondStatus = higlightPositionsMovement(dice, new FieldPosition(column,row),positionBefore, addHighlight);
            }
        }
        return status || secondStatus;
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

    public  void setAllHighlighted(){
        for(int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                if(boardArray[x][y].getFieldState() == FieldStates.FIELD){
                    view.setHighlight(x,y);
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
}
