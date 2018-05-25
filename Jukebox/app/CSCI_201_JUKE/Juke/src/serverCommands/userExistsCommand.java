package serverCommands;

import server.DatabaseConnector;

public class userExistsCommand extends ServerCommand {
	
	private static final long serialVersionUID = 2055226322719732195L;
	
	private String username;
	
	public userExistsCommand(String username, String playlistname) {
		this.username = username;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.userExists(username); 
		return new ServerReply("Bool", success);
	}
	
	public String getUsername() {
		return username;
	}
	
}