package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.malefiz.BoardUpdate;
import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.UpdateHandler;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private int TCP_PORT, UDP_PORT, TIMEOUT, player;
    private static final Logger LOGGER = Logger.getLogger( GameClient.class.getName() );
    private Client client;
    private UpdateHandler handler;
    private Malefiz game;
    private String serverIp;

    public GameClient(int tcp, int udp, int timeout, Malefiz game){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        this.TIMEOUT = timeout;
        this.game = game;

        client = new Client();
        Network.registerKryoClasses(client);
    }

    public void connect(String serverIP){
        try{
            //Starts Client
            client.start();
            client.connect(TIMEOUT, serverIP, TCP_PORT, UDP_PORT);

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

    // TODO: MUST HAPPEN somewhere. Otherwise Client/Server remains active
    public void terminate(){
        client.close();
        //Gdx.app.log("Client","Disconnected.");
    }

    public void setPlayerNumber(int player){
        this.player = player;
    }


    // Create message sent to the server.
    // We can send virtually everything, if so: Adjust in Network.ClientMessage & register it as attributes above.
    // TODO: This needs a call after player picked a position, thus sending the "new field" to server
    // Data sent in transmission <=> Params processed in GameServerListener
    public void sendData(List<BoardUpdate> update, int playerTurn){
        Network.ClientMessage transmission = new Network.ClientMessage();
        transmission.update = update;
        transmission.playerTurn = playerTurn;

        client.sendTCP(transmission);       // ** .. Send it to server
//        Gdx.app.log("Client","Transmitted Data.");
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
