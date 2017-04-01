package com.mygdx.malefiz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MyMalefizGame extends ApplicationAdapter {
	private Stage stage;
	Texture txt;
	Image img;

	@Override
	public void create () {
		stage=new Stage(new StretchViewport(1500,1498));
		Gdx.input.setInputProcessor(stage);
		txt = new Texture("Playboard.jpg");
		img=new Image(txt);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.addActor(img);
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
