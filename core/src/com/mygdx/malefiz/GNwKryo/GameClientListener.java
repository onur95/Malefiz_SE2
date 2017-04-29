package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mygdx.malefiz.BoardUpdate;
import com.mygdx.malefiz.Player;

public class GameClientListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Network.ServerEcho){
            Network.ServerEcho serverEcho = (Network.ServerEcho) object;

            //TODO: Doublecheck the logic. Somewhat certain that'd be correct
            /*if(serverEcho.actorIndex == x.actorIndex && (serverEcho.column != x.column || serverEcho.row != x.column)){
                x.column = serverEcho.column;
                x.row = serverEcho.row;
                Log.debug("Updated Playerfield on Client.")
            }
            */
            if(serverEcho.playerTurn == Player.getNumber()){
                // TODO: Set player as active Player
                Log.debug("Spieler #" + serverEcho.playerTurn +" an der Reihe");
            }
        }
    }
}
