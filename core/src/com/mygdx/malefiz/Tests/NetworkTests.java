package com.mygdx.malefiz.Tests;

import com.mygdx.malefiz.GNwKryo.GameClient;
import com.mygdx.malefiz.GNwKryo.GameServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkTests {
    static public String ip = "127.0.0.1";
    static public int tcpPort = 45455;
    static public int udpPort = 45456;
    static public int timeout = 10000;

    @Test
    public void createAServer(){
        GameServer server = new GameServer(tcpPort, udpPort);
        server.startServer();
    }

    @Test
    public void createClientAndConnect(){
        GameClient client = new GameClient(tcpPort, udpPort, timeout);
        client.connect(ip);
    }

    @Test
    public void testMaximumConnectionCount(){
        int tc = 100;   // Don't do too many.
        GameClient clients[] = new GameClient[tc];

        for(int i = 0; i < tc; i++)
        {
            clients[i] = new GameClient(tcpPort, udpPort, timeout);
            clients[i].connect(ip);
        }
        // Only 3 Connections allowed. First connection in Test before, 2 more allowed.
        // Every other was successfully shut down.
    }

}
