package com.mygdx.malefiz;

public class CheatEngine {
    // Pseudo-Interface for calling right cheat.
    // Static necessary (GameMenu.x.y-Button as Caller)
    public void cheatCaller(String code) {
        // TODO: Potential StringOps for more concrete cheatoperations
        if(code.equals("1")){
            setBlocks();
        }
        else if(code.equals("2")) {
            resetPlayer();

        }else if(code.equals("3")){
            moveToAnyField();

        }else{
            // Entered wrong cheat.
            // Set to 'ignore' and resume game.
        }

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