package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GameNetwork {
    // Serverside
    // TODO: Set maximum number of connecting persons
    // TODO: Set ssh invisible on maximum player count
    // TODO: Update Data-Transfer of Field/Turn/etc.

    public static void initServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocketHints sshS = new ServerSocketHints();
                ServerSocket host = Gdx.net.newServerSocket(Net.Protocol.TCP, "localhost", 7331, sshS);
                Socket client = host.accept(null);

                while (true) {
                    try {
                        String datatransfer = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                        Gdx.app.log("Host", "Received Transfer: " + datatransfer);
                        client.getOutputStream().write(/*New Gamefield.getBytes()*/null);
                    } catch (IOException e) {
                        Gdx.app.log("Error in GameNetwork", "Error in Serverside", e);
                    }
                }
            }
        }).start();

    }

    //Clientside
    // TODO:
    public static void initClient() {
        SocketHints sshC = new SocketHints();
        Socket client = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 7331, sshC);
        try {
            client.getOutputStream().write(/*New Gamefield.getBytes()*/null);
            String serverresponse = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Gdx.app.log("Client", "Received Transfer: " + serverresponse);
        } catch (IOException e) {
            Gdx.app.log("Error in GameNetwork", "Error in Clientside", e);
        }
    }


    /// TODO: Look for optional workouts for more dynamic arrangements
    /// Currently locked to 5 displayable Connections
    public String[] fetchIPAddresses() {
        String[] ipAddresses = new String[4];   // Up to 5 Strings in total, for the moment
        int count = 0;
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> enumInetAddress = null;

            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                enumInetAddress = networkInterface.getInetAddresses();

                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ipAddresses[count++] = inetAddress.getHostAddress();
                        if (count == 5) {
                            break;          // Otherwise ArrayOutOfBounds
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipAddresses;
    }
}

