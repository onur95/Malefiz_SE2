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
import java.net.SocketException;
import java.util.Enumeration;

/*
Gschickt werdn drei klassn und de spielernummer
Klasse besteht aus:
Actorindex, column, row
*/

public class GameNetwork extends Thread {
    // Serverside
    // TODO: Update Data-Transfer of Field/Turn/etc.
    public static void initServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Server + Settings
                ServerSocketHints sshS = new ServerSocketHints();
                sshS.backlog = 4;
                sshS.acceptTimeout = 0; // No timeout
                ServerSocket host = Gdx.net.newServerSocket(Net.Protocol.TCP, "localhost", 7454, sshS);

                // Serverside Client-Setup
                SocketHints shC = new SocketHints();
                Socket client = host.accept(shC);
    // Commitcomment

                // Running the server
                    while (true) {
                        try {
                            // Fetch data of the inputStream by client accepted by Server
                            String datatransfer = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                            Gdx.app.log("Host", "Received Transfer: " + datatransfer);

                            // Send data to outputstream of client.
                            client.getOutputStream().write(/*New Gamefield.getBytes()*/null);

                            /* Alt.:
                            byte[] readBuffer = new byte[1024];
                            socket.getInputStream().read(read);
                            UpdateField();

                            String playfield = "";  // Insert updated Field
                            String sendOut = new String(playfield).trim();
                            socket.getOutputStream().write(readString.getBytes());
                             */

                        } catch (IOException e) {
                            Gdx.app.log("Error in GameNetwork", "Error in Serverside", e);
                        }
                    }
            }
        }).start();
    }

    //Clientside
    public static void initClient() {
        String host = "";
        int port = 0;
        SocketHints sshC = new SocketHints();

        //TODO :: Entry for serverinformation

        Socket client = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, sshC);

        try {
            client.getOutputStream().write(/*Send move to server*/null);
            String serverresponse = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();

            Gdx.app.log("Client", "Received Transfer: " + serverresponse);
        } catch (IOException e) {
            Gdx.app.log("Error in GameNetwork", "Error in Clientside", e);
        }
    }


    // TODO: Call this in Textfield of Submenu in Main
    // We need to distribute Information to clients. Do so via sharing information person to person.
    public String[] fetchServerInfo() {
        String[] serverInfo = new String[2];
        // 0 = Port; 1 = private ip; 2 = public ip
        serverInfo[0] = "7454"; // Port

        String ipAddress = "";
        // This is common in most tutorials, thus implemented that way aswell.
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> enumInetAddress = null;
            // <=> Set enum for all acceptable Networkinterfaces ..
            while (enumNetworkInterfaces.hasMoreElements()) {   // ... as long as there are more NetworkInterfaces
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) { // .. as long as there are more passable adresses.
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) { // If the add has that local address,
                        serverInfo[1] = " Private IP Address :: " + ipAddress + "\n";
                        serverInfo[2] = " IP Address :: " + inetAddress.getHostAddress() + "\n"; // Give us the remote Host-IP
                        /* Alternative
                        InetAddress addy = socket.getInetAddress();
                        String remoteIp = addy.getHostAddress();
                         */
                    }
                }
            }
        } catch (SocketException e) {e.printStackTrace(); ipAddress = ipAddress + "Error in getIpAddress() ::" + e.toString() + "\n";}
        return serverInfo;
    }


    private void updateParams(){
        // TODO :: Set playfield for Server & thus for all following clients

    }

    private byte[] getParams(){
        // TODO :: Take params; (put them to Byte and) hand it to client/server

        return null;
    }
}

