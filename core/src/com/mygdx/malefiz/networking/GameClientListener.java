package com.mygdx.malefiz.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.malefiz.MyMalefizGame;
import com.mygdx.malefiz.Player;
import com.mygdx.malefiz.playboard.BoardToPlayboard;
import com.mygdx.malefiz.screens.CheatAlertScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClientListener extends Listener {
    private GameClient client;
    private static final Logger LOGGER = Logger.getLogger( GameClientListener.class.getName() );

    public GameClientListener(GameClient client) {
        super();
        this.client = client;
    }

    @Override
    public void received(Connection connection, Object object) {
        LOGGER.log(Level.INFO, "Client: Message received");

        if(client == null || object == null){
            return;
        }
        // Received from Connection that Object
        if(object instanceof Network.ServerEcho){
            // Parse data to use it.
            Network.ServerEcho serverEcho = (Network.ServerEcho) object;

            if(client.getPlayerNumber() != serverEcho.playerTurnBefore) {//Der Spieler der den Zug getätigt hat, wird nicht angepasst
                client.getHandler().updatePlayboard(serverEcho.update, serverEcho.playerTurn, serverEcho.playerTurnBefore, serverEcho.cheated);
            }
        }
        else if(object instanceof Network.StartClient){
            final Network.StartClient startClient = (Network.StartClient) object;
            int message = startClient.player;
            if(message != -1){
                client.setPlayerNumber(message);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        client.getGame().setScreen(new MyMalefizGame(client, startClient.playerCount));
                    }
                });

            }
            else{
                client.terminate();
            }
        }
        else if(object instanceof Network.PlayerDisconnected) {//String wird noch geändert
            client.getHandler().playerDisconnected(((Network.PlayerDisconnected) object).player);
        }
    }

    @Override
    public void disconnected(Connection connection){
        client.terminate();
        LOGGER.log(Level.INFO, "Client: client disconnected");
    }

}
