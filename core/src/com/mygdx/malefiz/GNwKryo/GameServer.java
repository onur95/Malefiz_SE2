package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.malefiz.BoardUpdate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GameServer {
    private int TCP_PORT, UDP_PORT;
    private Server server;
    private int max_usercount;
    private int players = 0;           // Aktuelle Anzahl an Spielern im Spiel
    private List<Connection> clients;
    private List<Integer> leavedPlayers;
    private boolean gameStarted = false;
    private int lastTurn = 1; //wird gebraucht, damit der nächste Spieler dran kommt, falls der Spieler vor ihm das Spiel verlässt

    public GameServer(int tcp, int udp, int max_usercount){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        clients = new ArrayList<Connection>(4);
        leavedPlayers = new ArrayList<Integer>(4);
        server = new Server();
        this.max_usercount = max_usercount;

        // Assign all datatransfer classes to server.
        Network.registerKryoClasses(server);

    }

    public void startServer(){
//Line interferes with tests.        Gdx.app.log("Server","Startet.");
        server.start();
        server.addListener(new GameServerListener(this));
        try{
            server.bind(TCP_PORT, UDP_PORT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // TODO: Needs a call once game is aborted
    public void stopServer(){
        //Gdx.app.log("Server","Stopt.");
        players = 0;   // Necessary for shutdown + windup. Bugs for some reason
        server.close();
        clients.clear();
        leavedPlayers.clear();
    }

    public String fetchPublicIP(){
        String ipAddress = "";

        // This is common in most tutorials, thus implemented that way aswell.
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> enumInetAddress = null;
            // <=> Set enum for all acceptable Networkinterfaces ..
            while (enumNetworkInterfaces.hasMoreElements()) {   // ... as long as there are more NetworkInterfaces
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) { // .. as long as there are more passable adresses.
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) { // If the add has that local address,

                        ipAddress = "IP of device: " + inetAddress.getHostAddress() + "\n"; // Give us the remote Host-IP
                    }
                }
            }
        } catch (SocketException e) {e.printStackTrace(); ipAddress = ipAddress + "Error in getIpAddress() ::" + e.toString() + "\n";}
        return ipAddress;
    }

    // TODO: This call is necessary after update of Gamefield.
    public void sendMessage(List<BoardUpdate> update, int playerturn){
        // Build Transmission from Server. Send @Par is necessary - Featured in Network.ServerEcho
        Network.ServerEcho transmission = new Network.ServerEcho();

        transmission.update = update;
        transmission.playerTurnBefore = playerturn;
        playerturn = nextPlayer(playerturn);

        transmission.playerTurn = playerturn;
        lastTurn = playerturn;

        server.sendToAllTCP(transmission); // Sends created message to all connected devices.
//        Gdx.app.log("GameServer.sendMessage()", "Message sent to all clients.");
    }

    public void sendMessage(int player){
        Network.PlayerDisconnected playerDisconnected = new Network.PlayerDisconnected();
        playerDisconnected.player = player;
        server.sendToAllTCP(playerDisconnected);
    }

    public void addClient(Connection connection){
        System.out.println("Client connected");
        if(players < max_usercount) {
            clients.add(connection);
        }

        players++;
        if(players == max_usercount){
            for(int i=1; i<=players; i++) {
                Network.StartClient startClient = new Network.StartClient();
                startClient.player = i;
                startClient.playerCount = players;
                clients.get(i - 1).sendTCP(startClient);
            }
            gameStarted = true;
        }
        else if(players > max_usercount){
            Network.StartClient startClient = new Network.StartClient();
            startClient.player = -1;
            connection.sendTCP(startClient); //close; Spieleranzahl bereits erreicht
        }
    }

    public int getMax_usercount(){
        return this.max_usercount;
    }


    public void setPlayerCount(int n){
        this.max_usercount = n;
    }

    private int nextPlayer(int playerTurn){
        playerTurn = adjustPlayerTurn(playerTurn+1);
        if(leavedPlayers.size() != 0 && leavedPlayers.size() <= 3){
            while(leavedPlayers.contains(playerTurn)){
                playerTurn = adjustPlayerTurn(playerTurn+1);
            }
        }
        return playerTurn;
    }

    public int removeClient(Connection connection){
        Gdx.app.log("Server", "Client disconnected");
        int clientIndex = -1;
        for(int i = 0; i < clients.size(); i++){
            if(!clients.get(i).isConnected()){
                clientIndex = i;
                break;
            }
        }
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
//            Gdx.app.log("Server", "All Players left. Server closed");
            System.out.println("Server :: All players have left the game. Server closed.");
        }
        return clientIndex;
    }

    public int adjustPlayerTurn(int playerTurn){
        if(playerTurn > max_usercount){
            playerTurn = 1;
        }
        return playerTurn;
    }
}
