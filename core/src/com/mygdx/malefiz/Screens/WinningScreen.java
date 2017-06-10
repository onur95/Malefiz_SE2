package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.malefiz.Malefiz;

/**
 * Created by Onur on 10.06.2017.
 */

public class WinningScreen implements Screen {

    private final Malefiz game;
    private ImageButton backtomenu;
    private ImageButton exitgame;
    private Stage stage;
    private Texture txtBackground;
    private Image imgBackground;
    private Texture txtWinner;
    private Image imgWinner;

    public WinningScreen(final Malefiz game){
        this.game=game;
        stage = new Stage(new FillViewport(1024,670));
        txtBackground=new Texture("malefiz_mainmenu_background.jpg");
        imgBackground=new Image(txtBackground);
        txtWinner=new Texture("winner_slogan.png");
        imgWinner=new Image(txtWinner);
        imgWinner.setBounds(375,425,400,200);
        backtomenu=MainMenuScreen.createImageButton("back_to_menu.png",375,320,350,150);
        exitgame=MainMenuScreen.createImageButton("exit_button.png",375,200,350,150);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        backtomenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
                game.setScreen(new MainMenuScreen(game));
            }
        });

        exitgame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(imgBackground);
        stage.addActor(imgWinner);
        stage.addActor(backtomenu);
        stage.addActor(exitgame);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
    public void dispose() {
        stage.dispose();
    }
}
