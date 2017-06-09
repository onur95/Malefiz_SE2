package com.mygdx.malefiz.tests;

import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.networking.GameServer;
import com.mygdx.malefiz.Malefiz;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkTests {
    private int tcpPort = 45455;
    private int udpPort = 45456;
    private int timeout = 10000;
    private int maxUserCount = 4;
    private GameServer server;
    private GameClient client;
    private Malefiz game;

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
        client.connect("");

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
            clients[i].connect("");
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
