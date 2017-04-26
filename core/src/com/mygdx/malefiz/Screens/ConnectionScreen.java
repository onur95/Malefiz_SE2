package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.malefiz.GameNetwork;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.MyMalefizGame;

// TODO: Ausimplementierung der Klasse
// Necessary ONLY for clients -- Servers are already in some game.

public class ConnectionScreen implements Screen {
    private SpriteBatch batch;
    Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;
    final Malefiz game;

    String input;

    public ConnectionScreen(final Malefiz game){
        this.game = game;
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
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create editable Textfields
        TextField eIPText = new TextField("Enter public IP", skin);
        eIPText.setSize(150f, 150f);
        TextField ePortText = new TextField("Enter Port", skin);
        ePortText.setSize(150f, 150f);

        // TODO: Create Listeners to input new Data into textfield


        // Create connection-Button
        TextButton conButton = new TextButton("Connect", skin);
        conButton.getLabel().setSize(150f, 150f);

        conButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // TODO :: Connect client to server

            }
        });

        //Add buttons to table
        mainTable.add(eIPText).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/5);
        mainTable.row();
        mainTable.add(ePortText).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/5);
        mainTable.row();
        mainTable.add(conButton).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/3);

        //Add table to stage
        stage.addActor(mainTable);
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
