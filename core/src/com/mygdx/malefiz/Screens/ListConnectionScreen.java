package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// TODO: Ausimplementierung der Klasse
// Necessary ONLY for clients -- Servers are already in some game.

public class ListConnectionScreen implements Screen {
    private SpriteBatch batch;
    Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;

    public ListConnectionScreen(){
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
        Table conTable = new Table();
        conTable.setFillParent(true);
        conTable.top();

        //Search for connections
        Object[] conBuf = new Object[10];     // Display up to 9 possible connections

                // Well, how TODO ?
                // GameNetwork.fetchIPAddresses() > Return null or String[5] with Addresses

        final SelectBox<Object> sb = new SelectBox<Object>(skin);
        sb.setItems(conBuf);

        // Actually fill the table & the app with list of possible connections
        conTable.add(sb);
        stage.addActor(conTable);

    }

    @Override
    public void dispose() {
        // Does this suffice for disposing connectionlist?
        skin.dispose();
        atlas.dispose();
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
        viewport.update(width, height);
    }
}
