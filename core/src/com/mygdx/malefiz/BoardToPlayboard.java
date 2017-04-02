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
//        float yTestOffset = 0.304666667F;
            float yTestOffset = 0.190F;
            float tempXOffset = stage.getWidth() * xOffset + lineOffset;
            int testColumn = 3;
            for (int i = 0; i < board[testColumn].length; i++) {
                Image field = null;
//            System.out.println(board);
//            System.out.println(i);
                switch (board[testColumn][i].getField_state()) {
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
                    action.setPosition(tempXOffset, stage.getHeight() * yTestOffset + lineOffset);

                    field.addAction(action);
                    stage.addActor(field);
                }
                tempXOffset += pointOffset;
            }

            stage.act();
            stage.draw();
            Board.setSomethingChanged(false);
        }

    }
}
