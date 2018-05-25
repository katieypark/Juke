package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

public class removePlaylistCommand extends ServerCommand {

	private static final long serialVersionUID = 4554047276733361588L;
	
	private String username;
	private String playlistname;
	
	public removePlaylistCommand(String username, String playlistname) {
		this.username = username;
		this.playlistname = playlistname;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.removePlaylist(playlistname, username);
		return new ServerReply("Bool", success);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPlaylistname() {
		return playlistname;
	}
	
}