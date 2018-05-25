package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.client.Song;
import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

import java.util.List;

public class getPlaylistCommand extends ServerCommand {

	private static final long serialVersionUID = 1796853693043110935L;
	
	private String username;
	private String playlistname;
	
	public getPlaylistCommand(String username, String playlistname) {
		this.username = username;
		this.playlistname = playlistname;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		List<Song> songs = dc.getPlaylist(playlistname, username);
		return new ServerReply("Playlist", songs);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPlaylistname() {
		return playlistname;
	}
	
}