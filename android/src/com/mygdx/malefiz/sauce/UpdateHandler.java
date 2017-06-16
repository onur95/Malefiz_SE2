package com.mygdx.malefiz.sauce;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.malefiz.sauce.coordinates.Coordinates;
import com.mygdx.malefiz.sauce.dice.Dice;
import com.mygdx.malefiz.sauce.field.Field;
import com.mygdx.malefiz.sauce.networking.BoardUpdate;
import com.mygdx.malefiz.sauce.networking.GameClient;
import com.mygdx.malefiz.sauce.playboard.Board;
import com.mygdx.malefiz.sauce.playboard.BoardToPlayboard;
import com.mygdx.malefiz.sauce.sound.SoundManager;
import com.mygdx.malefiz.sauce.sound.Sounds;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sebit on 24.05.2017.
 */

public class UpdateHandler {
    private GameClient client;
    private List<BoardUpdate> update;
    private BoardToPlayboard view;
    private Board board;
    private Dice dice;
    private int playerCount;
    private SoundManager soundManager;
    private static final Logger LOGGER = Logger.getLogger( UpdateHandler.class.getName() );

    public UpdateHandler(GameClient client, Dice dice, int playerCount, SoundManager soundManager, BoardToPlayboard view, Board board){
        client.setHandler(this);
        this.client = client;
        this.dice = dice;
        this.update = new ArrayList<>(3);
        this.playerCount = playerCount;
        this.soundManager = soundManager;
        this.view = view;
        this.board = board;
    }

    public void sendMessage(int playerTurn){
        dice.setRenderRunning(false);
        dice.setPlayerSet(false);
        client.sendData(update,playerTurn);
    }

    public int getPlayerCount(){
        return playerCount;
    }

    public void add(BoardUpdate item){
        update.add(item);
    }

    public void updatePlayboard(List<BoardUpdate> update, int playerTurn){
        if(update == null){
            LOGGER.log(Level.SEVERE, "Client: Error: update == null");
            return;
        }
        this.update.clear();
        Field[][] array =  board.getBoardArray();
        Stage stage = view.getStage();



        if(update.size() > 2){
            BoardUpdate move1 = update.get(0);
            BoardUpdate move2 = update.get(1);
            BoardUpdate move3 = update.get(2);


            boolean ownPlayerKicked = array[move2.getColumn()][move2.getRow()].getFieldState().ordinal() == client.getPlayerNumber();

            //Board anpassen
            Field temp = array[move3.getColumn()][move3.getRow()];
            array[move3.getColumn()][move3.getRow()] = array[move2.getColumn()][move2.getRow()];
            if(move3.getRow() == move1.getRow() && move3.getColumn() == move1.getColumn()) {
                array[move2.getColumn()][move2.getRow()] = temp;
            }
            else{
                array[move2.getColumn()][move2.getRow()] = array[move1.getColumn()][move1.getRow()];
                array[move1.getColumn()][move1.getRow()] = move1.getColumn() <= 2 ? new Field('.') : new Field('o');
            }




            //View anpassen
            final Actor actor1 = stage.getActors().get(move1.getActorIndex());
            final Actor actor2 = stage.getActors().get(move2.getActorIndex());
            Coordinates coordinates3 = view.getHelper().getCoordinatesOfField(move3.getColumn(), move3.getRow()); //Gibt die richtigen X und Y Koordinaten zurück

            //move gekicktes Element
            MoveToAction moveAction1 = new MoveToAction(); //falls !ownPlayerKicked, dann ist das ein anderer Spieler oder ein Block, sonst ist es das Highlight des Spielers
            moveAction1.setPosition(coordinates3.getxOffset(), coordinates3.getyOffset());
            moveAction1.setDuration(1F);
            // actor2 mit moveAction1

            //move
            MoveToAction moveAction2 = new MoveToAction();
            moveAction2.setPosition(actor2.getX(), actor2.getY());
            moveAction2.setDuration(1F);
            // actor1 mit moveAction2

            if(ownPlayerKicked) {
                Actor actorPlayer = stage.getActors().get(move2.getActorIndex()+1); //ist der Kegel, +1 das Highlight des Kegels
                MoveToAction moveAction3 = new MoveToAction();
                moveAction3.setPosition(coordinates3.getxOffset(), coordinates3.getyOffset());
                moveAction3.setDuration(1F);
                actorPlayer.addAction(moveAction3);

                view.adjustPlayerClickListener(move3.getColumn(), move3.getRow(),move2.getActorIndex());
                view.adjustPlayerClickListener(move3.getColumn(), move3.getRow(),move2.getActorIndex()+1);
            }

            move(actor1,actor2, moveAction1, moveAction2, board.isPlayer(move2.getColumn(),move2.getRow()));


            //Aufpassen wenn gekickter Kegel der eigene ist (Highlight auch verschieben)
            //nicht jeder ActorIndex muss existieren! NO_FIELD zum Beispiel besitzt keinen ActorIndex
            //wenn drei beweget werden gibt es zwei ActorIndizes


        }
        else if(update.size() ==2){
            BoardUpdate move1 = update.get(0);
            BoardUpdate move2 = update.get(1);

            //Board anpassen
            array[move2.getColumn()][move2.getRow()] = array[move1.getColumn()][move1.getRow()];
            array[move1.getColumn()][move1.getRow()] = move1.getColumn() <= 2 ? new Field('.') : new Field('o');

            //View anpassen
            Actor actor1 = stage.getActors().get(move1.getActorIndex());
            Coordinates coordinates2 = view.getHelper().getCoordinatesOfField(move2.getColumn(), move2.getRow());

            MoveToAction moveAction1 = new MoveToAction();
            moveAction1.setPosition(coordinates2.getxOffset(), coordinates2.getyOffset());
            moveAction1.setDuration(1F);
            actor1.addAction(moveAction1);

            soundManager.playSound(Sounds.MOVE);

            view.setWinningLosingScreen(move2.getColumn(),move2.getRow(),false);
        }

        if(playerTurn == client.getPlayerNumber()){
            Label yourTurn=stage.getRoot().findActor("yourTurn");
            yourTurn.addAction(Actions.sequence(Actions.visible(true),Actions.delay(2f),Actions.visible(false)));
            soundManager.playSound(Sounds.PLAYERTURN);
            dice.setShaked(false);
        }
        LOGGER.log(Level.INFO, "Client: Message handled");
    }

    private void move(Actor actor1,final Actor actor2, final MoveToAction moveAction1, MoveToAction moveAction2, final boolean playerKicked){
        Action switchScreenAction = new Action(){
            @Override
            public boolean act(float delta){
                playSecondSound(actor2, moveAction1, playerKicked);
                return true;
            }
        };
        actor1.addAction(Actions.sequence(moveAction2, switchScreenAction)); //gekicktes Element
        soundManager.playSound(Sounds.MOVE);
    }

    private void playSecondSound(Actor actor, MoveToAction moveToAction, boolean playerKicked){
        actor.addAction(moveToAction);
        if(playerKicked){
            soundManager.playSound(Sounds.PLAYERKICKED);
        }
        else{
            soundManager.playSound(Sounds.BLOCKPLACED);
        }
    }

    public void playerDisconnected(int player){
        view.setPlayerVisibility(player, false);
        board.removePlayer(player);
    }

    public GameClient getClient() {
        return client;
    }
}
