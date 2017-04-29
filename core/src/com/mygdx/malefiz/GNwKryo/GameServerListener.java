package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.malefiz.Board;
import com.mygdx.malefiz.BoardToPlayboard;
import com.mygdx.malefiz.BoardUpdate;
import com.mygdx.malefiz.FieldPosition;
import com.mygdx.malefiz.MyMalefizGame;


public class GameServerListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        // Check what is sent, then do x

        if(object instanceof Network.ClientMessage){
            Network.ClientMessage clientTransmission = (Network.ClientMessage) object;

            // TODO: Setter for updated Gamefield
            /* Something similar to:
            if(x.actorIndex == clientTransmission.actorIndex){
                x.column = clientTransmission.column;
                x.row = clientTransmission.row;
            }else{
                // Data fault :: No known Actor has been moved.
                // Should be virtually impossible. Still consider controlling
            }
            */

        }
    }
}
