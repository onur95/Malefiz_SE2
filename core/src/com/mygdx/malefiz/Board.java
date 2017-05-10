package com.mygdx.malefiz;

import java.lang.reflect.Array;

import jdk.nashorn.internal.ir.Block;

/**
 * Created by Klaus on 02.04.2017.
 */

public class Board {
    private static Field[][] boardArray; //Das aktuelle Spielfeld
    private static final String[] meta =   //Das Grundgerüst des Spielfeldes
            {   "........G........",
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
//                        "oo4ooBoo1Bo2oo3oB",
                    "..1...2...3...4..",
                    ".1.1.2.2.3.3.4.4.",
                    ".1.1.2.2.3.3.4.4."};
    private static final String[] boardMeta = reverseBoardMeta(meta);
    //G->Goal
    //B->Block
    //o->Normales Feld
    //.->kein benutzbares Feld
    //1-4->Player 1-4
    private static FieldPosition fieldActive;
    private static FieldPosition newPlayerPosition;


    public static void init(){
        boardArray = new Field[17][17];
        for(int column = boardMeta.length-1; column>=0; column--){

            for(int char_index = 0; char_index < boardMeta[column].length(); char_index++){
                boardArray[column][char_index] = new Field(boardMeta[column].charAt(char_index));
            }
        }
        /**Test-Data**/
        Player.setNumber(2);
        /**Test-Data**/
    }

    private static String[] reverseBoardMeta(String [] defaultMeta){
        String[] meta = defaultMeta;
        for(int i=0;i<meta.length/2;i++){
            String a = meta[meta.length-i-1];
            meta[meta.length-i-1] = meta[i] ;
            meta[i] = a;
        }
        return meta;
    }

    public static Field[][] getBoardArray(){
        return boardArray;
    }

    public static void setFieldActive(int column, int row){
        fieldActive = new FieldPosition(column, row);
    }

    public static void moveTo(int column, int row, boolean isBlock){
        if(isBlock){
            boardArray[column][row] = new Field('B');
            BoardToPlayboard.setKickedVisibility();
        }
        else {
            boardArray[column][row] = boardArray[fieldActive.getColumn()][fieldActive.getRow()];
            //Feld, auf dem der Spieler war, wird auf FIELD gesetzt
            boardArray[fieldActive.getColumn()][fieldActive.getRow()] = fieldActive.getColumn() <= 2 ? new Field('.') : new Field('o');
        }
        fieldActive = null;
    }

    public static FieldPosition getRealFieldActive(){
        FieldPosition fieldTemp = new FieldPosition(fieldActive.getColumn(), fieldActive.getRow()); // wenn =  fieldActive dann Referenz!!!!!
        if(fieldTemp.getColumn() < 2){
            for(int k=0;k<boardMeta[2].length();k++){
                char field = boardMeta[2].charAt(k);
                if(field == '.'){continue;}
                int player = Character.getNumericValue(field);
                if(player==Player.getNumber()){
                    fieldTemp.setColumn(2);
                    fieldTemp.setRow(k);
                    break;
                }
            }
        }
        return fieldTemp;
    }

    public static void higlightPositionsMovement (int dice, FieldPosition field, FieldPosition positionBefore) {
        checkFieldStates(field.getColumn()+1,field.getRow(),dice,positionBefore, field); //above
        checkFieldStates(field.getColumn()-1,field.getRow(),dice,positionBefore, field); //below
        checkFieldStates(field.getColumn(),field.getRow()-1,dice,positionBefore, field); //left
        checkFieldStates(field.getColumn(),field.getRow()+1,dice,positionBefore, field); //right
    }

    private static void checkFieldStates(int column, int row, int dice, FieldPosition positionBefore, FieldPosition positionBeforeAfter){
        if(column>2 && row >=0 && column<boardArray.length && row<boardArray[column].length &&(positionBefore == null || !(column ==positionBefore.getColumn() && row==positionBefore.getRow()))){
            FieldStates state=boardArray[column][row].getField_state();
            checkDiceField(state,column,row,dice,positionBeforeAfter);
        }
    }

    private static void checkDiceField(FieldStates myState, int column, int row, int dice, FieldPosition positionBefore){
        dice--;
        if((myState.equals(FieldStates.FIELD) || (dice==0 && myState.equals(FieldStates.BLOCK)) || (myState.ordinal()==Player.getNumber() && dice != 0 || isPlayer(myState.ordinal()) && myState.ordinal() != Player.getNumber()))&& !myState.equals(FieldStates.NOFIELD)){
            if(dice == 0){
                BoardToPlayboard.setHighlight(column,row);
            }
            else{
                higlightPositionsMovement(dice, new FieldPosition(column,row),positionBefore);
            }
        }
    }

    public static boolean isPlayer(int ordinal){
        return (ordinal >= 1 && ordinal <= 4);
    }

    public static boolean isPlayer(int column, int row){
        return isPlayer(boardArray[column][row].getField_state().ordinal());
    }

    public static boolean isBlock(int column, int row){
        return (boardArray[column][row].getField_state() == FieldStates.BLOCK);
    }

    public static boolean isField(int column, int row){
        return (boardArray[column][row].getField_state() == FieldStates.FIELD);
    }

    public static void movePlayerToStart(int column, int row){

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

    public static FieldPosition getNewPlayerPosition(){
        return newPlayerPosition;
    }

    public static void setAllHighlighted(){
        for(int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                if(boardArray[x][y].getField_state() == FieldStates.FIELD){
                    BoardToPlayboard.setHighlight(x,y);
                }
            }
        }
    }
}
