package com.mygdx.malefiz;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.malefiz.screens.MainMenuScreen;

/**
 * Created by Onur on 04.04.2017.
 */

public class Malefiz extends Game {

    private Stage stage;

    public void create() {
        this.stage=new Stage();
        this.setScreen(new MainMenuScreen(this));
    }


    public void dispose() {
        stage.dispose();
    }

}
