package com.mygdx.malefiz.playboard.clicklistener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.Player;
import com.mygdx.malefiz.playboard.BoardToPlayboard;

/**
 * Created by kstri on 03.04.2017.
 */

public class PlayerClickListener extends ClickListener {

    private int column;
    private int row;
    private int actorIndex;
    private Player player;
    private BoardToPlayboard view;
    private int selectedFigure;

    public PlayerClickListener(int column, int row, int actorIndex, Player player, BoardToPlayboard view) {
        this.column = column;
        this.row = row;
        this.actorIndex = actorIndex;
        this.player = player;
        this.view = view;
    }

    @Override
    public void clicked(InputEvent event, float x, float y)
    {
        if(isPlayersTurnHighlighted()) {
            view.setHighlightsOfFigure(actorIndex,selectedFigure, column, row);
        }
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private boolean isPlayersTurnHighlighted(){
        //Falls der Spieler am Zug ist, kann er die gerade ausgewählte Figur dadurch wieder ändern
        boolean status = false;
        for(int index : player.getHighlightedFiguresIndizes()){
            if(view.getStage().getActors().get(index).isVisible()){
                status = true;
            }
        }
        for(int i = 0; i < player.getFiguresPosition().size(); i++){
            if(player.getFiguresPosition().get(i).getColumn() == this.column && player.getFiguresPosition().get(i).getRow() == this.row){
                if(!view.getPlayerMovesPossible().contains(i)) {
                    status = false;
                }
                else{
                    selectedFigure = i;
                }
            }
        }
        return status;
    }
}