package com.mygdx.malefiz;

import java.lang.reflect.Array;
import java.util.List;

import jdk.nashorn.internal.ir.Block;

/**
 * Created by Klaus on 02.04.2017.
 */

public class Board {
    private  Player player;
    private  BoardToPlayboard view;
    private  Field[][] boardArray; //Das aktuelle Spielfeld
    private  final String[] meta =   //Das Grundgerüst des Spielfeldes
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

// (4)                       "oo4ooBoo1Bo2oo3oB",

    private  final String[] boardMeta = reverseBoardMeta(meta);
    //G->Goal
    //B->Block
    //o->Normales Feld
    //.->kein benutzbares Feld
    //1-4->Player 1-4
    private  FieldPosition fieldActive;
    private  FieldPosition newPlayerPosition;


    public  void init(Player play, BoardToPlayboard view){
        this.view = view;
        player = play;
        boardArray = new Field[17][17];
        for(int column = boardMeta.length-1; column>=0; column--){

            for(int char_index = 0; char_index < boardMeta[column].length(); char_index++){
                boardArray[column][char_index] = new Field(boardMeta[column].charAt(char_index));
            }
        }
        /**Test-Data**/
//        Player.setNumber(2);
        /**Test-Data**/
    }

    private  String[] reverseBoardMeta(String [] defaultMeta){
        String[] meta = defaultMeta;
        for(int i=0;i<meta.length/2;i++){
            String a = meta[meta.length-i-1];
            meta[meta.length-i-1] = meta[i] ;
            meta[i] = a;
        }
        return meta;
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
            view.setKickedVisibility();
        }
        else {
            boardArray[column][row] = boardArray[fieldActive.getColumn()][fieldActive.getRow()];
            //Feld, auf dem der Spieler war, wird auf FIELD gesetzt
            boardArray[fieldActive.getColumn()][fieldActive.getRow()] = fieldActive.getColumn() <= 2 ? new Field('.') : new Field('o');
        }
        fieldActive = null;
    }

    public  FieldPosition getRealFieldActive(){
        FieldPosition fieldTemp = new FieldPosition(fieldActive.getColumn(), fieldActive.getRow()); // wenn =  fieldActive dann Referenz!!!!!
        if(fieldTemp.getColumn() < 2){
            for(int k=0;k<boardMeta[2].length();k++){
                char field = boardMeta[2].charAt(k);
                if(field == '.'){continue;}
                int playerNumber = Character.getNumericValue(field);
                if(playerNumber==player.getNumber()){
                    fieldTemp.setColumn(2);
                    fieldTemp.setRow(k);
                    break;
                }
            }
        }
        return fieldTemp;
    }

    public  void higlightPositionsMovement (int dice, FieldPosition field, FieldPosition positionBefore) {
        checkFieldStates(field.getColumn()+1,field.getRow(),dice,positionBefore, field); //above
        checkFieldStates(field.getColumn()-1,field.getRow(),dice,positionBefore, field); //below
        checkFieldStates(field.getColumn(),field.getRow()-1,dice,positionBefore, field); //left
        checkFieldStates(field.getColumn(),field.getRow()+1,dice,positionBefore, field); //right
    }

    private  void checkFieldStates(int column, int row, int dice, FieldPosition positionBefore, FieldPosition positionBeforeAfter){
        if(column>2 && row >=0 && column<boardArray.length && row<boardArray[column].length &&(positionBefore == null || !(column ==positionBefore.getColumn() && row==positionBefore.getRow()))){
            FieldStates state=boardArray[column][row].getField_state();
            checkDiceField(state,column,row,dice,positionBeforeAfter);
        }
    }

    private  void checkDiceField(FieldStates myState, int column, int row, int dice, FieldPosition positionBefore){
        dice--;
        if((myState.equals(FieldStates.FIELD) || (dice==0 && myState.equals(FieldStates.BLOCK)) || (myState.ordinal()==player.getNumber() && dice != 0 || isPlayer(myState.ordinal()) && myState.ordinal() != player.getNumber()))&& !myState.equals(FieldStates.NOFIELD)){
            if(dice == 0){
                view.setHighlight(column,row, false);
            }
            else{
                higlightPositionsMovement(dice, new FieldPosition(column,row),positionBefore);
            }
        }
    }

    public  boolean isPlayer(int ordinal){
        return (ordinal >= 1 && ordinal <= 4);
    }

    public  boolean isPlayer(int column, int row){
        return isPlayer(boardArray[column][row].getField_state().ordinal());
    }

    public  boolean isBlock(int column, int row){
        return (boardArray[column][row].getField_state() == FieldStates.BLOCK);
    }

    public  boolean isField(int column, int row){
        return (boardArray[column][row].getField_state() == FieldStates.FIELD);
    }

    public  FieldPosition getFieldActive(){
        return fieldActive;
    }

    public  void movePlayerToStart(int column, int row){

        for(int x = 0; x < 3; x++){
            for(int y = 0; y < boardArray[x].length; y++){
                if(boardMeta[x].charAt(y) != '.'){
                    int playerNumber = Character.getNumericValue(boardMeta[x].charAt(y));

                    if(playerNumber == boardArray[column][row].getField_state().ordinal() && boardArray[x][y].getField_state() == FieldStates.NOFIELD){
                        System.out.println("hier");
                        newPlayerPosition = new FieldPosition(x,y);
                        boardArray[x][y] = new Field(boardMeta[x].charAt(y));
                    }
                }
            }
        }
    }

    public  FieldPosition getNewPlayerPosition(){
        return newPlayerPosition;
    }

    public  void setAllHighlighted(){
        for(int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                if(boardArray[x][y].getField_state() == FieldStates.FIELD){
                    view.setHighlight(x,y, false);
                }
            }
        }
    }

    public void removePlayer(int player){
        for(int column = 0; column < boardArray.length; column++) {
            for(int row = 0; row < boardArray[column].length; row++){
                if(boardArray[column][row].getField_state().ordinal() == player){
                    boardArray[column][row] = new Field('.');
                }
            }
        }
    }


}
