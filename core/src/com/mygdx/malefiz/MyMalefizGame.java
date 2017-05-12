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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.malefiz.Screens.*;
import com.mygdx.malefiz.Screens.GameMenu;

public class MyMalefizGame implements Screen, GestureDetector.GestureListener {

	public static Stage stage;
	Texture txt_playground;
	Image img_playground;
	final Malefiz mal;
	String pfad;
	private float currentZoom;

	public MyMalefizGame (final Malefiz mal) {
		this.mal=mal;
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		stage=new Stage(new FitViewport(1500, 1500));
		currentZoom=((OrthographicCamera)stage.getCamera()).zoom;
		im.addProcessor(gd);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		txt_playground = new Texture("Playboard.jpg");
		img_playground=new Image(txt_playground);
		stage.addActor(img_playground);
		Board.init();
		BoardToPlayboard.init();
		BoardToPlayboard.generate();
	// Disable Menu/Exit-Buttons via Commenting here
		stage.addActor(GameMenu.createExit());
		stage.addActor(GameMenu.createMenu());

		Dice.randomNumber();
		pfad=Dice.getResult(Dice.getResultNumber());
		DiceAnimation.create(pfad);


		//auskommentiert da die Bewegung sonst hängt
		//Gdx.graphics.setContinuousRendering(false);
		//Gdx.graphics.requestRendering();
	}

	public static Stage getState(){
		return stage;
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
		Dice.shake();
	}

	@Override
	public void resize (int width, int height) {
		// See below for what true means.
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
	public boolean longPress(float x, float y) {
		return false;
	}

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
		float actualZoom=((OrthographicCamera)stage.getCamera()).zoom;
		if(initialDistance>=distance && actualZoom!=currentZoom){ //zoom out
			((OrthographicCamera)stage.getCamera()).zoom += 0.005f;
			actualZoom+=0.005f;
		}
		else if(initialDistance<=distance){ //zoom in
			((OrthographicCamera) stage.getCamera()).zoom -= 0.005f;
			actualZoom-=0.005f;

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
