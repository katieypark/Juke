package com.example.nayhakamboj.jukebox.client;


import android.util.Log;

import com.example.nayhakamboj.jukebox.server.Host;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerConnector {
	
	private Socket server;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ServerConnector(String hostname, int port) throws IOException {
        Log.d("debug","ServerConnector started");
		server = new Socket(hostname, port);
        Log.d("debug"," socket acquired");
        oos = new ObjectOutputStream(server.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(server.getInputStream());
        Log.v("debug","Hello");
    }
	
	public boolean login(String username, String password) {
		ServerReply reply = null;
		try {
			sendMessage(new loginCommand(username, password));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean createUser(String username, String password, String email) {
		ServerReply reply = null;
		try {
			sendMessage(new addUserCommand(username, password, email));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean addHost(String username, String hostname, int port) {
		ServerReply reply = null;
		try {
			sendMessage(new addHostCommand(username, hostname, port));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public Host connect(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new connectCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null && reply.getData() instanceof Host)
			return (Host) reply.getData();
		return null;
	}
	
	public boolean removeHost(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new removeHostCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Song> getPlaylist(String username, String playlistname) {
		ServerReply reply = null;
		try {
			sendMessage(new getPlaylistCommand(username, playlistname));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (List<Song>) reply.getData();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPlaylists(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new getPlaylistsCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (List<String>) reply.getData();
		return null;
	}
	
	public boolean removePlaylist(String username, String playlistname) {
		ServerReply reply = null;
		try {
			sendMessage(new removePlaylistCommand(username, playlistname));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean followUser(String currentUsername, String toFollowUsername) {
		ServerReply reply = null;
		try {
			sendMessage(new followUserCommand(currentUsername, toFollowUsername));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	List<String> getFollowing(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new getFollowingCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (List<String>) reply.getData();
		return null;
	}
	
	public boolean createPlaylist(String username, String playlistname, Playlist playlist) {
		List<PlaylistSong> playlistsongs = playlist.getPlayedSongs();
		ServerReply reply = null;
		try {
			sendMessage(new createPlaylistCommand(username, playlistname, playlistsongs));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			System.out.println("couldn't connect");
			
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean copyPlaylist(String currentUsername, String selectedUsername, String playlistname) {
		ServerReply reply = null;
		try {
			sendMessage(new copyPlaylistCommand(currentUsername, selectedUsername, playlistname));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
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
