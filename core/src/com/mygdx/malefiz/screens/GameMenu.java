package com.mygdx.malefiz.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.Player;
import com.mygdx.malefiz.cheats.CheatEngine;
import com.mygdx.malefiz.cheats.CheatEngineObserver;

import static com.mygdx.malefiz.screens.MainMenuScreen.createImageButton;

/* Serves the purpose to do something Ingame */

public class GameMenu
{
    private ImageButton exitButton;
    private ImageButton menuButton;
    protected Stage stage;
    private Skin defSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private String serverIp;
    // CodeEntry MUST REMAIN GLOBAL. Otherwise Bugs.
    public CheatEngineObserver ceo;
    TextField cheatCodeEntry = new TextField("Enter Code here", defSkin);

    public GameMenu(){

    }

    public GameMenu(Stage stage, String serverIp, CheatEngine cheatEngine){
        this.stage = stage;
        this.serverIp = serverIp;
        this.ceo = new CheatEngineObserver(cheatEngine);
    }

    public Actor createExit(){
        exitButton = createImageButton("ExitGame.png",125,875,330,215);
        stage.addActor(exitButton);

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = new Dialog("Confirm Exit", defSkin){
                    public void result(Object object){
                        if(object.equals(1L)){

                        }
                        if(object.equals(2L)){
                            Gdx.app.exit();
                        }
                    }
                };
                dialog.button("Exit Game", 2L);
                dialog.button("Resume Game", 1L);
                dialog.show(stage);
            }
        });

        return exitButton;
    }

    public Actor createMenu(){
        menuButton = createImageButton("MenuButton.png",1025,900,330,215);
        stage.addActor(menuButton);

        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = new Dialog("Game Menu", defSkin){
                    public void result(Object object){
                        if(object.equals(1L)){

                        }
                        if(object.equals(2L))
                        {
                            cheatMenu();
                        }
                    }
                };
                dialog.text(serverIp);
                dialog.button("Cheat", 2L);
                dialog.button("Resume", 1L);
                dialog.show(stage);
            }
        });

        return menuButton;
    }

    public void cheatMenu(){
        Dialog dialog = new Dialog("Cheat Menu.png", defSkin){
            public void result(Object object){
                if(object.equals(1L)){
                    ceo.setListener(ceo);
//                    ceo.setConfirmedCheater(player.getNumber());
                    ceo.setCheat(cheatCodeEntry.getText());
                }
                if(object.equals(2L)){
                    // Exit to mainmenu
                }
            }
        };

        // Menu-Composition.
        dialog.add(cheatCodeEntry);
        dialog.button("Back to Game", 2L);
        dialog.button("Confirm", 1L);
        dialog.show(stage);
    }
}