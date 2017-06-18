package com.mygdx.malefiz.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class CheatAlertScreen{
    static int cheater;
    private Stage stage;
    private Skin defSkin = new Skin(Gdx.files.internal("uiskin.json"));

    public CheatAlertScreen(int cheater, Stage stage) {
        this.cheater = cheater;
        this.stage = stage;
    }

    public Actor createDisplay(){
        Dialog dialog = new Dialog("Cheating detected!", defSkin){
            public void result(Object object){
                if(object.equals(1L)){

                }
                if(object.equals(2L)){
                    Gdx.app.exit();
                }
            }
        };
        dialog.button("Initiate Ragequit", 2L);
        dialog.button("Resume Game", 1L);
        dialog.show(stage);
        return dialog;
    }
}
