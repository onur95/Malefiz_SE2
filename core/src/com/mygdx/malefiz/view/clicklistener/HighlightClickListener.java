package com.mygdx.malefiz.view.clicklistener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.field.FieldPosition;
import com.mygdx.malefiz.networking.BoardUpdate;
import com.mygdx.malefiz.UpdateHandler;
import com.mygdx.malefiz.view.BoardToPlayboard;

/**
 * Created by kstri on 03.04.2017.
 */

public class HighlightClickListener extends ClickListener {

    private int column;
    private int row;
    private int actorIndex;
    private BoardToPlayboard view;
    private Board board;
    private UpdateHandler handler;

    public HighlightClickListener(int column, int row, int actorIndex, Board board, BoardToPlayboard view, UpdateHandler handler) {
        this.column = column;
        this.row = row;
        this.actorIndex = actorIndex;
        this.board = board;
        this.view = view;
        this.handler = handler;
    }

    @Override
    public void clicked(InputEvent event, float x, float y)
    {
        boolean isBlock = board.isBlock(column, row);
        boolean isPlayer = board.isPlayer(column, row);
        if(isPlayer){
            view.setKickedIndex(actorIndex, true);
            board.movePlayerToStart(column, row);
        }
        else if(isBlock){
            view.setKickedIndex(actorIndex, false);
        }

        int kickedIndex = view.getKickedIndex();

        boolean blockIsMoving = view.getKickedIndex() != -1 && board.isField(column, row);

        if (!blockIsMoving){
            handler.add(new BoardUpdate(view.getActorActive()-1,board.getFieldActive().getColumn(), board.getFieldActive().getRow())); //was wird bewegt
            handler.add(new BoardUpdate(kickedIndex != -1 ? kickedIndex : actorIndex, column, row)); //wohin wird es bewegt
        }
        else{
            view.setKickedVisibility(true);
        }

        board.moveTo(this.column, this.row, blockIsMoving);
        view.moveToPosition(this.actorIndex, blockIsMoving, column, row);
        if(isPlayer) {
            FieldPosition coordinates = view.moveKicked();
            //actorIndex ist hier eigentlich egal
            handler.add(new BoardUpdate(kickedIndex, coordinates.getColumn(), coordinates.getRow())); //Was passiert mit dem Spielkegel, der auf der Position ist, auf die der Kegel fährt
        }
        else if(isBlock){
            view.setActorsCount();
            board.setFieldActive(column,row);
            view.setAllHighlighted();
        }
        if(blockIsMoving){
            handler.add(new BoardUpdate(actorIndex, column, row)); //Was passiert mit dem Block, der auf der Position ist, auf die der Kegel fährt
        }
        view.checkFinished();

    }

}
