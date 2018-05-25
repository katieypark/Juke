package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.client.PlaylistSong;
import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

import java.util.List;

public class createPlaylistCommand extends ServerCommand {

	private static final long serialVersionUID = -1608848695451206837L;
	
	private String username;
	private String playlistname;
	List<PlaylistSong> songs;
	
	public createPlaylistCommand(String username, String playlistname, List<PlaylistSong> songs) {
		this.username = username;
		this.playlistname = playlistname;
		this.songs = songs;
	}
	
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.createPlaylist(playlistname, username, songs); 
		return new ServerReply("Bool", success);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPlaylistname() {
		return playlistname;
	}
	
	public List<PlaylistSong> getSongs() {
		return songs;
	}

}
