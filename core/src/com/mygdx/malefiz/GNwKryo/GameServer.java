package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GameServer {
    private int TCP_PORT, UDP_PORT;
    Server server;
    protected static final int max_usercount = 3;
    protected static int players = 0;

    public GameServer(int tcp, int udp){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        server = new Server();

        // Assign all datatransfer classes to server.
        Network.registerKryoClasses(server);

    }

    public void startServer(){
        Gdx.app.log("Server","Startet.");
        server.start();
        server.addListener(new GameServerListener());
        try{
            server.bind(TCP_PORT, UDP_PORT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // TODO: Needs a call once game is aborted
    public void stopServer(){
        Gdx.app.log("Server","Stopt.");
        server.stop();
        server.close();
    }

    public static String fetchPublicIP(){
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
    public void sendMessage(int actorindex, int column, int row, int playerturn){
        // Build Transmission from Server. Send @Par is necessary - Featured in Network.ServerEcho
        Network.ServerEcho transmission = new Network.ServerEcho();

        transmission.actorIndex = actorindex;
        transmission.column = column;
        transmission.row = row;
        transmission.playerTurn = playerturn;

        server.sendToAllTCP(transmission); // Sends created message to all connected devices.
        Gdx.app.log("GameServer.sendMessage()", "Message sent to all clients.");
    }
}
