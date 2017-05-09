package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.Player;


public class GameServerListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        // Received object from connection

        if (object instanceof Network.ClientMessage) {
            // Parse Data to use it.
            Network.ClientMessage clientTransmission = (Network.ClientMessage) object;

            // Check if the correct player has made a move
            if (clientTransmission.actorIndex == Player.getNumber()) {
                // Then set his figure to the allocated spot.
                Board.moveTo(clientTransmission.column, clientTransmission.row, false);
                Gdx.app.log("GameServerListener", "Receiving Data successfull. Updating field.");

            } else {
                // Data fault :: No known Actor has been moved.
                // Should be virtually impossible. Still consider controlling
                Gdx.app.log("GameServerListener", "Entered 'else'");
            }
        }
    }
}
