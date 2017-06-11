package com.mygdx.malefiz.networking;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.UpdateHandler;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private int tcpPort;
    private int udpPort;
    private int timeout;
    private int player;
    private static final Logger LOGGER = Logger.getLogger( GameClient.class.getName() );
    private Client client;
    private UpdateHandler handler;
    private Malefiz game;
    private String serverIp;

    public GameClient(int tcp, int udp, int timeout, Malefiz game){
        this.tcpPort = tcp;
        this.udpPort = udp;
        this.timeout = timeout;
        this.game = game;

        client = new Client();
        Network.registerKryoClasses(client);
    }

    public void connect(String serverIP){
        try{
            //Starts Client
            client.start();
            client.connect(timeout, serverIP, tcpPort, udpPort);

            client.addListener(new GameClientListener(this));

            this.serverIp = serverIP;
        }catch(IOException e){
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public String getServerIp(){
        return this.serverIp;
    }

    public Malefiz getGame(){
        return this.game;
    }

    public void terminate(){
        client.close();
        LOGGER.log(Level.FINE, "Client: Client disconnected");
    }

    public void setPlayerNumber(int player){
        this.player = player;
    }


    // Create message sent to the server.
    // We can send virtually everything, if so: Adjust in Network.ClientMessage & register it as attributes above.
    // Data sent in transmission <=> Params processed in GameServerListener
    public void sendData(List<BoardUpdate> update, int playerTurn){
        Network.ClientMessage transmission = new Network.ClientMessage();
        transmission.update = update;
        transmission.playerTurn = playerTurn;

        client.sendTCP(transmission);       // ** .. Send it to server
        LOGGER.log(Level.FINE, "Client: Transmitted Data to Server");
    }

    public int getPlayerNumber(){
        return  this.player;
    }

    public void setHandler(UpdateHandler handler){
        this.handler = handler;
    }

    public UpdateHandler getHandler(){
        return this.handler;
    }
}
