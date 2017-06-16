package com.mygdx.malefiz.sauce.field;


/**
 * Created by Klaus on 02.04.2017.
 */

public class Field {
    private FieldStates fieldState;

    public Field(char fieldType){
        init(fieldType);
    }

    public void init(char fieldType){
        switch (fieldType){
            case 'G':
                this.fieldState = FieldStates.GOAL;
                break;

            case 'B':
                this.fieldState = FieldStates.BLOCK;
                break;

            case 'o':
                this.fieldState = FieldStates.FIELD;
                break;

            case '1':
                this.fieldState = FieldStates.PLAYER1;
                break;

            case '2':
                this.fieldState = FieldStates.PLAYER2;
                break;

            case '3':
                this.fieldState = FieldStates.PLAYER3;
                break;

            case '4':
                this.fieldState = FieldStates.PLAYER4;
                break;

            default: // '.' is default
                this.fieldState = FieldStates.NOFIELD;
                break;
        }
    }

    public FieldStates getFieldState(){
        return this.fieldState;
    }

}
