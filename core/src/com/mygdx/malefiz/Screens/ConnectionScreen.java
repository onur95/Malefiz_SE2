package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.malefiz.GNwKryo.GameClient;
import com.mygdx.malefiz.Malefiz;

// TODO: Ausimplementierung der Klasse
// Necessary ONLY for clients -- Servers are already in some game.

public class ConnectionScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Image img_background_menu;
    private Texture txt_background_menu;
    private ImageButton imageButtonConnect;
    private ImageButton imageButtonReturn;
    private final Malefiz game;
    private GameClient client;

    String input;

    public ConnectionScreen(final Malefiz game){
        this.game = game;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        /*batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        stage = new Stage(viewport, batch);*/
        imageButtonConnect=MainMenuScreen.createImageButton("connect.png",375,335,350,150);
        imageButtonReturn=MainMenuScreen.createImageButton("return.png",380,220,350,150);
        stage = new Stage(new FillViewport(1024,670));
        txt_background_menu=new Texture("malefiz_mainmenu_background.jpg");
        img_background_menu=new Image(txt_background_menu);
        Gdx.input.setInputProcessor(stage);

    }

    public void show() {
       /* //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();*/

        //Create editable Textfields
        final TextField eIPText = new TextField("Enter public IP. (F.ex.: 10.226.172.156)", skin);
        eIPText.setBounds(400,450,300,100);
        final Label connectionInfo= new Label("Waiting for other Players to connect",skin);
        connectionInfo.setColor(Color.RED);
        connectionInfo.setBounds(375,100,350,200);
        connectionInfo.setFontScale(1.5f);
        connectionInfo.setVisible(false);

        // TODO: Check if we really do not need any listener on TextField >> Test

        // Create connection-Button
        /*TextButton conButton = new TextButton("Connect", skin);

        TextButton returnButton = new TextButton("Return", skin);*/

        imageButtonConnect.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                final String ip = eIPText.getText();

                client = new GameClient(44775, 44776, 10000, game);
                try{
                    connectionInfo.setVisible(true);
                    connectionInfo.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.delay(2f),Actions.fadeIn(1f))));
                    client.connect(ip);
                    Gdx.app.log("Client", "Successfully connected to server.");
                }catch(Exception e){
                    client.terminate();
                    Gdx.app.log("Client", "Failed to Connect to server.", e);
                }

            }
        });

        imageButtonReturn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(client != null) {
                    client.terminate();
                }
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        //Add buttons to table
        /*mainTable.add(eIPText).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/5);
        mainTable.row();
        mainTable.add(conButton).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/3);
        mainTable.row();
        mainTable.add(returnButton).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/5);

        //Add table to stage
        stage.addActor(mainTable);*/
        stage.addActor(img_background_menu);
        stage.addActor(imageButtonConnect);
        stage.addActor(imageButtonReturn);
        stage.addActor(eIPText);
        stage.addActor(connectionInfo);
    }

    @Override
    public void dispose() {
        // Does this suffice for disposing connectionlist?
        if(client != null) {
            client.terminate();
        }
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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

        //viewport.update(width, height);
        stage.getViewport().update(width,height);
    }
}
