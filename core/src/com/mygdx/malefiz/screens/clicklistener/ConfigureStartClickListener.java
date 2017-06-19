package com.mygdx.malefiz.screens.clicklistener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.networking.GameServer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kstri on 19.06.2017.
 */

public class ConfigureStartClickListener extends ClickListener {
    private TextField playerNumber;
    private GameClient client;
    private GameServer server;
    private Label connectionInfo;
    private static final Logger LOGGER = Logger.getLogger( ConfigureStartClickListener.class.getName() );

    public ConfigureStartClickListener(TextField playerNumber, GameServer server, GameClient client, Label connectionInfo){
        this.playerNumber = playerNumber;
        this.server = server;
        this.client = client;
        this.connectionInfo = connectionInfo;
    }

    @Override
    public void clicked(InputEvent event, float x, float y){
        Gdx.input.setOnscreenKeyboardVisible(false);
        if(playerNumber.getText().length() == 0){
            return;
        }
        server.setMaxUsercount(Integer.parseInt(playerNumber.getText()));
        try{
            server.startServer();
            showConnectionInfo(connectionInfo);
            client.connect("");
        }catch(Exception e){
            server.stopServer();
            if(client != null){
                client.terminate();
            }
            LOGGER.log(Level.SEVERE, "Server: Failed to create Server on starting the main game", e);
        }
    }

    private void showConnectionInfo(Label connectionInfo){
        connectionInfo.setVisible(true);
        connectionInfo.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.delay(2f),Actions.fadeIn(1f))));
    }
}
