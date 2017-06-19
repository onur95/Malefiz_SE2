package com.mygdx.malefiz.tests;

import com.mygdx.malefiz.Malefiz;
import com.mygdx.malefiz.networking.GameClient;
import com.mygdx.malefiz.networking.GameServer;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkTests {
    private int tcpPort = 45455;
    private int udpPort = 45456;
    private int maxUserCount = 4;
    private GameServer server;

    @Test
    public void createAndStopServer() throws IOException{
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();
        server.stopServer();
    }

    @Test
    public void createClientAndConnect() throws IOException{
        int timeout = 10000;
        Malefiz game = new Malefiz();
        server = new GameServer(tcpPort, udpPort, maxUserCount);
        server.startServer();

        GameClient client = new GameClient(tcpPort, udpPort, timeout, game, server);
        client.connect("");

        client.terminate();
        server.stopServer();
    }

}
