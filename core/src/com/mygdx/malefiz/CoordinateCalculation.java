package com.mygdx.malefiz;

import com.badlogic.gdx.scenes.scene2d.Stage;
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

        return new Coordinates(getXOffsetOfFirstRows(tempXOffset1,tempXOffset2,row), yOffset);
    }

    private  float getXOffsetOfFirstRows(float offsetOdd, float offsetEven,int row){
        int result;
        float resultXOffset = 0;
        boolean rowsOdd = false;
        switch (row){
            //Odd
            case 1:
                result = 0;
                rowsOdd = true;
                break;

            case 5:
                result = 1;
                rowsOdd = true;
                break;

            case 9:
                result = 2;
                rowsOdd = true;
                break;

            case 13:
                result = 3;
                rowsOdd = true;
                break;

            //even
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
        if(result != -1){
            float offset = rowsOdd ? offsetOdd : offsetEven;
            resultXOffset = offset + (float) (stage.getWidth() * 0.223)*result;
        }
        return resultXOffset;
    }


    private  float getOffsetXNormal(float row){
        float xOffset = 0.02133333333333F; //in Prozent vom Spielfeld
        return (stage.getWidth() * xOffset + lineOffset)+(row)*pointOffset;
    }

    private  float getOffsetYNormal(float column){
        return ((float) (0.143 * stage.getHeight()))+(column-2)*pointOffset;
    }
}
