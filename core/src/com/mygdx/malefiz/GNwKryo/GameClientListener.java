package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.malefiz.Player;

/**
 * Created by tom on 29.04.17.
 */

public class GameClientListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Network.ServerEcho){
            Network.ServerEcho serverEcho = (Network.ServerEcho) object;

            //TODO: Doublecheck the logic. Somewhat certain that'd be correct
            /*if(serverEcho.actorIndex == x.actorIndex && (serverEcho.column != x.column || serverEcho.row != x.column)){
                x.column = serverEcho.column;
                x.row = serverEcho.row;
            }
            */
            if(serverEcho.playerTurn == Player.getNumber()){
                // TODO: Set player as active Player
            }
        }
    }
}
