package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.malefiz.screens.GameMenu;
import com.mygdx.malefiz.cheats.CheatEngine;
import com.mygdx.malefiz.dice.Dice;
import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.playboard.Board;
import com.mygdx.malefiz.playboard.BoardToPlayboard;
import com.mygdx.malefiz.sound.SoundManager;
import com.mygdx.malefiz.sound.Sounds;

public class MyMalefizGame implements Screen, GestureDetector.GestureListener {

	private Stage stage;
	private GameClient client;
	private OrthographicCamera camera;
	private float currentZoom;
	private Skin skin;
	private Dice dice;

	public MyMalefizGame (GameClient client, int playerCount) {
		this.client = client;
		//client besitzt das Game
		this.stage = new Stage(new FitViewport(1500, 1500));

		setSkin();
		setGestures();
		setPlayground();
		setCamera();

		SoundManager soundManager = new SoundManager();
		Player player = new Player(client.getPlayerNumber());
		BoardToPlayboard view = new BoardToPlayboard();
		Board board = new Board();




		this.dice = new Dice(getShakeStatus(soundManager), view);
		CheatEngine cheatEngine = new CheatEngine(view, board, dice);
		GameMenu menu = new GameMenu(stage, client.getServerIp(), cheatEngine);


		UpdateHandler handler = new UpdateHandler(client, dice, playerCount, soundManager, view, board);
		view.init(handler, player, stage, board, dice, soundManager);

		// Disable Menu/Exit-Buttons via Commenting here
		stage.addActor(menu.createExit());
		stage.addActor(menu.createMenu());
		setYourTurnLabel();

	}

	private void setCamera(){
		camera=(OrthographicCamera)stage.getCamera();
		currentZoom=camera.zoom;
	}

	private boolean getShakeStatus(SoundManager soundManager){
		boolean shakeStatus = true;
		if(client.getPlayerNumber() == 1){
			shakeStatus = false;
			soundManager.playSound(Sounds.PLAYERTURN);
		}
		return shakeStatus;
	}

	private void setGestures(){
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
	}

	private void setSkin(){
		TextureAtlas atlas = new TextureAtlas("uiskin.atlas");
		skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
	}

	private void setPlayground(){
		Texture txtPlayground = new Texture("Playboard.jpg");
		Image imgPlayground=new Image(txtPlayground);

		stage.addActor(imgPlayground);
	}

	private void setYourTurnLabel(){
		Label yourTurn=new Label("It's your turn!",skin);
		yourTurn.setName("yourTurn");
		yourTurn.setColor(Color.RED);
		yourTurn.setBounds(100,500,300,200);
		yourTurn.setFontScale(4f);
		yourTurn.setVisible(false);
		stage.addActor(yourTurn);
	}

	@Override
	public void show() {
		//nothing to change yet
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		float xGrav=Gdx.input.getAccelerometerX();
		float yGrav=Gdx.input.getAccelerometerY();
		float zGrav=Gdx.input.getAccelerometerZ();
		//gForce will be close to 10 when there is no movement.
		float gForce=(float)Math.sqrt((xGrav*xGrav)+(yGrav*yGrav)+(zGrav*zGrav));
		dice.shake(gForce);
	}

	@Override
	public void resize (int width, int height) {
		// true - center camera.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		//should be implemented
	}

	@Override
	public void resume() {
		//will be programmed
	}

	@Override
	public void hide() {
		//not sure if we will use this
	}

	@Override
	public void dispose () {
		client.terminate();
		if(client.getServer() != null) {
			client.getServer().stopServer();
		}
		stage.dispose();
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
		camera.translate(-deltaX * currentZoom,deltaY * currentZoom);
		camera.update();
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		currentZoom = camera.zoom;
		return false;
	}

	/*
	initialDistance - distance between fingers when the gesture started
	distance - current distance between fingers
	 */
	@Override
	public boolean zoom(float initialDistance, float distance) {
		float epsilon = 0.0000001F;
		if(initialDistance>=distance && Math.abs(camera.zoom - 1.0f) > epsilon){ //zoom out
			camera.zoom += 0.005f;
			camera.update();
		}
		else if(initialDistance<=distance && Math.abs(camera.zoom - 0.6800003f) > epsilon){ //zoom in
			camera.zoom -= 0.005f;
			camera.update();
		}
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {
		//don't know if we implement this
	}
}
