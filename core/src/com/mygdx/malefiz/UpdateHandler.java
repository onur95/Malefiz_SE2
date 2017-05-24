package com.mygdx.malefiz;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.malefiz.GNwKryo.GameClient;
import com.mygdx.malefiz.GNwKryo.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebit on 24.05.2017.
 */

public class UpdateHandler {
    private GameClient client;
    private List<BoardUpdate> update;
    private BoardToPlayboard view;
    private Board board;
    private Dice dice;

    public UpdateHandler(GameClient client, Dice dice){
        client.setHandler(this);
        this.client = client;
        this.dice = dice;
        update = new ArrayList<BoardUpdate>(3);
    }

    public void sendMessage(int playerTurn){
        client.sendData(update,playerTurn);
    }

    public int getPlayerCount(){
        return client.getPlayerCount();
    }

    public void setBoardAndView(BoardToPlayboard view, Board board){
        this.view = view;
        this.board = board;
    }

    public void add(BoardUpdate item){
        update.add(item);
    }

    public void updatePlayboard(List<BoardUpdate> update, int playerTurn){
        update.clear();
        Field[][] array =  board.getBoardArray();
        Stage stage = view.getStage();



        if(update.size() > 2){
            BoardUpdate move1 = update.get(1);
            BoardUpdate move2 = update.get(2);
            BoardUpdate move3 = update.get(0);

            boolean ownPlayerKicked = array[move2.getColumn()][move2.getRow()].getField_state().ordinal() == client.getPlayerNumber();

            //Board anpassen
            array[move2.getColumn()][move2.getRow()] = array[move1.getColumn()][move1.getRow()];
            array[move1.getColumn()][move1.getRow()] = array[move3.getColumn()][move3.getRow()];
            array[move3.getColumn()][move3.getRow()] = new Field('.');


            //View anpassen
            Actor actor1 = stage.getActors().get(move3.getActorIndex());
            Actor actor2 = stage.getActors().get(move2.getActorIndex());
            Coordinates coordinates2 = new Coordinates(actor2.getX(), actor2.getY());
            Coordinates coordinates3 = view.setHighlight(move1.getColumn(), move1.getRow(), true); //Gibt die richtigen X und Y Koordinaten zurück

            MoveToAction moveAction1 = new MoveToAction();
            moveAction1.setPosition(coordinates3.getxOffset(), coordinates3.getyOffset());
            actor2.addAction(moveAction1);

            MoveToAction moveAction2 = new MoveToAction(); //falls !ownPlayerKicked, dann ist das ein anderer Spieler oder ein Block, sonst ist es das Highlight des Spielers
            moveAction1.setPosition(coordinates2.getxOffset(), coordinates2.getyOffset());
            actor1.addAction(moveAction2);

            if(ownPlayerKicked) {
                Actor actorPlayer = stage.getActors().get(move3.getActorIndex()-1); //-1 ist der Kegel, sonst das Highlight des Kegels
                MoveToAction moveAction3 = new MoveToAction();
                moveAction1.setPosition(coordinates2.getxOffset(), coordinates2.getyOffset());
                actorPlayer.addAction(moveAction3);

                view.adjustPlayerClickListener(move2.getColumn(), move2.getRow(),move3.getActorIndex());
                view.adjustPlayerClickListener(move2.getColumn(), move2.getRow(),move3.getActorIndex()-1);
            }



            //Aufpassen wenn gekickter Kegel der eigene ist (Highlight auch verschieben)
            //nicht jeder ActorIndex muss existieren! NO_FIELD zum Beispiel besitzt keinen ActorIndex
            //wenn drei beweget werden gibt es zwei ActorIndizes


        }
        else{
            BoardUpdate move1 = update.get(0);
            BoardUpdate move2 = update.get(1);

            //Board anpassen
            array[move2.getColumn()][move2.getRow()] = array[move1.getColumn()][move1.getRow()];
            array[move1.getColumn()][move1.getRow()] = new Field('.');

            //View anpassen
            Actor actor1 = stage.getActors().get(move1.getActorIndex());
            Actor actor2 = stage.getActors().get(move2.getActorIndex());
            Coordinates coordinates2 = new Coordinates(actor2.getX(), actor2.getY());

            MoveToAction moveAction1 = new MoveToAction();
            moveAction1.setPosition(coordinates2.getxOffset(), coordinates2.getyOffset());
            actor1.addAction(moveAction1);
        }

        if(playerTurn == client.getPlayerNumber()){
            dice.setShaked(false);
        }
    }

}
