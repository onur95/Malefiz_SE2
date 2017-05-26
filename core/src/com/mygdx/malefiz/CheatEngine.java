package com.mygdx.malefiz;

public class CheatEngine {

    // Pseudo-Interface for calling right cheat.
    public void cheatEngine(String code) {
        // TODO: Potential StringOps for more concrete cheatoperations

        if(code.equals("setBlocks")){
            setBlocks();
        }
        else if(code.equals("ResetPlayer")) {
            resetPlayer();

        }else if(/*Enter stringcheck*/false){
            // Enter new cheat.

        }else{
            // Entered wrong cheat.
            // Set to 'ignore' and resume game.
        }

    }

    // Cheat-Executions below
    public void setBlocks(){

    }

    public void resetPlayer(){
        // TODO
    }



}
