package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class GameClient {
    private int TCP_PORT, UDP_PORT, TIMEOUT;
    private Client client;

    public GameClient(int tcp, int udp, int timeout){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        this.TIMEOUT = timeout;

        client = new Client();
        Network.registerKryoClasses(client);
    }

    public void connect(String serverIP){
        try{
            //Starts Client
            client.start();

            // Attempt to connect to server; Controlling via GameServer.var
            if(GameServer.players < GameServer.max_usercount){
// Line interferes with testing                Gdx.app.log("Client", "Connected.");
                client.connect(TIMEOUT, serverIP, TCP_PORT, UDP_PORT);
                GameServer.players++;
                client.addListener(new GameClientListener());
            }else{
// Line interferes with testing               Gdx.app.log("Client", "Closing client :: Server Full");
                client.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // TODO: MUST HAPPEN somewhere. Otherwise Client/Server remains active
    public void terminate(){
        client.close();
        //Gdx.app.log("Client","Disconnected.");
    }


    // Create message sent to the server.
    // We can send virtually everything, if so: Adjust in Network.ClientMessage & register it as attributes above.
    // TODO: This needs a call after player picked a position, thus sending the "new field" to server
    // Data sent in transmission <=> Params processed in GameServerListener
    public void sendData(int actorindex, int column, int row){
        Network.ClientMessage transmission = new Network.ClientMessage();

        transmission.actorIndex = actorindex;
        transmission.column = column;
        transmission.row = row;

        client.sendTCP(transmission);       // ** .. Send it to server
        Gdx.app.log("Client","Transmitted Data.");
    }
}
