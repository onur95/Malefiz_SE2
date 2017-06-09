package com.mygdx.malefiz.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.networking.GameClient;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionScreen implements Screen {
    private Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private Image imgBackgroundMenu;
    private Texture txtBackgroundMenu;
    private ImageButton imageButtonConnect;
    private ImageButton imageButtonReturn;
    private final Malefiz game;
    private GameClient client;
    private static final Logger LOGGER = Logger.getLogger( ConfigureScreen.class.getName() );

    public ConnectionScreen(final Malefiz game){
        this.game = game;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        imageButtonConnect= MainMenuScreen.createImageButton("connect.png",375,335,350,150);
        imageButtonReturn= MainMenuScreen.createImageButton("return.png",380,220,350,150);
        stage = new Stage(new FillViewport(1024,670));
        txtBackgroundMenu=new Texture("malefiz_mainmenu_background.jpg");
        imgBackgroundMenu=new Image(txtBackgroundMenu);
        Gdx.input.setInputProcessor(stage);

    }

    public void show() {
        //Create editable Textfields
        final TextField eIPText = new TextField("Enter public IP. (F.ex.: 10.226.172.156)", skin);
        eIPText.setBounds(400,450,300,100);
        final Label connectionInfo= new Label("Waiting for other Players to connect",skin);
        connectionInfo.setColor(Color.RED);
        connectionInfo.setBounds(375,100,350,200);
        connectionInfo.setFontScale(1.5f);
        connectionInfo.setVisible(false);


        // Create connection-Button

        imageButtonConnect.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                final String ip = eIPText.getText();
                client = new GameClient(44775, 44776, 10000, game);
                try{
                    connectionInfo.setVisible(true);
                    connectionInfo.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.delay(2f),Actions.fadeIn(1f))));
                    client.connect(ip);
                    LOGGER.log(Level.FINE, "Client: Successfully connected to server");
                }catch(Exception e){
                    client.terminate();
                    LOGGER.log(Level.SEVERE, "Client: Failed to connect to server", e);
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


        stage.addActor(imgBackgroundMenu);
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
        //no use for it
    }

    @Override
    public void pause() {
        //not needed
    }

    @Override
    public void resume() {
        //also no need
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
    }
}
