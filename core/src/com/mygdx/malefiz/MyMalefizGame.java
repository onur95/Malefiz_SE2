package com.mygdx.malefiz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.text.View;


public class MyMalefizGame extends ApplicationAdapter {
	private static Stage stage;
	Texture txt_playground;
	Image img_playground;

	// Networkingcomponents - Server
	// Unless fit into 1 class, we might have a problem with multiple
	// instances of these. Probable cleanup at a later time.
	ServerSocket serverSocket;
	static final int SocketServerPORT = 7331;	// {2000 .. 9999}
	private Socket hostThreadSocket;
	String ipAddress = "";
	// Networkingcomponents - Client
	Socket socket = null;
	String destinationAddress;
	int destinationPort;
	// We need effective tradeoff between Server and Client-Components.

	@Override
	public void create () {
		stage=new Stage(new FitViewport(1500,1498));
		Gdx.input.setInputProcessor(stage);
		txt_playground = new Texture("Playboard.jpg");
		img_playground=new Image(txt_playground);

		// Todo: For better performance, try to divide Server & Clientside properly
		// -- Client does not need to run own server but to connect to one.
		//Serverside:
//		Thread hostSocketThread = new Thread(new SocketServerThread());
//		hostSocketThread.start();
		//Clientside:
		// Impl.: Button to connect ::   b_Connect = (Button)findViewById(R.id.connect);
		// Then:
		//b_Connect.setOnClickListener(buttonConnectOnClickListener);


		Board.init();
		BoardToPlayboard.init();
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
		stage.addActor(img_playground);
		BoardToPlayboard.generate();
	}


	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	public static Stage getState(){
		return stage;
	}


	@Override
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () {
		stage.dispose();

/*		// TODO :: Close ServerSocket accordingly
		if (serverSocket != null) {
			try {
				serverSocket.close();  // Does not work for some reason
			} catch (IOException e) {System.out.println("Error in onDestroy() :: "); e.printStackTrace();}
		}
*/	}


/* START OF COMMENT :: NETWORKING

	// TODO :: Serverside-cleanup
	private class SocketServerThread extends Thread {

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(SocketServerPORT);
				MyMalefizGame.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO :: Upon start, do ...
					}
				});

				while (true) {                                  // Set later to "While not playing game" >> Deactivate permanent search for clients
					Socket socket = serverSocket.accept();

					MyMalefizGame.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO :: While running, update the game
						}
					});
					SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket, count);
					socketServerReplyThread.run();

				}
			} catch (IOException e) {System.out.println("Error in run() :: ");e.printStackTrace();}
		}

	}

	private class SocketServerReplyThread extends Thread {
		SocketServerReplyThread(Socket socket) {
			hostThreadSocket = socket;
			// TODO :: Send Data of gamefield
			// C :: Stream in run() below
		}

		@Override
		public void run() {
			OutputStream outputStream;

			try {   // Reply String msgReply to client accordingly via Stream
				outputStream = hostThreadSocket.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				printStream.print(gamefield);	// TODO :: Correction of this
				printStream.close();

				MyMalefizGame.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO: gamefield.setGamefield(new gamefield)
					}
				});

			} catch (IOException e) {e.printStackTrace();}

			MyMalefizGame.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO: Pass turn to next player
				}
			});
		}

	}

	private String getIpAddress() {
		String ipAddress = "Lokal";

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

						ipAddress = inetAddress.getHostAddress() + "\n"; // Give us the remote Host-IP

                        //InetAddress addy = socket.getInetAddress();
                        //String remoteIp = addy.getHostAddress();

						// DONT USE STRINGBUILDER; looks damn glued
					}
				}
			}
		} catch (SocketException e) {e.printStackTrace();}
		return ipAddress;
	}


	// TODO: Clientside-cleanup
	OnClickListener buttonConnectOnClickListener =
			new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					MyClientTask myClientTask = new MyClientTask(
							et_Address.getText().toString(),
							Integer.parseInt(ed_Port.getText().toString()));

					myClientTask.execute();
				}};

	// Subclass :: In order to control scripting & usage later on. Purpose : Fetch data
	public class MyClientTask extends AsyncTask<Void, Void, Void> {
		MyClientTask(String designated_Adress, int designated_port){
			destinationAddress = designated_Adress;
			destinationPort = designated_port;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				socket = new Socket(destinationAddress, destinationPort);

				Object Playfield;
				byte[] buffer = new byte[1024];

				int bytesRead;
				InputStream inputStream = socket.getInputStream();

				while ((bytesRead = inputStream.read(buffer)) != -1){
					byteArrayOutputStream.write(buffer, 0, bytesRead);
					response += byteArrayOutputStream.toString("UTF-8");
				}

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {e.printStackTrace();}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// TODO :: Send changes during turn
			gamefield.setField(new Gamefield);

			super.onPostExecute(result);
		}
	}

*/ // END OF COMMENT :: NETWORKING
}