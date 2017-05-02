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
            client.start();

            if(GameServer.players <= GameServer.max_usercount){
                Gdx.app.log("Client", "Connected.");
                client.connect(TIMEOUT, serverIP, TCP_PORT, UDP_PORT);
                GameServer.players++;
                client.addListener(new GameClientListener());
            }else{
                Gdx.app.log("Client", "Closing client :: Server Full");
                client.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void terminate(){
        client.stop();
        client.close();
        Gdx.app.log("Client","Disconnected.");
    }


    public void sendData(){

        Network.ClientMessage transmission = new Network.ClientMessage();

        /* TODO: Take Data from current turn -- Currently only on the current player .. **
        transmission.actorIndex = x.actorIndex;
        transmission.column = x.column;
        transmission.row = x.row;
        */

        client.sendTCP(transmission);       // ** .. Send it to server
        Gdx.app.log("Client","Transmitted Data.");
    }
}