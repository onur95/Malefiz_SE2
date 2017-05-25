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
        // Received from Connection that Object
        if(object instanceof Network.ServerEcho){
            // Parse data to use it.
            Network.ServerEcho serverEcho = (Network.ServerEcho) object;


            if(client.getPlayerNumber() != serverEcho.playerTurnBefore) {//Der Spieler der den Zug getätigt hat, wird nicht angepasst
                client.getHandler().updatePlayboard(serverEcho.update, serverEcho.playerTurn);
            }



//            Gdx.app.log("GameClientListener", "Transmission successfull. Updating playfield");
//                Gdx.app.log("GameClientListener", "Transmission fault.");


//            // Set player for next turn :: There is only 1,2,3,4
//            if(serverEcho.playerTurn > 0 && serverEcho.playerTurn < 5 ){
//                Player.setNumber(serverEcho.playerTurn);
//                Gdx.app.log("GameClientListener", "Turn of Player #" + serverEcho.playerTurn);
//            }else{
//                Gdx.app.log("GameClientListener", "Error updating Players turn.");
//            }
        }
        else if(object instanceof Integer){
            int message = (Integer) object;
            if(message != -1){
                client.setPlayerNumber(message);
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MyMalefizGame(client.getGame(), client));
            }
            else{
                client.terminate();
            }
        }
    }

}
