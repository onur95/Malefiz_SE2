package com.mygdx.malefiz.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.cheats.CheatEngine;
import com.mygdx.malefiz.dice.Dice;

import static com.mygdx.malefiz.screens.MainMenuScreen.createImageButton;

/* Serves the purpose to do something Ingame */

public class GameMenu
{
    private Stage stage;
    private Skin defSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private String serverIp;
    private Dice dice;
    private Dialog menuDialog;
    private Dialog exitDialog;
    private Dialog cheatDialog;
    private CheatEngine cheatEngine;
    // CodeEntry MUST REMAIN GLOBAL. Otherwise Bugs.
    private TextField cheatCodeEntry = new TextField("Enter Code here", defSkin);

    public GameMenu(Stage stage, String serverIp, CheatEngine cheatEngine, Dice dice){
        this.stage = stage;
        this.serverIp = serverIp;
        this.dice = dice;
        this.cheatEngine = cheatEngine;
        setMenuDialog();
        setExitDialog();
        setCheatDialog();
    }

    private void setCheatDialog(){
        cheatDialog = new Dialog("Cheat Menu", defSkin){

            public void result(Object object){
                if(object.equals(1L)){
                    cheatEngine.cheatCaller(cheatCodeEntry.getText());

                }
                if(object.equals(2L)){
                    dice.setShaked(false);
                }
            }
        };

        // Menu-Composition.
        cheatDialog.add(cheatCodeEntry);
        cheatDialog.button("Back to Game", 2L);
        cheatDialog.button("Confirm", 1L);
        cheatDialog.setBounds(stage.getHeight()/2, stage.getWidth()/2, 400, 150);
    }

    private void setExitDialog(){
        exitDialog = new Dialog("Confirm Exit", defSkin){
            public void result(Object object){
                if(object.equals(2L)){
                    Gdx.app.exit();
                }
            }
        };
        exitDialog.button("Exit Game", 2L);
        exitDialog.button("Resume Game", 1L);
    }

    private void setMenuDialog(){
        menuDialog = new Dialog("Game Menu", defSkin){
            public void result(Object object){
                if(object.equals(1L)){
                    dice.setShaked(false);
                }
                if(object.equals(2L))
                {
                    cheatMenu();
                }
            }
        };
        menuDialog.text(serverIp);
        menuDialog.button("Cheat", 2L);
        menuDialog.button("Resume", 1L);
        menuDialog.setBounds(stage.getHeight()/2, stage.getWidth()/2, 400, 150);
    }

    public void createExit(){
        ImageButton exitButton = createImageButton("ExitGame.png",125,875,330,215);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitDialog.show(stage);
            }
        });
        stage.addActor(exitButton);

    }

    public void createMenu(){
        ImageButton menuButton = createImageButton("MenuButton.png",1025,900,330,215);
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // wenn des GameMenü aufgmocht weat, setz shaked vom dice auf true
                if(dice.getShaked()){
                    return;
                }
                dice.setShaked(true);
                menuDialog.show(stage);
            }
        });
        stage.addActor(menuButton);
    }

    public void cheatMenu(){
        cheatDialog.show(stage);
    }
}