package com.mygdx.malefiz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.viewport.FitViewport;


public class MyMalefizGame extends ApplicationAdapter {
	private static Stage stage;
	Texture txt_playground;
	Texture txt_circle;
	Image img_playground;
	Image img_circle;

	private ClickListener playgroundListener = new ClickListener(){
		public boolean touchDown(InputEvent event, float x, float y, int pointer,int button){
			return true;
		}
		public void touchUp(InputEvent event, float x, float y, int pointer, int button){
			MoveToAction action = new MoveToAction();
			action.setPosition(x,y);
			action.setDuration(0.2f);
			img_circle.addAction(action);
		}
	};

	@Override
	public void create () {
		stage=new Stage(new FitViewport(1500,1498));
		Gdx.input.setInputProcessor(stage);
		txt_playground = new Texture("Playboard.jpg");
		txt_circle=new Texture("circle-xl.png");
		img_playground=new Image(txt_playground);
		img_circle=new Image(txt_circle);
		Board.init();
		BoardToPlayboard.init();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		img_playground.addListener(playgroundListener);
		stage.addActor(img_playground);
		stage.addActor(img_circle);
		stage.act();
		stage.draw();
		BoardToPlayboard.render();
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
}
