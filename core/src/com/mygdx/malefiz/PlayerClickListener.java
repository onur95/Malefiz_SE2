package com.mygdx.malefiz;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by kstri on 03.04.2017.
 */

public class PlayerClickListener extends ClickListener {

    private int column, row, actorIndex;

    public PlayerClickListener(int column, int row, int actorIndex) {
        this.column = column;
        this.row = row;
        this.actorIndex = actorIndex;
    }

    @Override
    public void clicked(InputEvent event, float x, float y)
    {
        //Player.getHighlightedFiguresIndizes().contains(actorIndex)     -->Wird eigentlich nicht gebraucht, da nur der ausgewählte Spieler diesen Listener besitzt
        if(isPlayersTurnHighlighted()) {
            Board.setFieldActive(this.column, this.row);

            //alle anderen Highlights der Spielerfiguren bis auf die ausgewählte Figur auf visible: false setzen
            BoardToPlayboard.setPlayerFiguresHighlighted(false);
            BoardToPlayboard.setPlayerFigureHighlighted(actorIndex, true);

            BoardToPlayboard.setActorActive(actorIndex);
            //TODO: den gewürfelten Wert übergeben.
            Board.higlightPositionsMovement(5,Board.getRealFieldActive(),null);
        }
    }

    private boolean isPlayersTurnHighlighted(){
        //Falls der Spieler am Zug ist, kann er die gerade ausgewählte Figur dadurch wieder ändern
        boolean status = false;
        for(int index : Player.getHighlightedFiguresIndizes()){
            if(BoardToPlayboard.stage.getActors().get(index).isVisible()){
                status = true;
            }
        }
        return status;
    }
}
