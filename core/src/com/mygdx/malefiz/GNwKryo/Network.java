package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.mygdx.malefiz.MyMalefizGame;

public class Network {
    // Register Datatypes to transmit -- Endpoint = Server or Client
    static public void registerKryoClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ClientMessage.class);
        kryo.register(ServerEcho.class);
    }

    // Transmission from Client to Server
    static public class ClientMessage {
        int actorIndex, column, row;
    }

    // Transmission from Server to client
    static public class ServerEcho {
        int actorIndex, column, row, playerTurn;
    }




}