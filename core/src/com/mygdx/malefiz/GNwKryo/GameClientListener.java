package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.Player;

public class GameClientListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {
        // Received from Connection that Object
        if(object instanceof Network.ServerEcho){
            // Parse data to use it.
            Network.ServerEcho serverEcho = (Network.ServerEcho) object;

            // Check if the correct player has made a move
            if(serverEcho.actorIndex == Player.getNumber()){
                // Then move the figure accordingly
                Board.moveTo(serverEcho.column, serverEcho.row, false);
                Gdx.app.log("GameClientListener", "Transmission successfull. Updating playfield");
            }
            else{
                Gdx.app.log("GameClientListener", "Transmission fault.");
            }

            // Set player for next turn :: There is only 1,2,3,4
            if(serverEcho.playerTurn > 0 && serverEcho.playerTurn < 5 ){
                Player.setNumber(serverEcho.playerTurn);
                Gdx.app.log("GameClientListener", "Turn of Player #" + serverEcho.playerTurn);
            }else{
                Gdx.app.log("GameClientListener", "Error updating Players turn.");
            }
        }
    }
}
