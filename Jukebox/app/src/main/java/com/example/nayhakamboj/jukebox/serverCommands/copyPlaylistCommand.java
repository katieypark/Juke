package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

public class copyPlaylistCommand extends ServerCommand {

	private static final long serialVersionUID = -1608848695451206837L;
	
	private String currentUsername;
	private String selectedUsername;
	private String playlistname;
	
	public copyPlaylistCommand(String currentUsername, String selectedUsername, String playlistname) {
		this.currentUsername = currentUsername;
		this.selectedUsername = selectedUsername;
		this.playlistname = playlistname;
	}
	
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.copyPlaylist(currentUsername, selectedUsername, playlistname); 
		return new ServerReply("Bool", success);
	}
	
	public String getCurrentUsername() {
		return currentUsername;
	}
	
	public String getSelectedUsername() {
		return selectedUsername;
	}
	
	public String getPlaylistname() {
		return playlistname;
	}

}
