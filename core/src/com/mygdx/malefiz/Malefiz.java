package com.mygdx.malefiz;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.malefiz.Screens.MainMenuScreen;

/**
 * Created by Onur on 04.04.2017.
 */

public class Malefiz extends Game {

    public Stage stage;

    public void create() {
        this.stage=new Stage();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        stage.dispose();
    }

}
