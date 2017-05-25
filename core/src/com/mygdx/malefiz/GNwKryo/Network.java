package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.mygdx.malefiz.BoardUpdate;
import com.mygdx.malefiz.MyMalefizGame;

import java.util.List;

public class Network {
    // Register Datatypes to transmit -- Endpoint = Server or Client
    static public void registerKryoClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ClientMessage.class);
        kryo.register(ServerEcho.class);
    }

    // Transmission from Client to Server
    static public class ClientMessage {
        int playerTurn;
        List<BoardUpdate> update;
    }

    // Transmission from Server to client
    static public class ServerEcho {
        int playerTurn, playerTurnBefore;
        List<BoardUpdate> update;
    }




}