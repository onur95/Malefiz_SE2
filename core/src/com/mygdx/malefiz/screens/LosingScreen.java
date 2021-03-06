package com.mygdx.malefiz.screens;

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
import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.sound.SoundManager;
import com.mygdx.malefiz.sound.Sounds;

/**
 * Created by Onur on 10.06.2017.
 */

public class LosingScreen implements Screen {

    private Stage stage;
    private final Malefiz game;
    private Texture txtBackground;
    private Image imgBackground;
    private Texture txtLoser;
    private Image imgLoser;
    private ImageButton backtomenu;
    private ImageButton exitgame;
    private GameClient client;
    private SoundManager soundManager;

    /**
     * Called once the screen is created
     * @param game
     * @param client
     * @param soundManager
     */
    public LosingScreen(final Malefiz game, GameClient client, SoundManager soundManager){
        this.game=game;
        this.client=client;
        this.soundManager=soundManager;
        stage = new Stage(new FillViewport(1024,670));
        txtBackground=new Texture("malefiz_mainmenu_background.jpg");
        imgBackground=new Image(txtBackground);
        txtLoser=new Texture("loser_slogan.png");
        imgLoser=new Image(txtLoser);
        imgLoser.setBounds(350,425,400,200);
        backtomenu= MainMenuScreen.createImageButton("back_to_menu.png",375,320,350,150);
        exitgame= MainMenuScreen.createImageButton("exit_button.png",375,200,350,150);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when this screen becomes the current screen for a Game
     */
    @Override
    public void show() {

        backtomenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopServerAndClient();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        exitgame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopServerAndClient();
                Gdx.app.exit();
            }
        });

        stage.addActor(imgBackground);
        stage.addActor(imgLoser);
        stage.addActor(backtomenu);
        stage.addActor(exitgame);
        soundManager.playSound(Sounds.LOSER);
    }

    private void stopServerAndClient(){
        if(client.getServer()!=null) {
            client.getServer().stopServer();
        }
        client.terminate();
    }

    /**
     * Called when the screen should render itself
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    /**
     * Called when the screen resizes itself
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the screen pauses. e.g. incoming call
     */
    @Override
    public void pause() {
        //not needed
    }

    /**
     * Called when the screen resumes from a paused state
     */
    @Override
    public void resume() {
        //not needed
    }

    /**
     * Called when this screen is no longer the current
     * screen for a Game
     */
    @Override
    public void hide() {
        //not needed
    }

    /**
     * Called when this screen should release all resources
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
