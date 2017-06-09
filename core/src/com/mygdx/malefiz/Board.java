package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;

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
    }

    private  String[] reverseBoardMeta(String [] defaultMeta){
        String[] tempMeta = defaultMeta;
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
                if(field != '.'){
                    int playerNumber = Character.getNumericValue(field);
                    if(playerNumber==player.getNumber()){
                        fieldTemp.setColumn(2);
                        fieldTemp.setRow(k);
                        break;
                    }
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
        if(column>2 && row >=0 && column<boardArray.length && row<boardArray[column].length &&(positionBefore == null || !(column ==positionBefore.getColumn() && row==positionBefore.getRow()))){
            FieldStates state = boardArray[column][row].getField_state();
            status = checkDiceField(state, column, row, dice, positionBeforeAfter, addHighlight);
        }
        return status;
    }

    private  boolean checkDiceField(FieldStates myState, int column, int row, int dice, FieldPosition positionBefore, boolean addHighlight){
        dice--;
        boolean status = false;
        boolean secondStatus = false;
        if((myState.equals(FieldStates.FIELD) || (dice==0 && myState.equals(FieldStates.BLOCK)) || (myState.ordinal()==player.getNumber() && dice != 0 || isPlayer(myState.ordinal()) && myState.ordinal() != player.getNumber()))&& !myState.equals(FieldStates.NOFIELD)){
            if(dice == 0){
                Gdx.app.log("Board","setHighlight: "+column+", "+row);
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
                    view.setHighlight(x,y);
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
