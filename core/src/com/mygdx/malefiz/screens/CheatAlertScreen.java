package com.mygdx.malefiz.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


import java.util.logging.Level;
import java.util.logging.Logger;


public class CheatAlertScreen{
    private Stage stage;
    private Skin defSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private static final Logger LOGGER = Logger.getLogger( CheatAlertScreen.class.getName() );


    public CheatAlertScreen(Stage stage) {
        this.stage = stage;
    }

    public void createDisplay(){
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
        LOGGER.log(Level.INFO, "CheatAlertScreen: Creating.");
        dialog.show(stage);
    }
}
