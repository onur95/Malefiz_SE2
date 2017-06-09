package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class GameServerListener extends Listener {
    private GameServer server;

    public GameServerListener(GameServer server) {
        super();
        this.server = server;
    }

    @Override
    public void received(Connection connection, Object object) {
        // Received object from connection

        if (object instanceof Network.ClientMessage) {
            // Parse Data to use it.
            Network.ClientMessage clientTransmission = (Network.ClientMessage) object;
            server.sendMessage(clientTransmission.update, clientTransmission.playerTurn);
            //message Senden mit clientTransmission
        }
    }

    @Override
    public void connected(Connection connection){
        server.addClient(connection);
    }

    @Override
    public void disconnected(Connection connection){
        server.sendMessage(server.removeClient(connection)+1);
    }


}
