package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.MyMalefizGame;

/**
 * Created by Onur on 04.04.2017.
 */

public class MainMenuScreen implements Screen {

    private SpriteBatch batch;
    Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private Image img_background_menu;
    private Texture txt_background_menu;
    private ImageButton imageButtonStart;
    private ImageButton imageButtonExit;
    private OrthographicCamera camera;
    private Viewport viewport;
    final Malefiz game;

    public MainMenuScreen(final Malefiz game){
        this.game=game;
        /*atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();*/
        imageButtonStart=createImageButton("start_button.png",375,335,350,150);
        imageButtonExit=createImageButton("exit_button.png",375,200,350,150);
        stage = new Stage(new FitViewport(1024,670));
        txt_background_menu=new Texture("malefiz_mainmenu_background.jpg");
        img_background_menu=new Image(txt_background_menu);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        //Create Table
       /* Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create buttons
        TextButton playButton = new TextButton("Start", skin);
        playButton.getLabel().setSize(150f,150f);
        //TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);*/

        //Add listeners to buttons
        imageButtonStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MyMalefizGame(game));
            }
        });
        imageButtonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        /*//Add buttons to table
        mainTable.add(playButton).width(Gdx.graphics.getWidth()/2).height(Gdx.graphics.getHeight()/2);
        //mainTable.row();
        mainTable.row();
        mainTable.add(exitButton).width(Gdx.graphics.getWidth()/2).height(Gdx.graphics.getHeight()/2);

        //Add table to stage
        stage.addActor(mainTable);*/
        stage.addActor(img_background_menu);
        stage.addActor(imageButtonStart);
        stage.addActor(imageButtonExit);
    }

    private ImageButton createImageButton(String file, float x, float y, float width, float height){
        Texture txt=new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        imageButton.setBounds(x,y,width,height);
        return imageButton;
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
