package com.mygdx.malefiz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Klaus on 02.04.2017.
 */

public class BoardToPlayboard {
    private Image player1;
    private Image player2;
    private Image player3;
    private Image player4;
    private Image block;
    private Image highlight;
    private Image playerHighlight;
    private Stage stage;
    private int actorActive;
    private int actorsCount = -1;
    private int kickedIndex = -1;
    private int playerCount;
    private UpdateHandler handler;
    private Player player;
    private Board board;
    private Dice dice;
    private List<List<Integer>> players;
    private CoordinateCalculation helper;
    private SoundManager soundManager;
    private List<Integer> playerMovesPossible;
    private static final Logger LOGGER = Logger.getLogger( BoardToPlayboard.class.getName() );


    private void initPlayers(){
        players = new ArrayList<>();
        for(int i = 0; i<playerCount; i++){
            List list = new ArrayList<>(5);
            list.add(0);
            players.add(list);
        }
    }

    public  void init(UpdateHandler handler, Player player, Stage stage, Board board, Dice dice, SoundManager soundManager){
        this.player = player;
        this.board = board;
        this.handler = handler;
        this.dice = dice;
        this.stage = stage;
        this.soundManager = soundManager;
        playerCount = handler.getPlayerCount();
        helper = new CoordinateCalculation(this.stage);
        playerMovesPossible = new ArrayList<>();
        initPlayers();
        setImages();
        generate();
    }

    public void setImages(){
        player1=new Image(new Texture("Player1.png"));
        player2=new Image(new Texture("Player2.png"));
        player3=new Image(new Texture("Player3.png"));
        player4=new Image(new Texture("Player4.png"));
        highlight=new Image(new Texture("Highlight.png"));
        playerHighlight=new Image(new Texture("Highlight_2.png"));
        block=new Image(new Texture("Block.png"));
    }

    public void generate(){
        for(int column = 0; column < board.getBoardArray().length; column++) {
            for (int row = 0; row < board.getBoardArray()[column].length; row++) {
                Coordinates coordinates = helper.getCoordinatesOfField(column, row);
                adjustView(coordinates, column, row);
            }
        }

        stage.act();
        stage.draw();
    }

    /**
     * Generierung eines Feldes auf/in der Stage
     * @param coordinates Koordinaten, wo sich dieses Feld auf/in der Stage befindet
     * @param column Column und row geben an, wo sich dieses Feld im boardArray befindet
     * @param row
     */
    private void adjustView(Coordinates coordinates, int column, int row){
        Image field = getFieldType(column, row);
        if (field != null && checkPlayerCount(column, row)) {
            MoveToAction action = new MoveToAction();
            action.setPosition(coordinates.getxOffset(), coordinates.getyOffset());

            field.addAction(action);
            stage.addActor(field);
            setPlayerHighlight(column, row, coordinates);
        }
    }


    /**
     * Es werden nur die Spieler generiert, die auch benötigt werden. Also bei zwei Spielern, wird Spieler 3 und 4 nicht generiert
     * @param column
     * @param row
     * @return
     */
    private  boolean checkPlayerCount(int column, int row){
        return !board.isPlayer(column, row) || (board.getBoardArray()[column][row].getFieldState().ordinal() <= playerCount && board.isPlayer(column, row));
    }


    /**
     * Setzen eines Highlights, auf dessen Position ein Kegel oder ein Block gesetzt werden kann
     * @param column Column und Row geben an, wo sich dieses befindet
     * @param row
     */
    public void setHighlight(int column, int row){
        Coordinates coordinates = helper.getCoordinatesOfField(column, row);
        MoveToAction action = new MoveToAction();
        action.setPosition(coordinates.getxOffset(), coordinates.getyOffset());
        Image field = new Image(highlight.getDrawable());
        field.addAction(action);
        field.addListener(new HighlightClickListener(column, row, stage.getActors().size, board, this, handler));
        stage.addActor(field);
    }


    /**
     * Setzen der Highlights aller Spieler (gebraucht werden zwar nur die Highlights der eigenen Kegel, aber um unterschiedliche Indizes in stage.getActors() zu verhindern, werden für alle Spieler Highlights generiert)
     * @param column Column und row geben an, was für ein Feld überprüft werden soll, ob es ein Player ist
     * @param row
     * @param coordinates Die Koordinaten geben an, wo das Highlight generiert werden soll (Hier die Koordinaten des jeweiligen Kegels)
     */
    private  void setPlayerHighlight(int column, int row, Coordinates coordinates){
        if(board.isPlayer(column, row)) {
            MoveToAction action = new MoveToAction();
            action.setPosition(coordinates.getxOffset(), coordinates.getyOffset());
            Image field = new Image(playerHighlight.getDrawable());
            field.addAction(action);
            field.setVisible(false);

            if (board.getBoardArray()[column][row].getFieldState().ordinal() == player.getNumber()) {
                field.addListener(new PlayerClickListener(column, row, stage.getActors().size, player, board, this, dice));
                player.addHighlightFigure(stage.getActors().size);
            }
            players.get(board.getBoardArray()[column][row].getFieldState().ordinal() - 1).add(stage.getActors().size - 1);
            stage.addActor(field);
        }
    }

    /**
     * Bestimmen des Feldes, das generiert werden soll
     * @param column Column und row bestimmen ein Feld im zweidimensionalen Array (boardArray). Zurückgegeben wird die Klasse Field, anhand der man bestimmen kann, was generiert wird (Player, Block, etc.)
     * @param row
     * @return Das Feld wird als Image zurückgegeben
     */
    private  Image getFieldType(int column, int row){
        Image field;
        switch (board.getBoardArray()[column][row].getFieldState()) {
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

            default:
                field = null;
                break;
        }

        System.out.println(board.getBoardArray()[column][row].getFieldState().toString());

        if(field != null && board.getBoardArray()[column][row].getFieldState().ordinal() == player.getNumber()){
            //Falls es eine Spielfigur des ausgewählten Spielers ist, wird der Figur ein Clicklistener angehängt
            //Dieser ist dafür da um das Highlight der gerade ausgewählten Figur auf eine andere zu ändern
            field.addListener(new PlayerClickListener(column, row, stage.getActors().size+1, player, board, this, dice)); //Weil es muss ja auf das Highlight referenziert werden, das genau 1 darüber liegt
            player.addFigurePosition(column, row);
        }
        return field;
    }

    /**
     * Anzeigen aller Highlights eines Spielers
     * @param status Wert, ob Highlight angezeigt oder versteckt wird
     */
    public  void setPlayerFiguresHighlighted(boolean status){
        if(status && !isMovePossible()){
            LOGGER.log(Level.FINE, "Client: No move possible");
            handler.sendMessage(player.getNumber());
            return;
        }
        else if(status){
            for (int i = 0; i< player.getHighlightedFiguresIndizes().size(); i++) {
                if(playerMovesPossible.contains(i)) {
                    setPlayerFigureHighlighted(player.getHighlightedFiguresIndizes().get(i), status);
                }
            }
        }
        else {
            for (int index : player.getHighlightedFiguresIndizes()) {
                setPlayerFigureHighlighted(index, status);
            }
        }
    }

    private boolean isMovePossible(){
        playerMovesPossible = new ArrayList<>();
        for(int i =0; i< player.getFiguresPosition().size(); i++) {
            board.setFieldActive(player.getFiguresPosition().get(i).getColumn(), player.getFiguresPosition().get(i).getRow());
            boolean movePossible = board.higlightPositionsMovement(dice.getResultNumber(), board.getRealFieldActive(), null, false);
            if(movePossible){
                playerMovesPossible.add(i);
            }
        }
        return !playerMovesPossible.isEmpty();
    }



    /**
     * Anzeigen des Highlights eines Kegels
     * @param index ActorIndex des Highlightes eines Spielers
     * @param status Wert, ob das Highlight nun angezeigt oder versteckt wird
     */
    public  void setPlayerFigureHighlighted(int index, boolean status){
        stage.getActors().get(index).setVisible(status);
    }

    /**
     * Hier wird die View and den getätigten Move angepasst
     * @param actorIndex Index des Actors bzw. Highlights zu dem gegangen wird. Bewegt wird normalerweise der Actor mit dem Index, der in actorActive steht (bzw. actorActive-1, wegen Highlight UND Spieler)
     * @param blockIsMoving Falls ein Block bewegt wird (==true) dann wird der Actor mit dem Index kickedIndex zum Actor mit dem Index actorIndex bewegt
     * @param column Column und row wird gebraucht, damit man die Werte im PlayerClickListener (column, row) aktualisiert werden. (Player1 ist jetzt auf einem anderen Feld; column und row wird dementsprechend angepasst)
     * @param row
     */
    public   void moveToPosition(int actorIndex, boolean blockIsMoving, int column, int row){
        if(actorActive != -1 && !blockIsMoving) {
            soundManager.playSound(Sounds.MOVE);

            //Highlight wieder verschwinden lassen
            setPlayerFiguresHighlighted(false);

            MoveToAction action = getMoveToAction(actorIndex, 1F);
            MoveToAction action2 = getMoveToAction(actorIndex, 0);
            stage.getActors().get(actorActive).addAction(action2);
            stage.getActors().get(actorActive - 1).addAction(action);

            adjustPlayerClickListener(column, row, actorActive);
            adjustPlayerClickListener(column, row, actorActive-1);
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
                adjustPlayers(((PlayerClickListener)event).getColumn(), ((PlayerClickListener)event).getRow(), column, row);
                ((PlayerClickListener)event).setColumn(column);
                ((PlayerClickListener)event).setRow(row);
            }
        }
    }

    private void adjustPlayers(int columnOld, int rowOld, int columnNew, int rowNew){
        for(FieldPosition fieldPosition : player.getFiguresPosition()){
            if(fieldPosition.getColumn() == columnOld && fieldPosition.getRow() == rowOld){
                fieldPosition.setColumn(columnNew);
                fieldPosition.setRow(rowNew);
            }
        }
    }

    /**
     * Die Highlights bzw. berechneten Positionen, die gesetzt werden, wenn man den Zug mit dem ausgewählten Kegel tätigen will, werden hier wieder alle gelöscht
     * Das Löschen orientiert sich nach dem gespeicherten Wert in actorsCount. actorsCount beinhaltet die Anzahl der Actoren, bevor die Highlights gesetzt wurden
     * also stage.getActors() <=> List<Actor>
     * und davon die Länge bzw. Anzahl: stage.getActors().size()
     * nun werden alle Actoren über dieser Größe gelöscht
     * die Highlights werden nacheinander gelöscht, bis die Anzahl der Actoren wieder passt
     */
    public  void removeHighlights(){
        while(stage.getActors().size>actorsCount && actorsCount != -1){
            stage.getActors().get(actorsCount).remove();
        }
        actorsCount = -1;
    }

    /**
     * Bewegung zum Feld wird vorbereitet
     * @param actorIndex Die Bewegung geht vom Startpunkt (Player1, etc.) aus zu den Koordinaten von dem Highlight, auf das geklickt wurde. Anhand des ActorIndexes des Highlights bekommt man den Actor mit den Koordinaten
     * @param duration Dauer der Bewegung vom Start bis zum Endpunkt
     * @return Zurückgeben der vorbereiteten MoveToAction
     */
    private  MoveToAction getMoveToAction(int actorIndex, float duration){
        MoveToAction action = new MoveToAction();
        action.setPosition(stage.getActors().get(actorIndex).getX(), stage.getActors().get(actorIndex).getY());
        action.setDuration(duration);
        return action;
    }

    /**
     *
     * @param index Index des Actors in stage.getActors() der sich in Zukunft bewegen wird. Zum Beispiel wenn auf den eigenen Kegel geklickt wurde, um ihn auf ein gehighlightetes Feld zu bewegen
     */
    public  void setActorActive(int index){
        actorActive = index;
    }

    public  void setActorsCount(){
        if(actorsCount == -1) {
            actorsCount = stage.getActors().size;
        }
    }

    /**
     * Setzen ActorIndexes des Blockes/Spielers, der gekickt wurde
     * @param index ActorIndex des Highlightes. Um die Figur darunter (Block oder Player) zu ermitteln, holt man dessen Koordinaten und vergleicht sie mit den Actoren in stage.getActors()
     * @param isVisible Falls ein Block gekickt wurde, wird er, bis er erneut platziert wird auf visible false gesetzt
     */
    public  void setKickedIndex(int index, boolean isVisible){
        float x = stage.getActors().get(index).getX();
        float y = stage.getActors().get(index).getY();
        int kicked = 0;
        float epsilon = 0.0000001F;
        for(Actor actor : stage.getActors()){
            if(Math.abs(actor.getX() - x) < epsilon && Math.abs(actor.getY() - y) < epsilon){
                kickedIndex = kicked;
                break;
            }
            kicked++;
        }
        setKickedVisibility(isVisible);
    }

    /**
     * Player wird gekickt und somit wieder in seinen Startbereich gesetzt. Die column und row, wo dieser Startbereich liegt, wurden zuvor in der Funktion movePlayerToStart von Board gesetzt
     * @return Die Koordinaten des Startbereiches, in dem der Kegel nun platziert wurde, werden zurückgegeben
     */
    public  FieldPosition moveKicked(){
        FieldPosition fieldPosition = null;
        if(kickedIndex != -1){
            int column = board.getNewPlayerPosition().getColumn();
            int row = board.getNewPlayerPosition().getRow();
            fieldPosition = new FieldPosition(column, row);
            Coordinates coordinates = helper.getCoordinatesOfField(column, row);

            MoveToAction action = new MoveToAction();
            action.setPosition(coordinates.getxOffset(),coordinates.getyOffset());
            action.setDuration(1F);
            stage.getActors().get(kickedIndex).addAction(action);
            kickedIndex = -1;
        }
        return fieldPosition;
    }

    /**
     * Der Block, der neu positioniert wird, wird nach seiner Bewegung, zu den entsprechenden Koordinaten, wieder auf visible true gesetzt
     */
    public void setKickedVisibility(boolean status){
        stage.getActors().get(kickedIndex).setVisible(status);
    }

    public  int getKickedIndex(){
        return kickedIndex;
    }

    public  int getActorActive(){
        return actorActive;
    }

    /**
     * Überprüft, ob der Spieler mit seinem Zug fertig ist und sendet falls dies zutrifft das Update an den Server .
     */
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

    /**
     * Seten der Sichtbarkeit alles Spieler (nötig, falls ein Spieler während des Spiels aufhört)
     * @param player PlayerNummer, welcher Spieler aufgehört hat
     * @param status Status, ob dieser Spieler anzegeigt oder versteckt wird
     */
    public void setPlayerVisibility(int player, boolean status){
        for(int index : players.get(player-1)){
            stage.getActors().get(index).setVisible(status);
        }
    }

    public CoordinateCalculation getHelper(){
        return this.helper;
    }

    public Stage getStage(){
        return this.stage;
    }

    public List<Integer> getPlayerMovesPossible() {
        return playerMovesPossible;
    }
}
