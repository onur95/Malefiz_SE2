package com.mygdx.malefiz.GNwKryo;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

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

    public void stopServer(){
        Gdx.app.log("Server","Stopt.");
        server.stop();
    }
}
