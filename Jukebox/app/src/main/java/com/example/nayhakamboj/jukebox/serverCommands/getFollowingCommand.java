package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

import java.util.List;

public class getFollowingCommand extends ServerCommand {

	private static final long serialVersionUID = -6206568112336383708L;
	
	private String username;
	
	public getFollowingCommand(String username) {
		this.username = username;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		//following
		List<String> following = dc.getFriends(username);
		return new ServerReply("Following", following);
	}
	
	public String getUsername() {
		return username;
	}
	
}