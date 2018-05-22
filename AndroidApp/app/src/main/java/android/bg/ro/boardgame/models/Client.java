package android.bg.ro.boardgame.models;

import android.app.Activity;
import android.app.Fragment;
import android.bg.ro.boardgame.ChatActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.ChatMessage;
import android.bg.ro.boardgame.fragments.ChatFragment;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.util.*;


public class Client {

	private String notif = " *** ";

	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;					// socket object

	private String server, username;	// server and username
	private int port;					//port
	private Fragment fragment;
	private Activity activity;
	private String message;

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Client(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}

	public boolean start() {
		try {
			socket = new Socket(server, port);
		}
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);

		/* Creating both Data Stream */
		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		new ListenFromServer().start();
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		return true;
	}

	private void display(String msg) {
		System.out.println(msg);
	}

	public void sendMessage(String msg) {
		try {
            System.setProperty("http.keepAlive", "false");
            sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	public void disconnect() {
		try {
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {}
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {}

	}

	/*
	 * a class that waits for the message from the server
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					message = (String) sInput.readObject();
					message = message.substring(8).substring(message.indexOf(":") + 2);
					if(activity instanceof ChatActivity){

						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {

								ChatMessage chatMessage = new ChatMessage();
								chatMessage.setMessage(message);

								Date date = new Date();
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(date);
								chatMessage.setDate(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
								chatMessage.setMe(true);
								((ChatActivity) activity).displayMessage(chatMessage);
							}
						});
					}
				}
				catch(IOException e) {
					display(notif + "Server has closed the connection: " + e + notif);
					break;
				}
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}

