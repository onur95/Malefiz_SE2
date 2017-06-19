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
    public void createAndStopServer(){
        boolean valid;
        try {
            server = new GameServer(tcpPort, udpPort, maxUserCount);
            server.startServer();
            server.stopServer();
            valid = true;
        }
        catch (Exception ex){
            valid = false;
        }
        assertEquals(valid, true);
    }

    @Test
    public void createClientAndConnect() {
        boolean valid;
        try {
            server = new GameServer(tcpPort, udpPort, maxUserCount);
            server.startServer();

            client = new GameClient(tcpPort, udpPort, timeout, game, server);
            client.connect("");

            client.terminate();
            server.stopServer();
            valid = true;
        }
        catch (Exception ex){
            valid = false;
        }
        assertEquals(valid, true);

    }

    @Test
    public void maximumConnectionCount(){
        boolean valid;
        try {
            server = new GameServer(tcpPort, udpPort, maxUserCount);
            server.startServer();

            int testcases = 5;                                  //*** Absolutely horrifying when 100
            GameClient[] clients = new GameClient[testcases];

            for (int i = 0; i < testcases; i++) {
                clients[i] = new GameClient(tcpPort, udpPort, timeout, game, server);
                clients[i].connect("");
            }

            for (int i = testcases - 1; i >= 0; i--) {
                clients[i].terminate();
            }
            valid = true;
            // stopServer Redundant: Once the last one disconnects, the server shuts down automatically
        }
        catch (Exception ex){
            valid = false;
        }
        assertEquals(valid, true);

    }

}
