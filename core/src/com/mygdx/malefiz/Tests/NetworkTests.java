package com.mygdx.malefiz.Tests;

import com.mygdx.malefiz.GNwKryo.GameClient;
import com.mygdx.malefiz.GNwKryo.GameServer;
import com.mygdx.malefiz.Malefiz;

<<<<<<< HEAD
=======
import org.junit.Test;
>>>>>>> origin/master
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkTests {
    static protected String ip = "127.0.0.1";   // Emulating Server-IP always throws this
    static protected int tcpPort = 45455;
    static protected int udpPort = 45456;
    static protected int timeout = 10000;
    int maxUserCount = 4;
    GameServer server;
    GameClient client;
    Malefiz game;

    @Test
    public void createAndStopServer(){
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();
        server.stopServer();
    }

    @Test
    public void createClientAndConnect(){
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout,game);
        client.connect(ip);

        client.terminate();
        server.stopServer();
    }

    @Test
    public void maximumConnectionCount(){
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        int testcases = 5;                                  //*** Absolutely horrifying when 100
        GameClient clients[] = new GameClient[testcases];

        for(int i = 0; i < testcases; i++){
            clients[i] = new GameClient(tcpPort, udpPort, timeout, game);
            clients[i].connect(ip);
        }

        for(int i = testcases; i == 0; i--){
            clients[i].terminate();
        }
        // server.stopServer(); Redundant: Once the last one disconnects, the server shuts down automatically
    }

    /*
    @Test
    public void sendMessageClientToServer(){
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout, game);
        client.connect(ip);

       // client.sendData(); >> CheatEngine. NOW.
    }

    @Test
    public void sendMessagServerToClient(){
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout, game);
        client.connect(ip);

        //server.sendMessage();
    }
*/
}
