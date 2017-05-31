package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.BoardToPlayboard;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.MyMalefizGame;
import com.mygdx.malefiz.Player;

public class GameClientListener extends Listener {
    private GameClient client;

    public GameClientListener(GameClient client) {
        super();
        this.client = client;
    }

    @Override
    public void received(Connection connection, Object object) {
        if(client == null){
            return;
        }
        // Received from Connection that Object
        if(object instanceof Network.ServerEcho){
            Gdx.app.log("Client", "Message received");
            // Parse data to use it.
            Network.ServerEcho serverEcho = (Network.ServerEcho) object;

            if(client.getPlayerNumber() != serverEcho.playerTurnBefore) {//Der Spieler der den Zug getätigt hat, wird nicht angepasst
                client.getHandler().updatePlayboard(serverEcho.update, serverEcho.playerTurn);
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
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new MyMalefizGame(client.getGame(), client, startClient.playerCount));
                    }
                });

            }
            else{
                client.terminate();
            }
        }
        else if(object instanceof Network.PlayerDisconnected) {//String wird ncoh geändert
            client.getHandler().playerDisconnected(((Network.PlayerDisconnected) object).player);
        }
    }
    @Override
    public void disconnected(Connection connection){
        client.terminate();
        Gdx.app.log("Client", "Server disconnected");
    }

}
