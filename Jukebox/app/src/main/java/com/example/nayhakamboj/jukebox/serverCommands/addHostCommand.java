package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

public class addHostCommand extends ServerCommand {

	private static final long serialVersionUID = -7284410342573085381L;
	private String username;
	private String hostname;
	private int port;
	
	public addHostCommand(String username, String hostname, int port) {
		this.username = username;
		this.hostname = hostname;
		this.port = port;
	}
	
	
	// database connector is null, return null, this is unused
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		return null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public int getPort() {
		return port;
	}
	
}
