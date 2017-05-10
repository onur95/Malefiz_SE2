package com.mygdx.malefiz.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.GNwKryo.GameServer;
import com.mygdx.malefiz.MyMalefizGame;

/* Serves the purpose to do something Ingame */

public class GameMenu
{
    static TextButton exitButton;
    static TextButton menuButton;


    static Skin defSkin = new Skin(Gdx.files.internal("uiskin.json"));


    public static Actor createExit()
    {
        exitButton = new TextButton("Exit Game", defSkin);
        exitButton.setPosition(250, 943);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        return exitButton;
    }

    public static Actor createMenu()
    {
        menuButton = new TextButton("Game Menu", defSkin);
        menuButton.setPosition(1125, 925);

        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = new Dialog("Game Menu", defSkin){
                    public void result(Object object){
                        if(object.equals(1L)){

                        }
                    }
                };
                dialog.text(GameServer.fetchPublicIP());
                dialog.button("Resume", 1L);
                dialog.show(MyMalefizGame.stage);
            }
        });

        return menuButton;
    }
}

/* Old Menu. Valid but not that usable for our Stage
    static Table menu;
    static Label menuLabel;
    static TextButton tBtn;
    static Skin defSkin;

    // Draw the Menu here
    public static Table create()
    {
        // Define Menuinput
        defSkin = new Skin(Gdx.files.internal("uiskin.json"));

        menuLabel = new Label("Menu", defSkin);

        tBtn = new TextButton("Exit Game.", defSkin);
        tBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Put MenuInput into Group
        VerticalGroup vG = new VerticalGroup();

        // Add Things into Group
        //vG.addActor(menuLabel);
        vG.addActor(tBtn);
        vG.addActor(menuLabel);

        // Define Menu & Add Group to Menu
        menu = new Table();
        menu.add(vG);

        // TODO: Set Position-parameters for the Menu :: Location on Stage. Needs somewhere better than *there*
        menu.setPosition(100, 925);
        menu.setVisible(true);

        return menu;
    }
*/