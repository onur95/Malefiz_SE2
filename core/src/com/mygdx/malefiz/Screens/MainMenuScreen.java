package com.mygdx.malefiz.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.malefiz.Malefiz;

/**
 * Created by Onur on 04.04.2017.
 */

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Image imgBackgroundMenu;
    private Texture txtBackgroundMenu;
    private Image imgMalefizLogo;
    private Texture txtMalefizLogo;
    private ImageButton imageButtonNewGame;
    private ImageButton imageButtonConnect;
    private ImageButton imageButtonExit;
    private final Malefiz game;

    public MainMenuScreen(final Malefiz game){
        this.game=game;
        imageButtonNewGame=createImageButton("new_game_setup.png",375,335,350,150);
        imageButtonConnect=createImageButton("connect_to.png",375,220,350,150);
        imageButtonExit=createImageButton("exit_button.png",375,100,350,150);
        stage = new Stage(new FillViewport(1024,670));
        txtBackgroundMenu=new Texture("malefiz_mainmenu_background.jpg");
        imgBackgroundMenu=new Image(txtBackgroundMenu);
        txtMalefizLogo=new Texture("malefiz_logo.png");
        imgMalefizLogo=new Image(txtMalefizLogo);
        imgMalefizLogo.setBounds(375,450,400,200);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        //Add listeners to buttons
        imageButtonNewGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //((Game)Gdx.app.getApplicationListener()).setScreen(new ConfigureScreen(game));
                game.setScreen(new ConfigureScreen(game));
            }
        });
        imageButtonConnect.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //((Game)Gdx.app.getApplicationListener()).setScreen(new ConnectionScreen(game));
                game.setScreen(new ConnectionScreen(game));
            }
        });
        imageButtonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(imgBackgroundMenu);
        stage.addActor(imgMalefizLogo);
        stage.addActor(imageButtonNewGame);
        stage.addActor(imageButtonConnect);
        stage.addActor(imageButtonExit);
    }

    public static ImageButton createImageButton(String file, float x, float y, float width, float height){
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
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        //no need for it
    }

    @Override
    public void resume() {
        //will not be used
    }

    @Override
    public void hide() {
        //I don't know for what this is
    }

    @Override
    public void dispose() {
       stage.dispose();
    }
}
