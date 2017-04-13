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


/// TODO: Update Data-Transfer of Field/Turn/etc.
/// Yet to Test ! -- This better works.

public class GameNetwork {
    // Serverside
    public static void initServer(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocketHints sshS = new ServerSocketHints();
                ServerSocket host = Gdx.net.newServerSocket(Net.Protocol.TCP, "localhost", 7331, sshS);

                Socket client = host.accept(null);
                try{
                    String datatransfer = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                    Gdx.app.log("Host", "Received Transfer: " + datatransfer);
                    client.getOutputStream().write(/*New Gamefield.getBytes()*/null);
                }catch(IOException e){
                    Gdx.app.log("Error in GameNetwork", "Error in Serverside", e);
                }
            }
        }).start();

    }

    public static void initClient(){
        //Clientside
        SocketHints sshC = new SocketHints();
        Socket client = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 7331, sshC);
        try{
            client.getOutputStream().write(/*New Gamefield.getBytes()*/null);
            String serverresponse = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Gdx.app.log("Client", "Received Transfer: " + serverresponse);
        }catch(IOException e){
            Gdx.app.log("Error in GameNetwork", "Error in Clientside", e);
        }
    }
}

