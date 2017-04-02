package com.mygdx.malefiz;

import java.lang.reflect.Array;

/**
 * Created by Klaus on 02.04.2017.
 */

public class Board {
    private static Field[][] boardArray; //Das aktuelle Spielfeld
    private static final String[] boardMeta =   //Das Grundgerüst des Spielfeldes
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
    private static boolean somethingChanged = false;
                //G->Goal
                //B->Block
                //o->Normales Feld
                //.->kein benutzbares Feld
                //1-4->Player 1-4


    public static void init(){
        reverseBoardMeta();
        boardArray = new Field[17][17];
        for(int column = boardMeta.length-1; column>=0; column--){

            for(int char_index = 0; char_index < boardMeta[column].length(); char_index++){
                boardArray[column][char_index] = new Field(boardMeta[column].charAt(char_index));
            }
        }
        somethingChanged = true;

    }

    private static void reverseBoardMeta(){
        for(int i=0;i<boardMeta.length/2;i++){
            String a = boardMeta[boardMeta.length-i-1];
            boardMeta[boardMeta.length-i-1] = boardMeta[i] ;
            boardMeta[i] = a;
        }
    }

    public static Field[][] getBoardArray(){
        return boardArray;
    }

    public static boolean getSomethingChanged(){
        return somethingChanged;
    }

    public static void setSomethingChanged(boolean status){
        somethingChanged = status;
    }
}
