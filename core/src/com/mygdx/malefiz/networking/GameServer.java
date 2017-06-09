package com.mygdx.malefiz.networking;


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
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameServer {
    private int TCPPort;
    private int UDPPort;
    private static final Logger LOGGER = Logger.getLogger( GameServer.class.getName() );
    private Server server;
    private int maxUsercount;
    private int players = 0;           // Aktuelle Anzahl an Spielern im Spiel
    private List<Connection> clients;
    private List<Integer> leavedPlayers;
    private boolean gameStarted = false;
    private int lastTurn = 1; //wird gebraucht, damit der nächste Spieler dran kommt, falls der Spieler vor ihm das Spiel verlässt

    public GameServer(int tcp, int udp, int maxUsercount){
        this.TCPPort = tcp;
        this.UDPPort = udp;
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
            server.bind(TCPPort, UDPPort);
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

    public String fetchPublicIP(){
        String ipAddress = "";

        // This is common in most tutorials, thus implemented that way aswell.
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> enumInetAddress;
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
        } catch (SocketException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            ipAddress = ipAddress + "Error in getIpAddress() ::" + e.toString() + "\n";
        }
        return ipAddress;
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
        LOGGER.log(Level.FINE, "Server: Transmitted Data to Clients");
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

        LOGGER.log(Level.FINE, "Server: Client connected; playercount="+players);

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
        LOGGER.log(Level.FINE, "Server: Client disconnected; clientIndex="+clientIndex);
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
            LOGGER.log(Level.FINE, "Server: All players have left the game. Server closed");
        }
        return clientIndex;
    }

    public int adjustPlayerTurn(int playerTurn){
        return playerTurn > maxUsercount ? 1 : playerTurn;
    }
}
