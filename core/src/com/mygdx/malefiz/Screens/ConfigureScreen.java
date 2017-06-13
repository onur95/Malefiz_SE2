package com.mygdx.malefiz.Screens;

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
import com.mygdx.malefiz.networking.GameServer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigureScreen implements Screen {
    
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Image imgBackgroundMenu;
    private Texture txtBackgroundMenu;
    private ImageButton imageButtonStartServer;
    private ImageButton imageButtonReturn;
    private final Malefiz game;
    private GameClient client;
    private GameServer server;
    private static final Logger LOGGER = Logger.getLogger( ConfigureScreen.class.getName() );

    public ConfigureScreen(final Malefiz game){
        this.game=game;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        imageButtonStartServer= MainMenuScreen.createImageButton("start_server.png",375,335,350,150);
        imageButtonReturn= MainMenuScreen.createImageButton("return.png",375,220,350,150);
        stage = new Stage(new FillViewport(1024,670));
        txtBackgroundMenu=new Texture("malefiz_mainmenu_background.jpg");
        imgBackgroundMenu=new Image(txtBackgroundMenu);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Label setPlayersInfo = new Label("The maximum amount of players: ", skin);
        setPlayersInfo.setBounds(375,450,250,90);
        setPlayersInfo.setColor(Color.RED);
        final TextField playerNumber = new TextField("3", skin);
        playerNumber.setBounds(650,470,50,50);
        playerNumber.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        final Label connectionInfo= new Label("Waiting for other Players to connect",skin);
        connectionInfo.setColor(Color.RED);
        connectionInfo.setBounds(375,100,350,200);
        connectionInfo.setFontScale(1.5f);
        connectionInfo.setVisible(false);
        //Add listeners to buttons

        imageButtonStartServer.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if(playerNumber.getText().length() == 0){
                    return;
                }
                server = new GameServer(44775, 44776, Integer.parseInt(playerNumber.getText()));
                try{
                    server.startServer();
                    client = new GameClient(44775, 44776, 10000, game,server);
                    connectionInfo.setVisible(true);
                    connectionInfo.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.delay(2f),Actions.fadeIn(1f))));
                    client.connect("");
                    LOGGER.log(Level.FINE, "Server, Client: Successfully started and connected");
                }catch(Exception e){
                    server.stopServer();
                    if(client != null){
                        client.terminate();
                    }
                    LOGGER.log(Level.SEVERE, "Server: Failed to create Server on starting the main game", e);
                }
            }
        });

        imageButtonReturn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to Mainmenu
                if(server != null){
                    server.stopServer();
                }
                if(client != null){
                    client.terminate();
                }
                game.setScreen(new MainMenuScreen(game));
            }
        });

        //Add table to stage*/
        stage.addActor(imgBackgroundMenu);
        stage.addActor(imageButtonStartServer);
        stage.addActor(imageButtonReturn);
        stage.addActor(setPlayersInfo);
        stage.addActor(playerNumber);
        stage.addActor(connectionInfo);
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
        stage.getViewport().update(width,height);
    }

    @Override
    public void pause() {
        //not needed
    }

    @Override
    public void resume() {
        //no use for it
    }

    @Override
    public void hide() {
        //will not be used
    }

    @Override
    public void dispose() {
        if(server != null){
            server.stopServer();
        }
        if(client != null){
            client.terminate();
        }
        skin.dispose();
        atlas.dispose();
        stage.dispose();
    }
}