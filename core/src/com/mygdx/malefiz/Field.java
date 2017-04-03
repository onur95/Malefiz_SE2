package com.mygdx.malefiz;

import static com.mygdx.malefiz.FieldStates.*;



/**
 * Created by Klaus on 02.04.2017.
 */

public class Field {
    private FieldStates field_state;
    private boolean highlight = false;

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
//        System.out.println(field_state);
    }

    public FieldStates getField_state(){
        return this.field_state;
    }

    public boolean isHighlighted(){
        return this.highlight;
    }

    public void setHighlighted(boolean status){
        this.highlight = status;
    }
}
