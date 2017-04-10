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
        Board.moveTo(this.column, this.row);
        BoardToPlayboard.moveToPosition(this.actorIndex);
    }

}
