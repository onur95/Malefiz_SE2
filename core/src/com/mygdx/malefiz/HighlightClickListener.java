package com.mygdx.malefiz;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by kstri on 03.04.2017.
 */

public class HighlightClickListener extends ClickListener {

    private int column, row, actorIndex;
    public HighlightClickListener(int column, int row, int actorIndex) {
        this.column = column;
        this.row = row;
        this.actorIndex = actorIndex;
    }

    @Override
    public void clicked(InputEvent event, float x, float y)
    {
        boolean isBlock = Board.isBlock(column, row);
        boolean isPlayer = Board.isPlayer(column, row);
        if(isPlayer){
            BoardToPlayboard.setKickedIndex(actorIndex, true);
            Board.movePlayerToStart(column, row);
        }
        else if(isBlock){
            BoardToPlayboard.setKickedIndex(actorIndex, false);
        }
        boolean blockIsMoving = BoardToPlayboard.getKickedIndex() != -1 && Board.isField(column, row);
        Board.moveTo(this.column, this.row, blockIsMoving);
        BoardToPlayboard.moveToPosition(this.actorIndex, blockIsMoving);
        if(isPlayer) {
            BoardToPlayboard.moveKicked();
        }
        else if(isBlock){
            BoardToPlayboard.setActorsCount();
            Board.setFieldActive(column,row);
            Board.setAllHighlighted();
        }

    }

}
