package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.malefiz.GNwKryo.GameClient;
import com.mygdx.malefiz.Screens.GameMenu;

public class MyMalefizGame implements Screen, GestureDetector.GestureListener {

	private Stage stage;
	private Dice dice;
	private Texture txt_playground;
	private Image img_playground;
	private final Malefiz mal;
	private GameClient client;


	public MyMalefizGame (final Malefiz mal, GameClient client, int playerCount) {
		this.client = client;
		this.mal=mal;
		dice = new Dice(client.getPlayerNumber() != 1); //Spieler 1 fängt an; Würfel wird aktiviert
		Board board = new Board();
		UpdateHandler handler = new UpdateHandler(client, dice, playerCount);
		BoardToPlayboard view= new BoardToPlayboard();
		Player player = new Player(client.getPlayerNumber());
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		stage=new Stage(new FitViewport(1500, 1500));
		GameMenu menu = new GameMenu(stage, client.getServerIp());
		im.addProcessor(gd);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		txt_playground = new Texture("Playboard.jpg");
		img_playground=new Image(txt_playground);
		stage.addActor(img_playground);
		board.init(player, view);

		handler.setBoardAndView(view,board);


		view.init(handler, player, stage, board, dice);
		dice.setView(view);
		dice.setDiceAnimation();

		// Disable Menu/Exit-Buttons via Commenting here
		stage.addActor(menu.createExit());
		stage.addActor(menu.createMenu());
//		dice.randomNumber();
//		pfad=dice.getResult(dice.getResultNumber());
//		DiceAnimation diceAnimation = new DiceAnimation();
//		diceAnimation.create(pfad);
//		dice.setDiceAnimation(diceAnimation);


	}

	public Stage getStage(){
		return this.stage;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//stage.addActor(img_playground);
		stage.act();
		stage.draw();
		//BoardToPlayboard.generate();
		float xGrav=Gdx.input.getAccelerometerX();
		float yGrav=Gdx.input.getAccelerometerY();
		float zGrav=Gdx.input.getAccelerometerZ();
		//gForce will be close to 10 when there is no movement.
		float gForce=(float)Math.sqrt((xGrav*xGrav)+(yGrav*yGrav)+(zGrav*zGrav));
		if(gForce>11.0)
			dice.shake();
	}

	@Override
	public void resize (int width, int height) {
		// true - center camera.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		stage.dispose();
		client.terminate();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) { return false; }

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	/*
	initialDistance - distance between fingers when the gesture started
	distance - current distance between fingers
	 */
	@Override
	public boolean zoom(float initialDistance, float distance) {

		if(initialDistance>=distance && ((OrthographicCamera)stage.getCamera()).zoom!=1.0f){ //zoom out
			((OrthographicCamera)stage.getCamera()).zoom += 0.005f;

		}
		else if(initialDistance<=distance && ((OrthographicCamera)stage.getCamera()).zoom!=0.93500006f){ //zoom in
			((OrthographicCamera) stage.getCamera()).zoom -= 0.005f;

		}
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}
}
