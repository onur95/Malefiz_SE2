package com.mygdx.malefiz;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by kstri on 07.06.2017.
 */

public class CoordinateCalculation {
    private Stage stage;
    private float lineOffset, pointOffset;

    public CoordinateCalculation(Stage stage){
        float percentOffset = 0.009333333F;
        float percentPoint = 0.046666667F;

        this.stage = stage;
        this.lineOffset = stage.getWidth()*percentOffset;
        this.pointOffset = lineOffset +stage.getWidth()*percentPoint;
    }

    public Coordinates getCoordinatesOfField(int column, int row){
        Image field;
        //Falls die ersten zwei Reihen: anders generieren
        if(column<2){
            return setFirstFields(column, row);
        }
        //sonst normal

        float tempXOffset = getOffsetXNormal(row);
        float yOffset = getOffsetYNormal(column);
        return new Coordinates(tempXOffset, yOffset);
    }

    private Coordinates setFirstFields(int column, int row){
        float yOffset, tempXOffset1, tempXOffset2;
        Coordinates coordinates = null;
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

        float result = getResultFirstRowsOdd(row);

        if(result > -1){
            status = 0;
        }

        tempXOffset1 += (float) (stage.getWidth() * 0.223)*result;

        result = getResultFirstRowsEven(row);

        if(result > -1){
            status = 1;
        }
        tempXOffset2 += (float) (stage.getWidth() * 0.223)*result;

        if(status > -1) {
            if (status == 0) {
                coordinates = new Coordinates(tempXOffset1, yOffset);
            } else { //status == 1
                coordinates = new Coordinates(tempXOffset2, yOffset);
            }
        }
        return coordinates;
    }

    private  int getResultFirstRowsEven(int row){
        int result;
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
        return result;
    }

    private  int getResultFirstRowsOdd(int row){
        int result;
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
        return result;
    }

    private  float getOffsetXNormal(float row){
        float xOffset = 0.02133333333333F; //in Prozent vom Spielfeld
        return (stage.getWidth() * xOffset + lineOffset)+(row)*pointOffset;
    }

    private  float getOffsetYNormal(float column){
        return ((float) (0.143 * stage.getHeight()))+(column-2)*pointOffset;
    }
}
