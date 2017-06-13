package com.mygdx.malefiz.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.logging.Level;
import java.util.logging.Logger;


public class GameServerListener extends Listener {
    private GameServer server;
    private static final Logger LOGGER = Logger.getLogger( GameServerListener.class.getName() );

    public GameServerListener(GameServer server) {
        super();
        this.server = server;
    }

    @Override
    public void received(Connection connection, Object object) {
        LOGGER.log(Level.INFO, "Server: Message received");
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
        server.sendMessage(server.removeClient()+1);
    }


}
