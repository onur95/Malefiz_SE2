package com.mygdx.malefiz.Tests;

import com.mygdx.malefiz.GNwKryo.GameClient;
import com.mygdx.malefiz.GNwKryo.GameServer;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testng.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkTests {
    static protected String ip = "127.0.0.1";   // Emulating Server-IP always throws this
    static protected int tcpPort = 45455;
    static protected int udpPort = 45456;
    static protected int timeout = 10000;
    GameServer server;
    GameClient client;

    @Test
    public void createAndStopServer(){
        server = new GameServer(tcpPort, udpPort);
        server.startServer();
        server.stopServer();
    }

    @Test
    public void createClientAndConnect(){
        server = new GameServer(tcpPort, udpPort);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout);
        client.connect(ip);

        client.terminate();
        server.stopServer();
    }

    @Test
    public void maximumConnectionCount(){
        server = new GameServer(tcpPort, udpPort);
        server.startServer();

        int tc = 100;   // Don't do too many.
        GameClient clients[] = new GameClient[tc];

        for(int i = 0; i < tc; i++)
        {
            clients[i] = new GameClient(tcpPort, udpPort, timeout);
            clients[i].connect(ip);
        }
        // Maximum of 3 Connections allowed
        // Every other was successfully shut down.
        for(int i = tc; i <= 0; i--)
        {
            clients[i].terminate();
        }
        server.stopServer();
    }

    @Test
    public void sendMessageClientToServer(){
        server = new GameServer(tcpPort, udpPort);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout);
        client.connect(ip);

        client.sendData(3, 2, 2);
    }

    @Test
    public void sendMessageServerToClient(){
        server = new GameServer(tcpPort, udpPort);
        server.startServer();

        client = new GameClient(tcpPort, udpPort, timeout);
        client.connect(ip);

        server.sendMessage(3, 2, 2, 2);
    }
}
