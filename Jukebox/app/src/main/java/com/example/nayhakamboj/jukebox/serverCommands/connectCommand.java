package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

public class connectCommand extends ServerCommand {

	private static final long serialVersionUID = 8119285332942796665L;
	private String username;
	
	public connectCommand(String username) {
		this.username = username;
	}
	
	
	// database connector is null, return null, this is unused
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		return null;
	}
	
	public String getUsername() {
		return username;
	}

}
