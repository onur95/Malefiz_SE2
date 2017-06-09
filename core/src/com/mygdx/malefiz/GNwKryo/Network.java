package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.mygdx.malefiz.BoardUpdate;

import java.util.ArrayList;
import java.util.List;

public class Network {
    // Register Datatypes to transmit -- Endpoint = Server or Client
    static public void registerKryoClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ClientMessage.class);
        kryo.register(ServerEcho.class);
        kryo.register(PlayerDisconnected.class);
        kryo.register(StartClient.class);
        kryo.register(ArrayList.class); //sonst Exception beim Senden im Client (ArrayList not registered)
        kryo.register(BoardUpdate.class); //sonst Exception beim Senden im Client (ArrayList not registered)

    }

    // Transmission from Client to Server
    static public class ClientMessage {
        int playerTurn;
        List<BoardUpdate> update;
    }

    static public class PlayerDisconnected {
        int player;
    }

    static public class StartClient {
        int player, playerCount;
    }

    // Transmission from Server to client
    static public class ServerEcho {
        int playerTurn, playerTurnBefore;
        List<BoardUpdate> update;
    }




}