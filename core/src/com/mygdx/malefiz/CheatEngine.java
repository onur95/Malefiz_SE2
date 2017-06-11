package com.mygdx.malefiz;

public class CheatEngine {

    public CheatEngine(){
        // Constructor
    }

    // Pseudo-Interface for calling right cheat.
    public void cheatCaller(String code) {
        // Call via ce in CheatEngineObserver
        switch(code){
            case "1":
                setBlocks();
                break;
            case "2":
                resetPlayer();
                break;
            case "3":
                moveToAnyField();
                break;
            default:
                // Literally do nothing.
                break;

        }
        /* Deprecated. Use instead, if switch malfunctions
        if(code.equals("1")){
            setBlocks();
        }
        else if(code.equals("2")) {
            resetPlayer();

        }else if(code.equals("3")){
            moveToAnyField();

        }else{
            // Just resume game.
        }
        */

    }

    private void setBlocks(){
        int start = 0;
        int max = 1;                                // For starters, set one block.
        for(start = 0; start < max; start++){
            /* TODO:
            // kickedIndex == im Figure-Array Block hinzufügen & auf diesen actorIndex zugreifen.
            //  Actorindex via stage.getActors >> Liste von Actors <=> ActorIndex = 1 Element der Liste.
            // KickedIndex == ActorIndex via Kick

            BoardToPlayboard.setAllHighlighted();
            BoardToPlayboard.setKickedIndex(0, false);
            BoardToPlayboard.moveToPosition(int actorIndex, true, int column, int row);


>> wie schaut des aus, wenn a Block verschobm weat:
view.setKickedIndex(actorIndex, false); //ActorIndex vom Block
view.setActorsCount();
board.setFieldActive(column,row); //Position des Blockes im Array
board.setAllHighlighted();
            */

        }
    }

    private void resetPlayer() {
        // TODO

    }

    private void moveToAnyField(){
        /*  TODO:
         if(BoardToPlayboard.getActorActive() != -1){                           // if Figure is selected only, otherwise error
            BoardToPlayboard.setAllHighlighted();
            BoardToPlayboard.moveToPosition(int actorIndex, true, int column, int row);

        }
        */

    }
}