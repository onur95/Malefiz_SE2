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
import com.mygdx.malefiz.GNwKryo.GameServer;
import com.mygdx.malefiz.Malefiz;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigureScreen implements Screen {

    private SpriteBatch batch;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Image img_background_menu;
    private Texture txt_background_menu;
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
        /*batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        stage = new Stage(viewport, batch);*/
        imageButtonStartServer=MainMenuScreen.createImageButton("start_server.png",375,335,350,150);
        imageButtonReturn=MainMenuScreen.createImageButton("return.png",375,220,350,150);
        stage = new Stage(new FillViewport(1024,670));
        txt_background_menu=new Texture("malefiz_mainmenu_background.jpg");
        img_background_menu=new Image(txt_background_menu);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
       /* //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();


        TextButton playButton = new TextButton("Start", skin);
        TextButton returnButton = new TextButton("Return", skin);
        */
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
                if(playerNumber.getText().length() == 0){
                    return;
                }
		        /* Networking */
                server = new GameServer(44775, 44776, Integer.parseInt(playerNumber.getText()));
                try{
                    server.startServer();
                    LOGGER.log(Level.FINE, "Server: Server successfully started on starting the main game");
                    client = new GameClient(44775, 44776, 10000, game);

                    try{
                        connectionInfo.setVisible(true);
                        connectionInfo.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.delay(2f),Actions.fadeIn(1f))));
                        client.connect("");
                        LOGGER.log(Level.FINE, "Client: Successfully connected to server");
                    }catch(Exception e){
                        client.terminate();
                        server.stopServer();
                        LOGGER.log(Level.FINE, "Client: Failed to connect Client of server to server");
                    }
                }catch(Exception e){
                    server.stopServer();
                    LOGGER.log(Level.FINE, "Server: Failed to create Server on starting the main game");
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
        /*HorizontalGroup buttons = new HorizontalGroup();
        buttons.addActor(returnButton);
        buttons.addActor(playButton);
        buttons.setOrigin(buttons.getWidth()/2, buttons.getHeight()/2);
        buttons.setPosition(Gdx.graphics.getWidth()/2 - (buttons.getWidth()/2), Gdx.graphics.getHeight()/2 - (buttons.getHeight()/2));

        // Add all to menu
        VerticalGroup menu = new VerticalGroup();
//      menu.addActor(playerDetails);
        menu.addActor(buttons);

        mainTable.add(menu);

        //Add table to stage*/
        stage.addActor(img_background_menu);
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
        //viewport.update(width, height);
        stage.getViewport().update(width,height);
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