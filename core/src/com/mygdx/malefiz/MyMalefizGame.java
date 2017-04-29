package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.malefiz.GNwKryo.GameServer;


public class MyMalefizGame implements Screen {

	private static Stage stage;
	Texture txt_playground;
	Image img_playground;
	final Malefiz mal;
	String pfad;


	public MyMalefizGame (final Malefiz mal) {
		this.mal=mal;
		stage=new Stage(new FitViewport(1500,1498));
		Gdx.input.setInputProcessor(stage);
		txt_playground = new Texture("Playboard.jpg");
		img_playground=new Image(txt_playground);
		Board.init();
		BoardToPlayboard.init();
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
		// Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		// MenuDialog mDia = new MenuDialog("Menu", uiSkin);
		// mDia.show(stage);
	}
/* TODO: Not quite working on our maingame.
	public class MenuDialog extends Dialog{


		public MenuDialog(String title, Skin skin) {
			super(title, skin);
		}
		public MenuDialog(String title, Skin skin, String windowStyleName) {
			super(title, skin, windowStyleName);
		}
		public MenuDialog(String title, WindowStyle windowStyle) {
			super(title, windowStyle);
		}

		{	// Used in every Constructor
			text("IP = " + GameServer.fetchPublicIP());
			button("Resume Game");
		}

		@Override
		protected void result(Object object) {

		}
	}
*/
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.addActor(img_playground);
		stage.act();
		stage.draw();
		BoardToPlayboard.generate();
		DiceAnimation.render();
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

}
