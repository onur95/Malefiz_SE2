package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    // Register Datatypes to transmit -- Endpoint = Server
    static public void registerKryoClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ClientMessage.class);
        kryo.register(ServerEcho.class);
    }

    // Transmission from Client
    static public class ClientMessage {
        int actorIndex, column, row;
    }

    // Transmission from Server
    static public class ServerEcho {
        int actorIndex, column, row, playerTurn;
    }



}