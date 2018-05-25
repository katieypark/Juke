package com.example.nayhakamboj.jukebox.server;

import com.example.nayhakamboj.jukebox.serverCommands.ServerReply;
import com.example.nayhakamboj.jukebox.serverCommands.addHostCommand;
import com.example.nayhakamboj.jukebox.serverCommands.addUserCommand;
import com.example.nayhakamboj.jukebox.serverCommands.connectCommand;
import com.example.nayhakamboj.jukebox.serverCommands.copyPlaylistCommand;
import com.example.nayhakamboj.jukebox.serverCommands.createPlaylistCommand;
import com.example.nayhakamboj.jukebox.serverCommands.followUserCommand;
import com.example.nayhakamboj.jukebox.serverCommands.getFollowingCommand;
import com.example.nayhakamboj.jukebox.serverCommands.getPlaylistCommand;
import com.example.nayhakamboj.jukebox.serverCommands.getPlaylistsCommand;
import com.example.nayhakamboj.jukebox.serverCommands.loginCommand;
import com.example.nayhakamboj.jukebox.serverCommands.removeHostCommand;
import com.example.nayhakamboj.jukebox.serverCommands.removePlaylistCommand;
import com.example.nayhakamboj.jukebox.serverCommands.userExistsCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServerThread extends Thread {
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Server server;
	private DatabaseConnector dc;
	
	ServerThread(Socket s, Server server) {
		try {
			this.server = server;
			dc = new DatabaseConnector();
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	
	
	@Override
	public void run() {
		while(true) { 
			try {
				// read in object
				Object o = ois.readObject();
				// act on message
				if (o instanceof addUserCommand) {
					sendMessage(((addUserCommand) o).execute(dc));
				}
				else if (o instanceof followUserCommand) {
					sendMessage(((followUserCommand) o).execute(dc));
				}
				else if (o instanceof getFollowingCommand) {
					sendMessage(((getFollowingCommand) o).execute(dc));
				}				
				else if (o instanceof getPlaylistCommand) {
					sendMessage(((getPlaylistCommand) o).execute(dc));
				}
				else if (o instanceof getPlaylistsCommand) {
					sendMessage(((getPlaylistsCommand) o).execute(dc));
				}
				else if (o instanceof loginCommand) {
					sendMessage(((loginCommand) o).execute(dc));
				}
				else if (o instanceof removePlaylistCommand) {
					sendMessage(((removePlaylistCommand) o).execute(dc));
				}
				else if (o instanceof userExistsCommand) {
					sendMessage(((userExistsCommand) o).execute(dc));
				}
				else if (o instanceof copyPlaylistCommand) {
					sendMessage(((copyPlaylistCommand) o).execute(dc));
				}
				else if (o instanceof createPlaylistCommand) {
					sendMessage(((createPlaylistCommand) o).execute(dc));
				}
				else if (o instanceof connectCommand) {
					boolean contained = server.contains(((connectCommand) o).getUsername());
					if (!contained) // if not in activeHosts list
						sendMessage(new ServerReply("Bool", false));
					else { // send Host object associated with username in return
						sendMessage(new ServerReply("Host", server.getHost(((connectCommand) o).getUsername())));
					}
				}
				else if (o instanceof addHostCommand) {
					server.addHost(((addHostCommand) o).getUsername(),((addHostCommand) o).getHostname(),((addHostCommand) o).getPort());
					sendMessage(new ServerReply("Bool", true));
				}
				else if (o instanceof removeHostCommand) {
					server.removeHost(((removeHostCommand) o).getUsername());
					sendMessage(new ServerReply("Bool", true));
				}
				else { // If none of these messages received
					sendMessage(new ServerReply("Error", o)); // return object
				}
			} catch (ClassNotFoundException cnfe) {
				System.out.println("cnfe: " + cnfe.getMessage());
			} catch (IOException ioe) {
				// System.out.println("client disconnected");
				// Do nothing
			}
		}
	}

	// Send a message on the output stream
	private void sendMessage(Object object) {
		try {
			oos.reset();
			oos.writeObject(object);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	
}
