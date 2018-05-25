package com.example.nayhakamboj.jukebox.client;

import android.util.Log;

import com.example.nayhakamboj.jukebox.host.HostServer;
import com.example.nayhakamboj.jukebox.host.UpdateThread;
import com.example.nayhakamboj.jukebox.hostCommands.AddSongCommand;
import com.example.nayhakamboj.jukebox.hostCommands.DownvoteCommand;
import com.example.nayhakamboj.jukebox.hostCommands.ImmediateCommand;
import com.example.nayhakamboj.jukebox.hostCommands.RemoveSongCommand;
import com.example.nayhakamboj.jukebox.hostCommands.UpvoteCommand;
import com.example.nayhakamboj.jukebox.server.Host;
import com.example.nayhakamboj.jukebox.serverCommands.ServerReply;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;


public class Client {
	
	private String username;
	private ServerConnector sc;
	// Client (connect to Playlist)
	private CommandThread commandThread;
	// Host (own Playlist)
	private boolean isHost;
	private HostServer hs;
	private Playlist playlist;
	
	// If guest, pass null
	public Client(ServerConnector sc, String username){
		this.sc = sc;
		this.username = username;
	}
	
	// Only if host
	public Song advanceSong() {
		if (!isHost)
			return null;
		return playlist.advanceSong();
	}
	
	public List<Song> getAvailableSongs() {
		if (commandThread == null)
			return null;
		return commandThread.getAvailableSongs();
	}
	
	public Playlist refreshPlaylist() {
		if (commandThread == null)
			return null;
		ServerReply sr = commandThread.executeImmediate(new ImmediateCommand("RefreshPlaylist"));
		if ( sr != null && sr.getLabel().equals("RefreshPlaylist"))
			return (Playlist) sr.getData();
		else
			return null;
	}
	
	public boolean upvoteSong(Song song) {
		if (commandThread == null) 
			return false;
		commandThread.addCommand(new UpvoteCommand(song));
		return true;
	}
	
	public boolean downvoteSong(Song song) {
		if (commandThread == null) 
			return false;
		commandThread.addCommand(new DownvoteCommand(song));
		return true;
	}
	
	public boolean removeSong(Song song) {
		if (commandThread == null) 
			return false;
		commandThread.addCommand(new RemoveSongCommand(song));
		return true;
	}
	
	public boolean addSong(Song song) {
		if (commandThread == null)
			return false;
		commandThread.addCommand(new AddSongCommand(song));
		return true;
	}
	
	public boolean joinPlaylist(String username){
		if (isHost) // if already owns playlist
			return false;
		// Get the host from the server if you can
		Host h = sc.connect(username);
		if(h == null) // return false if can't join
			return false; 
		
		try {
			//connect to host
			Socket s = new Socket(h.getHostname(),h.getPort());
			//create notifier
			TimeoutNotifier n = new TimeoutNotifier();
			n.bindClient(this);
			//command thread
			commandThread = new CommandThread(s, h.getHostname(), h.getPort(),n);
			commandThread.start();
			// TODO
			// Client Refresh Thread????
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}
	
	
	// Exits user from a playlist that current client is subscribed to
	public boolean exitPlaylist(){
		if (commandThread != null) {
			commandThread.exitPlaylist();
			commandThread = null;
			return true;
		}
		else
			return false;
	}
	
	// If a host, stops your playlist
	public boolean stopPlaylist(){
		if (isHost == false) // if not a host
			return false;
		else {
			isHost = false;
			if(!exitPlaylist())
				return false;
			hs.shutDownServer();
			if(!sc.removeHost(this.username))
				return false;	
		}
		return true;
	}
	
	// Become a host!!
	public boolean startPlaylist(String hostname, int port, List<Song> availableSongs){
		if (username == null || isHost == true) // if guest user
			return false;
		isHost = true;
		playlist = new Playlist();
		UpdateThread ut = new UpdateThread(playlist);
		ut.start();//TODO not sure if proper
		Log.d("back","pre Hostserver");
		hs = new HostServer(port, ut, availableSongs);
		hs.start();
		if (!sc.addHost(this.username, hostname, port))
			return false;
		
		try {
			//connect to host (ourselves)
			Log.d("back","preconnect to self");
			Socket s = new Socket(hostname, port);
			Log.d("back","postconnect to self");
			//create notifier
			TimeoutNotifier n = new TimeoutNotifier();
			n.bindClient(this);
			//command thread
			commandThread = new CommandThread(s, hostname, port,n);
			commandThread.start();
			// TODO
			// Client Refresh Thread????
		} catch (UnknownHostException e) {
			Log.d("back","unknownhostexception: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			Log.d("back","ioe in client: " + ioe.getMessage());
			return false;
		}
		return true;
	}
	
	// Gets a plyalist from the server
	public List<Song> getPlaylist(String playlistname, String username){
		return sc.getPlaylist(username, playlistname);
	}
	
	public List<String> getPlaylists(String username){
		return sc.getPlaylists(username);
	}
	
	public boolean removePlaylist(String playlistname){
		return sc.removePlaylist(this.username, playlistname);
	}
	
	public boolean followUser(String toFollowUsername){
		return sc.followUser(this.username, toFollowUsername);
	}
	
	public List<String> getFollowing(){
		return sc.getFollowing(this.username);
	}
	
	public boolean createPlaylist(String playlistname, Playlist p){
		return sc.createPlaylist(this.username, playlistname, p);
	}
	
	public boolean copyPlaylist(String selectedUsername, String playlistname){
		return sc.copyPlaylist(this.username, selectedUsername, playlistname);
	}
	
	public boolean isHost(){
		return isHost;
	}
	
	public String getUsername(){
		return username;
	}

}
