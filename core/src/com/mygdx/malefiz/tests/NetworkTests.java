package com.mygdx.malefiz.tests;

import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.networking.GameServer;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkTests {
    private int tcpPort = 45455;
    private int udpPort = 45456;
    private int timeout = 10000;
    private int maxUserCount = 4;
    private GameServer server;
    private GameClient client;
    private Malefiz game = new Malefiz();

    @Test
    public void createAndStopServer() throws IOException{
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();
        server.stopServer();
    }

    @Test
    public void createClientAndConnect() throws IOException{
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout, game, server);
        client.connect("");

        client.terminate();
        server.stopServer();
    }

    @Test
    public void maximumConnectionCount() throws IOException{
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        int testcases = 10;                                  //*** Absolutely horrifying when 100
        GameClient[] clients = new GameClient[testcases];

        for (int i = 0; i < testcases; i++) {
            clients[i] = new GameClient(tcpPort, udpPort, timeout, game, server);
            clients[i].connect("");
        }

        for (int i = 0; i < testcases; i++) {
            clients[i].terminate();
        }
            // stopServer Redundant: Once the last one disconnects, the server shuts down automatically
    }

}
