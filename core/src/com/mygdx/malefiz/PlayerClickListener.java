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
        if(Player.getHighlightedFiguresIndizes().contains(actorIndex) && isPlayersTurnHighlighted()) {
            Board.setFieldActive(this.column, this.row);
            BoardToPlayboard.setPlayerFiguresHighlighted(false);
            BoardToPlayboard.setPlayerFigureHighlighted(actorIndex, true);
            BoardToPlayboard.setActorActive(actorIndex);
        }
    }

    private boolean isPlayersTurnHighlighted(){
        boolean status = false;
        for(int index : Player.getHighlightedFiguresIndizes()){
            if(BoardToPlayboard.stage.getActors().get(index).isVisible()){
                status = true;
            }
        }
        return status;
    }
}
