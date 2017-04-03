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
        highlight=new Image(new Texture("Player4-xl.png"));
        block=new Image(new Texture("Block-xl.png"));
        board = Board.getBoardArray();
        stage = MyMalefizGame.getState();
        float percentOffset =0.009333333F;
        lineOffset = stage.getWidth()*percentOffset;
        float percentPoint = 0.046666667F;
        pointOffset = lineOffset +stage.getWidth()*percentPoint;

        System.out.println("init");
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
        }

    }

    private static void setFirstFields(int column, int i){
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
        switch (i){
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

        switch (i){
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

        Image field = null;
        switch (board[column][i].getField_state()) {
            case PLAYER1:
                field = new Image(player1.getDrawable());
                break;

            case PLAYER2:
                field = new Image(player2.getDrawable());
                field.addListener(new PlayerClickListener(column, i, stage.getActors().size-1));
                break;

            case PLAYER3:
                field = new Image(player3.getDrawable());
                break;

            case PLAYER4:
                field = new Image(player4.getDrawable());
                break;
        }
        if (field != null) {
            MoveToAction action = new MoveToAction();
            if (status == 0) {
                action.setPosition(tempXOffset1, yOffset);
            }
            else if(status == 1){
                action.setPosition(tempXOffset2, yOffset);
            }
            else{
                return;
            }
            field.addAction(action);
            stage.addActor(field);
        }

    }

    private static void setField(int column, int row){
        Image field = null;
        if(column<2){
            setFirstFields(column, row);
            return;
        }
        switch (board[column][row].getField_state()) {
            case PLAYER1:
                field = new Image(player1.getDrawable());
                break;

            case PLAYER2:
                field = new Image(player2.getDrawable());
                field.addListener(new PlayerClickListener(column, row, stage.getActors().size - 1));
                break;

            case PLAYER3:
                field = new Image(player3.getDrawable());
                break;

            case PLAYER4:
                field = new Image(player4.getDrawable());
                break;

            case BLOCK:
                field = new Image(block.getDrawable());
        }

        float tempXOffset = (stage.getWidth() * xOffset + lineOffset)+(row)*pointOffset;
        float yOffset = ((float) (0.143 * stage.getHeight()))+(column-2)*pointOffset;
        if (field != null) {
            MoveToAction action = new MoveToAction();
            action.setPosition(tempXOffset, yOffset);

            field.addAction(action);
            stage.addActor(field);
        }
        if (board[column][row].isHighlighted()) {
            MoveToAction action = new MoveToAction();
            action.setPosition(tempXOffset, yOffset);

            field = new Image(highlight.getDrawable());
            field.addAction(action);
            field.addListener(new HighlightClickListener(column, row, stage.getActors().size - 1));
            stage.addActor(field);
        }

    }

    public static  void deleteActor(int actorIndex){
        if(actorActive != -1) {
            MoveToAction action = new MoveToAction();
            action.setPosition(stage.getActors().get(actorIndex).getX(), stage.getActors().get(actorIndex).getY());
            action.setDuration(0.2f);
            stage.getActors().get(actorActive).addAction(action);
//        actorActive = actorIndex < actorActive ? actorActive-- : actorActive;

            stage.getActors().get(actorIndex).remove();


            actorActive = -1;
//        Board.setSomethingChanged(true);
        }
    }

    public static void setActorActive(int index){
        actorActive = index;
    }
}
