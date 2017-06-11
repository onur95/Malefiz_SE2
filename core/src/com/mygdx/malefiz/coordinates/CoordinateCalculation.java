package com.mygdx.malefiz.coordinates;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by kstri on 07.06.2017.
 */

public class CoordinateCalculation {
    private Stage stage;
    private float lineOffset;
    private float pointOffset;

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
        float yOffset;
        float tempXOffset1;
        float tempXOffset2;

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
        float offset;
        float resultXOffset;
        boolean rowsOdd = (row-1) %4 == 0;

        if(rowsOdd){
            result = (row-1)/4;
            offset = offsetOdd;
        }
        else{
            result = (row-3)/4;
            offset = offsetEven;
        }

        resultXOffset = offset + (float) (stage.getWidth() * 0.223)*result;

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
