package com.mygdx.malefiz.sauce.networking;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameServer {
    private int tcpPort;
    private int udpPort;
    private static final Logger LOGGER = Logger.getLogger( GameServer.class.getName() );
    private Server server;
    private int maxUsercount;
    private int players = 0;           // Aktuelle Anzahl an Spielern im Spiel
    private List<Connection> clients;
    private List<Integer> leavedPlayers;
    private boolean gameStarted = false;
    private int lastTurn = 1; //wird gebraucht, damit der nächste Spieler dran kommt, falls der Spieler vor ihm das Spiel verlässt

    public GameServer(int tcp, int udp, int maxUsercount){
        this.tcpPort = tcp;
        this.udpPort = udp;
        clients = new ArrayList<>(4);
        leavedPlayers = new ArrayList<>(4);
        server = new Server();
        this.maxUsercount = maxUsercount;

        // Assign all datatransfer classes to server.
        Network.registerKryoClasses(server);

    }

    public void startServer(){
        server.start();
        server.addListener(new GameServerListener(this));
        try{
            server.bind(tcpPort, udpPort);
        }catch(IOException e){
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }


    public void stopServer(){
        players = 0;   // Necessary for shutdown + windup. Bugs for some reason
        server.close();
        clients.clear();
        leavedPlayers.clear();
    }

    public void sendMessage(List<BoardUpdate> update, int playerturn){
        // Build Transmission from Server. Send @Par is necessary - Featured in Network.ServerEcho
        Network.ServerEcho transmission = new Network.ServerEcho();

        transmission.update = update;
        transmission.playerTurnBefore = playerturn;
        int newPlayerturn = nextPlayer(playerturn);

        transmission.playerTurn = newPlayerturn;
        lastTurn = newPlayerturn;

        server.sendToAllTCP(transmission); // Sends created message to all connected devices.
        LOGGER.log(Level.INFO, "Server: Transmitted Data to Clients");
    }

    public void sendMessage(int player){
        Network.PlayerDisconnected playerDisconnected = new Network.PlayerDisconnected();
        playerDisconnected.player = player;
        server.sendToAllTCP(playerDisconnected);
    }

    public void addClient(Connection connection){
        if(players < maxUsercount) {
            clients.add(connection);
        }

        players++;

        LOGGER.log(Level.INFO, "Server: Client connected; playercount="+players);

        if(players == maxUsercount){
            for(int i=1; i<=players; i++) {
                Network.StartClient startClient = new Network.StartClient();
                startClient.player = i;
                startClient.playerCount = players;
                clients.get(i - 1).sendTCP(startClient);
            }
            gameStarted = true;
        }
        else if(players > maxUsercount){
            Network.StartClient startClient = new Network.StartClient();
            startClient.player = -1;
            connection.sendTCP(startClient); //close; Spieleranzahl bereits erreicht
        }
    }

    private int nextPlayer(int playerTurn){
        int newPlayerTurn = adjustPlayerTurn(playerTurn+1);
        if(!leavedPlayers.isEmpty() && leavedPlayers.size() <= 3){
            while(leavedPlayers.contains(newPlayerTurn)){
                newPlayerTurn = adjustPlayerTurn(newPlayerTurn+1);
            }
        }
        return newPlayerTurn;
    }

    public int removeClient(){
        int clientIndex = -1;
        for(int i = 0; i < clients.size(); i++){
            if(!clients.get(i).isConnected()){
                clientIndex = i;
                break;
            }
        }
        LOGGER.log(Level.INFO, "Server: Client disconnected; clientIndex="+clientIndex);
        if(gameStarted && clientIndex != -1){
            leavedPlayers.add(clientIndex);
            if(lastTurn == clientIndex+1){
                sendMessage(null,lastTurn);
            }
        }
        else if(clientIndex != -1){ //Spiel noch nicht gestartet
            clients.remove(clientIndex);
            players--;
            //playerTurn vor dem schicken anpasssen

        }
        if(players == 0 && gameStarted){
            stopServer();
            LOGGER.log(Level.INFO, "Server: All players have left the game. Server closed");
        }
        return clientIndex;
    }

    public int adjustPlayerTurn(int playerTurn){
        return playerTurn > maxUsercount ? 1 : playerTurn;
    }
}
