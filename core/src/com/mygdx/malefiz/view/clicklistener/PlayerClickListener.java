package com.mygdx.malefiz.view.clicklistener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.Player;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.view.BoardToPlayboard;

/**
 * Created by kstri on 03.04.2017.
 */

public class PlayerClickListener extends ClickListener {

    private int column;
    private int row;
    private int actorIndex;
    private Player player;
    private Board board;
    private BoardToPlayboard view;
    private Dice dice;

    public PlayerClickListener(int column, int row, int actorIndex, Player player, Board board, BoardToPlayboard view, Dice dice) {
        this.column = column;
        this.row = row;
        this.actorIndex = actorIndex;
        this.player = player;
        this.view = view;
        this.board = board;
        this.dice = dice;
    }

    @Override
    public void clicked(InputEvent event, float x, float y)
    {
        //Player.getHighlightedFiguresIndizes().contains(actorIndex) --> Wird eigentlich nicht gebraucht, da nur der ausgewählte Spieler diesen Listener besitzt
        if(isPlayersTurnHighlighted()) {
            view.removeHighlights();

            //alle anderen Highlights der Spielerfiguren bis auf die ausgewählte Figur auf visible: false setzen
            view.setPlayerFiguresHighlighted(false);
            view.setPlayerFigureHighlighted(actorIndex, true);

            view.setActorActive(actorIndex);
            view.setActorsCount();  //Um Highlights rauszulöschen

            board.setFieldActive(this.column, this.row);
            board.higlightPositionsMovement(dice.getResultNumber(), board.getRealFieldActive(), null, true);

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
            if(player.getFiguresPosition().get(i).getColumn() == this.column && player.getFiguresPosition().get(i).getRow() == this.row
                    && !view.getPlayerMovesPossible().contains(i)){
                status = false;
            }
        }
        return status;
    }
}
