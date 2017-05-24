package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.malefiz.BoardUpdate;
import com.mygdx.malefiz.Screens.ConfigureScreen;

import org.omg.CORBA.Context;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GameServer {
    private int TCP_PORT, UDP_PORT;
    Server server;
    private int max_usercount = 3; // TODO: Change von Textfield Anzahl holen.
    private int players = 0;           // Aktuelle Anzahl an Spielern im Spiel
    private List<Connection> clients;

    public GameServer(int tcp, int udp, int max_usercount){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        clients = new ArrayList<Connection>(4);
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
        transmission.playerTurn = playerturn;

        server.sendToAllTCP(transmission); // Sends created message to all connected devices.
//        Gdx.app.log("GameServer.sendMessage()", "Message sent to all clients.");
    }

    public void addClient(Connection connection){
        System.out.println("Client connected");
        players++;
        if(players == max_usercount){
            for(int i=1; i<=players; i++) {
                clients.get(i - 1).sendTCP(i);
            }
        }
        else if(players > max_usercount){
            connection.sendTCP(-1); //close; Spieleranzahl erreicht
        }
        else{
            clients.add(connection);
        }
    }

    public int getMax_usercount(){
        return this.max_usercount;
    }


    public void setPlayerCount(int n){
        this.max_usercount = n;
    }
}
