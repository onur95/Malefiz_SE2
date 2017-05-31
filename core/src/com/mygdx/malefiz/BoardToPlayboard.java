package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klaus on 02.04.2017.
 */

public class BoardToPlayboard {
    private Field[][] board;
    private final float xOffset = 0.02133333333333F; //in Prozent vom Spielfeld
    private Image player1;
    private Image player2;
    private Image player3;
    private Image player4;
    private Image block;
    private Image highlight;
    private Image playerHighlight;
    private Stage stage;
    private float lineOffset;
    private float pointOffset;
    private int actorActive;
    private Sound yourTurn;
    private Sound kickPlayer;
    private Sound kickedPlayerMove;
    private Sound placeBlock;
    private Sound kickedOwnFigure;
    private Sound moveFigure;
    private int actorsCount = -1;
    private int kickedIndex = -1;
    private int activePlayer = 1;
    private int playerCount;
    private UpdateHandler handler;
    private Player player;
    private Board board_main;
    private Dice dice;
    private List<List<Integer>> players;



    private  void init_sound(){
        yourTurn=Gdx.audio.newSound(Gdx.files.internal("soundeffects/your-turn.wav"));
        kickPlayer = Gdx.audio.newSound(Gdx.files.internal("soundeffects/kick-player.wav"));
        kickedPlayerMove = Gdx.audio.newSound(Gdx.files.internal("soundeffects/kicked-player-move-back.wav"));
        placeBlock = Gdx.audio.newSound(Gdx.files.internal("soundeffects/place-block.wav"));
        kickedOwnFigure = Gdx.audio.newSound(Gdx.files.internal("soundeffects/own-figure-kicked.wav"));
        moveFigure = Gdx.audio.newSound(Gdx.files.internal("soundeffects/move-figure2.wav"));
    }

    private void init_players(){
        players = new ArrayList<List<Integer>>();
        System.out.println(playerCount);
        for(int i = 0; i<playerCount; i++){
            List list = new ArrayList<Integer>();
            list.add(0);
            players.add(list);
        }
    }

    public  void init(UpdateHandler handler, Player player, Stage stage, Board board, Dice dice){
        this.player = player;
        this.board_main = board;
        this.handler = handler;
        this.dice = dice;
        playerCount = handler.getPlayerCount();
        init_sound();
        init_players();
        player1=new Image(new Texture("Player1.png"));
        player2=new Image(new Texture("Player2.png"));
        player3=new Image(new Texture("Player3.png"));
        player4=new Image(new Texture("Player4.png"));
        highlight=new Image(new Texture("Highlight.png"));
        playerHighlight=new Image(new Texture("Highlight_2.png"));
        block=new Image(new Texture("Block.png"));
        this.board = board_main.getBoardArray();
        this.stage = stage;
        float percentOffset =0.009333333F;
        lineOffset = stage.getWidth()*percentOffset;
        float percentPoint = 0.046666667F;
        pointOffset = lineOffset +stage.getWidth()*percentPoint;

        generate();
    }

    public void generate(){
        for(int column = 0; column < board.length; column++) {
            for (int i = 0; i < board[column].length; i++) {
                setField(column,i);
            }
        }

        stage.act();
        stage.draw();
    }

    private float setFirstFields(int column, int row, boolean onlyCalculateAndReturn){
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
            Image field = getFieldType(column, row);

            if (field != null) {
                MoveToAction action = new MoveToAction();
                if (status == 0) {
                    if(!onlyCalculateAndReturn && checkPlayerCount(column, row)) {
                        action.setPosition(tempXOffset1, yOffset);
                        field.addAction(action);
                        stage.addActor(field);

                        setPlayerHighlight(column, row, tempXOffset1, yOffset);
                    }
                    else{
                        //Return wird nur für das Highlight gebraucht
                        return tempXOffset1;
                    }
                } else { //status == 1
                    if(!onlyCalculateAndReturn && checkPlayerCount(column, row)) {
                        action.setPosition(tempXOffset2, yOffset);
                        field.addAction(action);
                        stage.addActor(field);

                        setPlayerHighlight(column, row, tempXOffset2, yOffset);
                    }
                    else{
                        //Return wird nur für das Highlight gebraucht
                        return tempXOffset2;
                    }
                }
            }
        }
        return -1;
    }

    private  boolean checkPlayerCount(int column, int row){
        return !board_main.isPlayer(column, row) || (board[column][row].getField_state().ordinal() <= playerCount && board_main.isPlayer(column, row));
    }

    private  void setField(int column, int row){
        Image field;
        //Falls die ersten zwei Reihen: anders generieren
        if(column<2){
            setFirstFields(column, row, false);
            return;
        }
        //sonst normal

        float tempXOffset = getOffsetXNormal(row);
        float yOffset = getOffsetYNormal(column);

        field = getFieldType(column, row);

        if (field != null && checkPlayerCount(column, row)) {
            MoveToAction action = new MoveToAction();
            action.setPosition(tempXOffset, yOffset);

            field.addAction(action);
            stage.addActor(field);
            setPlayerHighlight(column, row, tempXOffset, yOffset);
        }
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

    private  void setHighlight(int column, int row, float offsetX, float offsetY){
        MoveToAction action = new MoveToAction();
        action.setPosition(offsetX, offsetY);
        Image field = new Image(highlight.getDrawable());
        field.addAction(action);
        field.addListener(new HighlightClickListener(column, row, stage.getActors().size, board_main, this, handler));
        stage.addActor(field);
    }

    public  Coordinates setHighlight(int column, int row, boolean returnCoordinates){
        float yOffset;
        float tempXOffset = setFirstFields(column, row, true);
        Coordinates coordinates = new Coordinates();
        if(column == 0){
            yOffset = (float) (stage.getHeight()*0.015);
        }
        else if(column == 1){
            yOffset = (float) (stage.getHeight()*0.096);
        }
        else{
            tempXOffset = getOffsetXNormal(row);
            yOffset = getOffsetYNormal(column);
        }
        if(returnCoordinates){
            coordinates = new Coordinates(tempXOffset, yOffset);
        }
        else {
            setHighlight(column, row, tempXOffset, yOffset);
        }
        return coordinates;
    }

    private  void setPlayerHighlight(int column, int row, float offsetX, float offsetY){
//        if(board[column][row].getField_state().ordinal() == player.getNumber()) {
        if(board_main.isPlayer(column, row)) {
            MoveToAction action = new MoveToAction();
            action.setPosition(offsetX, offsetY);
            Image field = new Image(playerHighlight.getDrawable());
            field.addAction(action);
            field.setVisible(false);

            if (board[column][row].getField_state().ordinal() == player.getNumber()) {
                field.addListener(new PlayerClickListener(column, row, stage.getActors().size, player, board_main, this, dice));
                player.addHighlightFigure(stage.getActors().size);
            }
            players.get(board[column][row].getField_state().ordinal() - 1).add(stage.getActors().size - 1);
            stage.addActor(field);
        }
//        }
    }

    private  Image getFieldType(int column, int row){
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
        if(board[column][row].getField_state().ordinal() == player.getNumber()){
            //Falls es eine Spielfigur des ausgewählten Spielers ist, wird der Figur ein Clicklistener angehängt
            //Dieser ist dafür da um das Highlight der gerade ausgewählten Figur auf eine andere zu ändern
            try{
                field.addListener(new PlayerClickListener(column, row, stage.getActors().size+1, player, board_main, this, dice)); //Weil es muss ja auf das Highlight referenziert werden, das genau 1 darüber liegt
            }catch(NullPointerException e){
                Gdx.app.log("GetFieldType", e.toString());
            }
        }
        return field;
    }

    public  void setPlayerFiguresHighlighted(boolean status){
        for (int index : player.getHighlightedFiguresIndizes()) {
            setPlayerFigureHighlighted(index, status);
        }
    }

    public  void setPlayerFigureHighlighted(int index, boolean status){
        stage.getActors().get(index).setVisible(status);
    }

    private  float getOffsetXNormal(float row){
        return (stage.getWidth() * xOffset + lineOffset)+(row)*pointOffset;
    }

    private  float getOffsetYNormal(float column){
        return ((float) (0.143 * stage.getHeight()))+(column-2)*pointOffset;
    }

    public   void moveToPosition(int actorIndex, boolean blockIsMoving, int column, int row){
        if(actorActive != -1 && !blockIsMoving) {
            moveFigure.play();
            //Highlight wieder verschwinden lassen
            setPlayerFiguresHighlighted(false);

            MoveToAction action = getMoveToAction(actorIndex, 1F);
            MoveToAction action2 = getMoveToAction(actorIndex, 0);
            stage.getActors().get(actorActive).addAction(action2);
            stage.getActors().get(actorActive - 1).addAction(action);

            adjustPlayerClickListener(column, row, actorActive);
            adjustPlayerClickListener(column, row, actorActive-1);




            //Hier alle gehighlighteten Positionen löschen
            //Wenn nach der Berechnung der Route die Highlights angezeigt werden, Size von
            //stage.getActors() speichern!!
            //danach kann man alle leicht wieder löschen (alle nach index (size-1)
            //stage.getActors().get(actorIndex).remove();
        }
        else if(blockIsMoving){
            MoveToAction action2 = getMoveToAction(actorIndex, 0);
            stage.getActors().get(kickedIndex).addAction(action2);
            kickedIndex = -1;
        }
        removeHighlights();

        actorActive = -1;
    }

    public void adjustPlayerClickListener(int column, int row, int index){
        for(EventListener event : stage.getActors().get(index).getListeners()){
            if(event.getClass() == PlayerClickListener.class){
                ((PlayerClickListener)event).setColumn(column);
                ((PlayerClickListener)event).setRow(row);
            }
        }
    }

    public  void removeHighlights(){
        while(stage.getActors().size>actorsCount && actorsCount != -1){
            stage.getActors().get(actorsCount).remove();
        }
        actorsCount = -1;
    }

    private  MoveToAction getMoveToAction(int actorIndex, float duration){
        MoveToAction action = new MoveToAction();
        action.setPosition(stage.getActors().get(actorIndex).getX(), stage.getActors().get(actorIndex).getY());
        action.setDuration(duration);
        return action;
    }

    public  void setActorActive(int index){
        //Actor Active ist der Index der gerade ausgewählten Figur
        actorActive = index;
    }

    public  void playYourTurn(){
        yourTurn.play();

    }

    public  void setActorsCount(){
        if(actorsCount == -1) {
            actorsCount = stage.getActors().size;
        }
    }

    public  int getActorsCount(){
        return actorsCount;
    }

    public  void setKickedIndex(int index, boolean isVisible){
        float x = stage.getActors().get(index).getX();
        float y = stage.getActors().get(index).getY();
        int kicked = 0;
        for(Actor actor : stage.getActors()){
            if(actor.getX() == x && actor.getY() == y){
                kickedIndex = kicked;
                break;
            }
            kicked++;
        }
        stage.getActors().get(kickedIndex).setVisible(isVisible);
    }

    public  FieldPosition moveKicked(){
        FieldPosition coordinates = null;
        if(kickedIndex != -1){
            int column = board_main.getNewPlayerPosition().getColumn();
            int row = board_main.getNewPlayerPosition().getRow();
            coordinates = new FieldPosition(column, row);
            float yOffset;
            float tempXOffset = setFirstFields(column, row, true);
            if(column == 0){
                yOffset = (float) (stage.getHeight()*0.015);
            }
            else if(column == 1){
                yOffset = (float) (stage.getHeight()*0.096);
            }
            else{
                tempXOffset = getOffsetXNormal(row);
                yOffset = getOffsetYNormal(column);
            }
            MoveToAction action = new MoveToAction();
            action.setPosition(tempXOffset,yOffset);
            action.setDuration(1F);
            stage.getActors().get(kickedIndex).addAction(action);
            kickedIndex = -1;
        }
        return coordinates;
    }

    public void setKickedVisibility(){
        stage.getActors().get(kickedIndex).setVisible(true);
    }

    public  int getKickedIndex(){
        return kickedIndex;
    }

    public  int getActorActive(){
        return actorActive;
    }

    public  void checkFinished(){
        boolean status = true;
        for (int index : player.getHighlightedFiguresIndizes()) {
            if(stage.getActors().get(index).isVisible()){
                status = false;
            }
        }

        if(status && kickedIndex == -1 && actorActive == -1){
            handler.sendMessage(player.getNumber());
        }
    }

    public void setPlayerVisibility(int player, boolean status){
        for(int index : players.get(player-1)){
            stage.getActors().get(index).setVisible(status);
        }
    }

    public Stage getStage(){
        return this.stage;
    }
}
