package com.mygdx.malefiz.sauce.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private Network() {
        //Smell: Add a private constructor to hide the implicit public one
    }

    // Register Datatypes to transmit -- Endpoint = Server or Client
    public static void registerKryoClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ClientMessage.class);
        kryo.register(ServerEcho.class);
        kryo.register(PlayerDisconnected.class);
        kryo.register(StartClient.class);
        kryo.register(ArrayList.class); //sonst Exception beim Senden im Client (ArrayList not registered)
        kryo.register(BoardUpdate.class); //sonst Exception beim Senden im Client (BoardUpdate not registered)

    }

    // Transmission from Client to Server
    public static class ClientMessage {
        int playerTurn;
        List<BoardUpdate> update;
    }

    public static class PlayerDisconnected {
        int player;
    }

    public static class StartClient {
        int player;
        int playerCount;
    }

    // Transmission from Server to client
    public static class ServerEcho {
        int playerTurn;
        int playerTurnBefore;
        List<BoardUpdate> update;
    }

}