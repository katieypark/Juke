package serverCommands;

import server.DatabaseConnector;

public class removeHostCommand extends ServerCommand {

	private static final long serialVersionUID = -2527715802298493753L;
	private String username;
	
	public removeHostCommand(String username) {
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