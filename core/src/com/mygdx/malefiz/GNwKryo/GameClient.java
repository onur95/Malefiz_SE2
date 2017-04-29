package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/**
 * Created by tom on 29.04.17.
 */

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
            Log.debug("Client verbindet.");
            client.connect(TIMEOUT, serverIP, TCP_PORT, UDP_PORT);
            client.addListener(new GameClientListener());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        Log.debug("Client hält.");
        client.stop();
    }

}
