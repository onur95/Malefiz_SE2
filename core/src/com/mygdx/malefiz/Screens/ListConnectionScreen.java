package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// TODO: Ausimplementierung der Klasse
// Necessary ONLY for clients.

public class ListConnectionScreen implements Screen {
    private SpriteBatch batch;
    Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;

    public ListConnectionScreen{
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

    }

    public void show() {
        //Create Table as in MainMenuScreen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        // Fill Table with possible Socketconnections
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {

    }
}

/* TODO :: Take a look. This may be more solid for overall coding, dunno if worth.
public class MainClass extends Game implements ApplicationListener {
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;

    @Override
    public void create() {
        // Code
    }
    setGameScreen()
    {
        gameScreen=new GameScreen(this);
        setScreen(gameScreen);
    }
    setMenuScreen()
    {
        menuScreen=new menuScreen(this);
        setScreen(menuScreen);
    }
    setConnectivityScreen()
    {
        connectivityScreen=new connectivityScreen(this);
        setScreen(connectivityScreen);
    }

    @Override
    public void dispose() {

        super.dispose();
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void resize(int width, int height) {

        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }
}

 */
