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
    private FieldStates field_state;

    public Field(char field_type){
        init(field_type);
    }

    public void init(char field_type){
        switch (field_type){
            case 'G':
                this.field_state = GOAL;
                break;

            case 'B':
                this.field_state = BLOCK;
                break;

            case 'o':
                this.field_state = FIELD;
                break;

            case '1':
                this.field_state = PLAYER1;
                break;

            case '2':
                this.field_state = PLAYER2;
                break;

            case '3':
                this.field_state = PLAYER3;
                break;

            case '4':
                this.field_state = PLAYER4;
                break;

            case '.':
                this.field_state = NOFIELD;
                break;
        }
    }

    public FieldStates getField_state(){
        return this.field_state;
    }

}
