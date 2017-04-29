package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.malefiz.GNwKryo.GameServer;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.MyMalefizGame;

public class ConfigureScreen implements Screen {

    private SpriteBatch batch;
    Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;
    final Malefiz game;

    public ConfigureScreen(final Malefiz game){
        this.game=game;
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

    @Override
    public void show() {
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();


        TextButton playButton = new TextButton("Start", skin);
        TextButton returnButton = new TextButton("Return", skin);

        TextField setPlayersInfo = new TextField("The maximum amount of players: ", skin);
        final TextField playerNumber = new TextField("3", skin);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

		        /* Networking */
                GameServer server = new GameServer(44775, 44776);
                try{
                    server.startServer();
                    Gdx.app.log("Server","Server successfully started on starting the main game.");
                }catch(Exception e){
                    server.stopServer();
                    Gdx.app.log("Server","Failed to create Server on starting the main game.");
                }

                /* Start Game */
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MyMalefizGame(game));
            }
        });

        returnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to Mainmenu
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
/*      //Add Textfield
        HorizontalGroup playerDetails = new HorizontalGroup();
        playerDetails.addActor(setPlayersInfo);
        playerDetails.addActor(playerNumber);
        setPlayersInfo.setTouchable(Touchable.disabled);
*/
        //Add buttons to table
        HorizontalGroup buttons = new HorizontalGroup();
        buttons.addActor(returnButton);
        buttons.addActor(playButton);
        buttons.setOrigin(buttons.getWidth()/2, buttons.getHeight()/2);
        buttons.setPosition(Gdx.graphics.getWidth()/2 - (buttons.getWidth()/2), Gdx.graphics.getHeight()/2 - (buttons.getHeight()/2));

        // Add all to menu
        VerticalGroup menu = new VerticalGroup();
//      menu.addActor(playerDetails);
        menu.addActor(buttons);

        mainTable.add(menu);

        //Add table to stage
        stage.addActor(mainTable);
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
        viewport.update(width, height);
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
        skin.dispose();
        atlas.dispose();
    }
}