package com.mygdx.malefiz.GNwKryo;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/**
 * Created by tom on 29.04.17.
 */

public class GameServer {
    private int TCP_PORT, UDP_PORT;
    Server server;


    public GameServer(int tcp, int udp){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        server = new Server();

        // Assign all datatransfer classes to server.
        Network.registerKryoClasses(server);

    }

    public void startServer(){
        Log.debug("Server start.");
        server.start();
        server.addListener(new GameServerListener());
        try{
            server.bind(TCP_PORT, UDP_PORT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void stopServer(){
        Log.debug("Server halt.");
        server.stop();
    }
}
