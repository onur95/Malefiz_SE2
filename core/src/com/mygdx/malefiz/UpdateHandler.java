package com.mygdx.malefiz;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.malefiz.coordinates.Coordinates;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.field.Field;
import com.mygdx.malefiz.networking.BoardUpdate;
import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;
import com.mygdx.malefiz.sound.SoundManager;
import com.mygdx.malefiz.sound.Sounds;

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

        client.sendData(update,playerTurn, view.getCheatEnabled() || dice.getCheatEnabled());
        view.setCheatEnabled(false);
        dice.setCheatEnabled(false);
    }

    public int getPlayerCount(){
        return playerCount;
    }

    public void add(BoardUpdate item){
        update.add(item);
    }

    public void updatePlayboard(List<BoardUpdate> update, int playerTurn, int playerBefore, boolean cheated){
        if(update == null){
            LOGGER.log(Level.SEVERE, "Client: Error: update == null");
            return;
        }
        this.update.clear();
        float delay = 2F;
        Field[][] array =  board.getBoardArray();
        Stage stage = view.getStage();
        boolean gameFinished = false;

        if(update.size() > 2){
            delay = 4F;
            BoardUpdate move1 = update.get(0);
            BoardUpdate move2 = update.get(1);
            BoardUpdate move3 = update.get(2);


            boolean ownPlayerKicked = array[move2.getColumn()][move2.getRow()].getFieldState().ordinal() == client.getPlayerNumber();
            boolean isPlayer = board.isPlayer(move2.getColumn(),move2.getRow());
            adjustBoard(array, move1, move2, move3);

            //View anpassen

            final Actor actor1 = stage.getActors().get(move1.getActorIndex());
            final Actor actor2 = stage.getActors().get(move2.getActorIndex());

            Coordinates coordinates3 = view.getHelper().getCoordinatesOfField(move3.getColumn(), move3.getRow()); //Gibt die richtigen X und Y Koordinaten zurück
            MoveToAction moveAction1 = getFirstMove(coordinates3);
            // actor2 mit moveAction1

            //move
            MoveToAction moveAction2 = getSecondMove(actor2);
            // actor1 mit moveAction2

            adjustOwnPlayerKicked(ownPlayerKicked, move2, move3, coordinates3);
            move(actor1,actor2, moveAction1, moveAction2, isPlayer);

            //Aufpassen wenn gekickter Kegel der eigene ist (Highlight auch verschieben)
            //nicht jeder ActorIndex muss existieren! NO_FIELD zum Beispiel besitzt keinen ActorIndex
            //wenn drei beweget werden gibt es zwei ActorIndizes
        }
        else if(update.size() ==2){
            BoardUpdate move1 = update.get(0);
            BoardUpdate move2 = update.get(1);

            //Board anpassen
            adjustBoard(array,move1,move2);

            //View anpassen
            Actor actor1 = stage.getActors().get(move1.getActorIndex());
            Coordinates coordinates2 = view.getHelper().getCoordinatesOfField(move2.getColumn(), move2.getRow());

            actor1.addAction(getFirstMove(coordinates2));

            soundManager.playSound(Sounds.MOVE);

            gameFinished = view.setWinningLosingScreen(move2.getColumn(),move2.getRow(),false);
        }
        if(!gameFinished) {
            displayYourTurn(playerTurn, delay);
            displayCheat(cheated, playerBefore);
        }
        LOGGER.log(Level.INFO, "Client: Message handled");
    }

    private void displayCheat(boolean cheated, int playerBefore){
        if(cheated){
            LOGGER.log(Level.INFO, "updatePlayboard: Cheating deteced.");
            Label cm=view.getStage().getRoot().findActor("cm");
            cm.setText("Player "+playerBefore+" cheated!");
            cm.addAction(Actions.sequence(Actions.visible(true),Actions.delay(2f),Actions.visible(false)));
        }
    }

    private void adjustOwnPlayerKicked(boolean kicked, BoardUpdate move2, BoardUpdate move3, Coordinates coordinates3){
        if(kicked) {
            Actor actorPlayer = view.getStage().getActors().get(move2.getActorIndex() + 1); //ist der Kegel, +1 das Highlight des Kegels
            MoveToAction moveAction3 = getFirstMove(coordinates3);
            actorPlayer.addAction(moveAction3);

            view.adjustPlayerClickListener(move3.getColumn(), move3.getRow(), move2.getActorIndex());
            view.adjustPlayerClickListener(move3.getColumn(), move3.getRow(), move2.getActorIndex() + 1);
        }
    }

    private void displayYourTurn(int playerTurn, float delay){
        if(playerTurn == client.getPlayerNumber()){
            Action yourTurnAction = new Action(){
                @Override
                public boolean act(float delta){
                    yourTurn();
                    return true;
                }
            };
            Label yourTurn=view.getStage().getRoot().findActor("yourTurn");
            yourTurn.addAction(Actions.sequence(Actions.delay(delay),Actions.visible(true), yourTurnAction,Actions.delay(2f),Actions.visible(false)));
        }
    }

    private void yourTurn(){
        soundManager.playSound(Sounds.PLAYERTURN);
        dice.setShaked(false);
    }

    private MoveToAction getFirstMove(Coordinates coordinates3){
        //move gekicktes Element
        MoveToAction moveAction1 = new MoveToAction(); //falls !ownPlayerKicked, dann ist das ein anderer Spieler oder ein Block, sonst ist es das Highlight des Spielers
        moveAction1.setPosition(coordinates3.getxOffset(), coordinates3.getyOffset());
        moveAction1.setDuration(1F);
        return moveAction1;
    }

    private MoveToAction getSecondMove(Actor actor2){
        MoveToAction moveAction2 = new MoveToAction();
        moveAction2.setPosition(actor2.getX(), actor2.getY());
        moveAction2.setDuration(1F);
        return moveAction2;
    }

    private void adjustBoard(Field[][] array, BoardUpdate move1, BoardUpdate move2){
        array[move2.getColumn()][move2.getRow()] = array[move1.getColumn()][move1.getRow()];
        array[move1.getColumn()][move1.getRow()] = move1.getColumn() <= 2 ? new Field('.') : new Field('o');
    }

    private void adjustBoard(Field[][] array, BoardUpdate move1, BoardUpdate move2, BoardUpdate move3){
        Field temp = array[move3.getColumn()][move3.getRow()];
        array[move3.getColumn()][move3.getRow()] = array[move2.getColumn()][move2.getRow()];
        if(move3.getRow() == move1.getRow() && move3.getColumn() == move1.getColumn()) {
            array[move2.getColumn()][move2.getRow()] = temp;
        }
        else{
            array[move2.getColumn()][move2.getRow()] = array[move1.getColumn()][move1.getRow()];
            array[move1.getColumn()][move1.getRow()] = move1.getColumn() <= 2 ? new Field('.') : new Field('o');
        }
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
