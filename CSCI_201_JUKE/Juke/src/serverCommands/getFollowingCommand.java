package serverCommands;

import java.util.List;

import server.DatabaseConnector;

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