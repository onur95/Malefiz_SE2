package com.mygdx.malefiz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.viewport.FitViewport;


public class MyMalefizGame implements Screen {

	private static Stage stage;
	Texture txt_playground;
	Image img_playground;
	final Malefiz mal;

	public MyMalefizGame (final Malefiz mal) {
		this.mal=mal;
		stage=new Stage(new FitViewport(1500,1498));
		Gdx.input.setInputProcessor(stage);
		txt_playground = new Texture("Playboard.jpg");
		img_playground=new Image(txt_playground);
		Board.init();
		BoardToPlayboard.init();
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.addActor(img_playground);
		stage.act();
		stage.draw();
		BoardToPlayboard.generate();
	}

	public static Stage getState(){
		return stage;
	}


	@Override
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () {
		stage.dispose();
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
	public void show() {

	}
}
