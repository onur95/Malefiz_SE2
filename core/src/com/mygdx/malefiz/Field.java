package com.mygdx.malefiz;

import static com.mygdx.malefiz.FieldStates.BLOCK;
import static com.mygdx.malefiz.FieldStates.FIELD;
import static com.mygdx.malefiz.FieldStates.GOAL;
import static com.mygdx.malefiz.FieldStates.NOFIELD;
import static com.mygdx.malefiz.FieldStates.PLAYER1;
import static com.mygdx.malefiz.FieldStates.PLAYER2;
import static com.mygdx.malefiz.FieldStates.PLAYER3;
import static com.mygdx.malefiz.FieldStates.PLAYER4;



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
                this.fieldState = GOAL;
                break;

            case 'B':
                this.fieldState = BLOCK;
                break;

            case 'o':
                this.fieldState = FIELD;
                break;

            case '1':
                this.fieldState = PLAYER1;
                break;

            case '2':
                this.fieldState = PLAYER2;
                break;

            case '3':
                this.fieldState = PLAYER3;
                break;

            case '4':
                this.fieldState = PLAYER4;
                break;

            default: // '.' is default
                this.fieldState = NOFIELD;
                break;
        }
    }

    public FieldStates getFieldState(){
        return this.fieldState;
    }

}
