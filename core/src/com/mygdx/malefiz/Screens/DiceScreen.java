package com.mygdx.malefiz.Screens;

/**
 * Created by Lilibeth on 25.04.2017.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.malefiz.Dice;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.MyMalefizGame;

/**
 * Created by Lilibeth on 24.04.2017.
 */

public class DiceScreen implements Screen {
    private SpriteBatch batch;
    TextureRegion[][] regions;
    Sprite sprite;
    int zeile;
    int spalte;
    Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    private OrthographicCamera camera;
    private Viewport viewport;
    Image imgResult;
    static Image result1;
    static Image result2;
    static Image result3;
    static Image result4;
    static Image result5;
    static Image result6;
    final Malefiz game;

    public DiceScreen(final Malefiz game) {
        this.game = game;
        batch = new SpriteBatch();
        result1 = new Image(new Texture("W- (1).png"));
        result2 = new Image(new Texture("W- (2).png"));
        result3 = new Image(new Texture("W- (3).png"));
        result4 = new Image(new Texture("W- (4).png"));
        result5 = new Image(new Texture("W- (5).png"));
        result6 = new Image(new Texture("W- (6).png"));
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        viewport.apply();
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        sprite = new Sprite(regions[0][0]);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spalte++;
                if (spalte > 11) {
                    spalte = 0;
                    if (zeile == 1) {
                        zeile = 0;
                    } else
                        zeile = 1;
                }
                sprite.setRegion(regions[zeile][spalte]);
            }
        }, 0, 0.2f);                                //der sprite ist in einer dauerschleife, da komm ich nicht raus.

        imgResult = showResult(Dice.randomNumber());


        //Create button
        TextButton OKButton = new TextButton("Start", skin);
        OKButton.getLabel().setSize(150f, 150f);


        //Add listeners to buttons
        OKButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MyMalefizGame(game));
            }
        });


        //Add table to stage
        stage.addActor(OKButton);

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

    private static Image showResult(int res) {
        Image result = result1;//default wert
        switch (res) {
            case 1:
                result = result1;
                break;
            case 2:
                result = result1;
                break;
            case 3:
                result = result1;
                break;
            case 4:
                result = result1;
                break;
            case 5:
                result = result1;
                break;
            case 6:
                result = result1;
                break;
        }
        return result;
    }
}




