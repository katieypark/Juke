package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

import java.util.List;

public class getPlaylistsCommand extends ServerCommand {

	private static final long serialVersionUID = 8239864118948638722L;
	
	private String username;
	
	public getPlaylistsCommand(String username) {
		this.username = username;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		List<String> playlists = dc.getPlaylists(username);
		return new ServerReply("Playlists", playlists);
	}
	
	public String getUsername() {
		return username;
	}
	
}
