package com.mygdx.malefiz;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Klaus on 02.04.2017.
 */

public class BoardToPlayboard {
    static Field[][] board;
    static final float xOffset = 0.02133333333333F; //in Prozent vom Spielfeld
    static Image player1;
    static Image player2;
    static Image player3;
    static Image player4;
    static Image block;
    static Stage stage;
    static float lineOffset;
    static float pointOffset;
    static Image highlight;
    static int actorActive;

    public static void init(){
        player1=new Image(new Texture("Player1-xl.png"));
        player2=new Image(new Texture("Player2-xl.png"));
        player3=new Image(new Texture("Player3-xl.png"));
        player4=new Image(new Texture("Player4-xl.png"));
        highlight=new Image(new Texture("Highlight-xl.png"));
        block=new Image(new Texture("Block-xl.png"));
        board = Board.getBoardArray();
        stage = MyMalefizGame.getState();
        float percentOffset =0.009333333F;
        lineOffset = stage.getWidth()*percentOffset;
        float percentPoint = 0.046666667F;
        pointOffset = lineOffset +stage.getWidth()*percentPoint;
    }

    public static void generate(){
        if(Board.getSomethingChanged()) {
            for(int column = 0; column < board.length; column++) {
                for (int i = 0; i < board[column].length; i++) {
                    setField(column,i);
                }
            }

            stage.act();
            stage.draw();

            Board.setSomethingChanged(false);
            /**Test-Data**/
            setPlayerFiguresHighlighted();
            /**Test-Data**/
        }

    }

    private static void setFirstFields(int column, int row){
        float yOffset, tempXOffset1, tempXOffset2;
        int status = -1; //0--> erste Zeile; 1--> zweite Zeile; -1 --> keine von beiden
        if(column == 0){
            yOffset = (float) (stage.getHeight()*0.015);
            tempXOffset1 = (float) (stage.getWidth() * 0.105);
            tempXOffset2 = (float) (stage.getWidth() * 0.187);
        }
        else{
            yOffset = (float) (stage.getHeight()*0.096);
            tempXOffset1 = (float) (stage.getWidth() * 0.078);
            tempXOffset2 = (float) (stage.getWidth() * 0.212);
        }

        float result;
        switch (row){
            case 1:
                result = 0;
                break;

            case 5:
                result = 1;
                break;

            case 9:
                result = 2;
                break;

            case 13:
                result = 3;
                break;

            default:
                result = -1;
        }
        if(result > -1){
            status = 0;
        }

        tempXOffset1 += (float) (stage.getWidth() * 0.223)*result;

        switch (row){
            case 3:
                result = 0;
                break;

            case 7:
                result = 1;
                break;

            case 11:
                result = 2;
                break;

            case 15:
                result = 3;
                break;

            default:
                result = -1;
                break;
        }
        if(result > -1){
            status = 1;
        }
        tempXOffset2 += (float) (stage.getWidth() * 0.223)*result;
        if(status > -1) {
            Image field = getFieldType(column, row, status == 0 ? tempXOffset1 : tempXOffset2, yOffset);

            if (field != null) {
                MoveToAction action = new MoveToAction();
                if (status == 0) {
                    action.setPosition(tempXOffset1, yOffset);
                    field.addAction(action);
                    stage.addActor(field);

                    setPlayerHighlight(column, row, tempXOffset1, yOffset);
                } else if (status == 1) {
                    action.setPosition(tempXOffset2, yOffset);
                    field.addAction(action);
                    stage.addActor(field);

                    setPlayerHighlight(column, row, tempXOffset2, yOffset);
                } else {
                    //Wenn die Position nicht im Startbereich liegt
                    return;
                }
            }
        }


    }

    private static void setHighlight(int column, int row, float offsetX, float offsetY){
        if (board[column][row].isHighlighted()) {
            MoveToAction action = new MoveToAction();
            action.setPosition(offsetX, offsetY);
            Image field = new Image(highlight.getDrawable());
            field.addAction(action);
            field.addListener(new HighlightClickListener(column, row, stage.getActors().size));
            stage.addActor(field);
        }
    }

    private static void setPlayerHighlight(int column, int row, float offsetX, float offsetY){
        if(board[column][row].getField_state().ordinal() == Player.getNumber()) {
            MoveToAction action = new MoveToAction();
            action.setPosition(offsetX, offsetY);
            Image field = new Image(highlight.getDrawable());
            field.addAction(action);
            field.setVisible(false);
            field.addListener(new PlayerClickListener(column, row, stage.getActors().size));
            stage.addActor(field);
            Player.addHighlightFigure(stage.getActors().size-1);
        }
    }

    private static Image getFieldType(int column, int row, float offsetX, float offsetY){
        Image field = null;
        switch (board[column][row].getField_state()) {
            case PLAYER1:
                field = new Image(player1.getDrawable());
                break;

            case PLAYER2:
                field = new Image(player2.getDrawable());
                break;

            case PLAYER3:
                field = new Image(player3.getDrawable());
                break;

            case PLAYER4:
                field = new Image(player4.getDrawable());
                break;

            case BLOCK:
                field = new Image(block.getDrawable());
                break;

            case GOAL:
                //Goal hat einen Clicklistener
                //Wenn es gehighlightet ist und man drauf klickt
                field = new Image();
                break;
        }
        return field;
    }

    public static void setPlayerFiguresHighlighted(){
        for(int index : Player.getHighlightedFiguresIndizes()){
            stage.getActors().get(index).setVisible(true);
        }
    }

    private static void setField(int column, int row){
        Image field;
        if(column<2){
            setFirstFields(column, row);
            return;
        }

        float tempXOffset = (stage.getWidth() * xOffset + lineOffset)+(row)*pointOffset;
        float yOffset = ((float) (0.143 * stage.getHeight()))+(column-2)*pointOffset;

        field = getFieldType(column, row, tempXOffset, yOffset);

        if (field != null) {
            MoveToAction action = new MoveToAction();
            action.setPosition(tempXOffset, yOffset);

            field.addAction(action);
            stage.addActor(field);
            setPlayerHighlight(column, row, tempXOffset, yOffset);
        }

        setHighlight(column, row, tempXOffset, yOffset);

    }

    public static  void deleteActor(int actorIndex){
        if(actorActive != -1) {
            //Highlight wieder verschwinden lassen
            stage.getActors().get(actorActive).setVisible(false);

            MoveToAction action = new MoveToAction();
            action.setPosition(stage.getActors().get(actorIndex).getX(), stage.getActors().get(actorIndex).getY());
            action.setDuration(1f);
            stage.getActors().get(actorActive-1).addAction(action);
//            action.setDuration(0);
            stage.getActors().get(actorActive).addAction(action);

            //Hier alle gehighlighteten Positionen löschen (außer die auf dem Spieler!!!)
            stage.getActors().get(actorIndex).remove();
            actorActive = -1;
        }
    }

    public static void setActorActive(int index){
        actorActive = index;
    }
}
