package com.mygdx.malefiz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    public static void init(){
        player1=new Image(new Texture("Player1-xl.png"));
        player2=new Image(new Texture("Player2-xl.png"));
        player3=new Image(new Texture("Player3-xl.png"));
        player4=new Image(new Texture("Player4-xl.png"));
        block=new Image(new Texture("Block-xl.png"));
        board = Board.getBoardArray();
        stage = MyMalefizGame.getState();
        float percentOffset =0.009333333F;
        lineOffset = stage.getWidth()*percentOffset;
        float percentPoint = 0.046666667F;
        pointOffset = lineOffset +stage.getWidth()*percentPoint;

        System.out.println("init");
    }

    public static void render(){
        if(Board.getSomethingChanged()) {
            float tempXOffset;
            float yOffset = (float) (stage.getHeight()*0.015);

            //erste Reihe
            float tempXOffset1 = (float) (stage.getWidth() * 0.105);
            float tempXOffset2 = (float) (stage.getWidth() * 0.187);
            for (int i = 0; i < board[0].length; i++) {
                Image field = null;
                switch (board[0][i].getField_state()) {
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
                }
                if (field != null) {
                    MoveToAction action = new MoveToAction();
                    if ((i == 1 || i == 5 || i == 9 || i == 13)) {
                        action.setPosition(tempXOffset1, yOffset);
                        tempXOffset1 += (float) (stage.getWidth() * 0.223);
                    }
                    else{
                        action.setPosition(tempXOffset2, yOffset);
                        tempXOffset2 += (float) (stage.getWidth() * 0.223);
                    }
                    field.addAction(action);
                    stage.addActor(field);
                }
            }


            //Zweite Reihe
            yOffset = (float) (stage.getHeight()*0.096);
            tempXOffset1 = (float) (stage.getWidth() * 0.078);
            tempXOffset2 = (float) (stage.getWidth() * 0.212);
            for (int i = 0; i < board[1].length; i++) {
                Image field = null;
                switch (board[1][i].getField_state()) {
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
                }
                if (field != null) {
                    MoveToAction action = new MoveToAction();
                    if ((i == 1 || i == 5 || i == 9 || i == 13)) {
                        action.setPosition(tempXOffset1, yOffset);
                        tempXOffset1 += (float) (stage.getWidth() * 0.223);
                    }
                    else{
                        action.setPosition(tempXOffset2, yOffset);
                        tempXOffset2 += (float) (stage.getWidth() * 0.223);
                    }
                    field.addAction(action);
                    stage.addActor(field);
                }
            }

            yOffset = (float) (0.143 * stage.getHeight());
            for(int column = 2; column < board.length; column++) {
                tempXOffset = stage.getWidth() * xOffset + lineOffset;

                for (int i = 0; i < board[column].length; i++) {
                    Image field = null;
                    switch (board[column][i].getField_state()) {
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
                    }
                    if (field != null) {
                        MoveToAction action = new MoveToAction();
                      action.setPosition(tempXOffset, yOffset);

                        field.addAction(action);
                        stage.addActor(field);
                    }
                    tempXOffset += pointOffset;
                }
                yOffset += pointOffset;
            }

            stage.act();
            stage.draw();
            Board.setSomethingChanged(false);
        }

    }
}
