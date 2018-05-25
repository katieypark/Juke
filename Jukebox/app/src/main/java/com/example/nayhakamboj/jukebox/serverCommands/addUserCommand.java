package com.example.nayhakamboj.jukebox.serverCommands;


import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

public class addUserCommand extends ServerCommand {

	private static final long serialVersionUID = 9158994297832369271L;

	private String username;
	private String email;
	private String password;
	
	public addUserCommand(String username, String password, String email) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.createUser(username, email, password);		
		return new ServerReply("Bool", success);
	}
	
	public String getUsername(String username) {
		return username;
	}
	
	public String getEmail(String email) {
		return email;
	}
	
	public String getPassword(String password) {
		return password;
	}

}
