package serverCommands;

import server.DatabaseConnector;

public class followUserCommand extends ServerCommand {

	private static final long serialVersionUID = -1608848695451206837L;
	
	private String currentUsername;
	private String toFollowUsername;
	
	public followUserCommand(String currentUsername, String toFollowUsername) {
		this.currentUsername = currentUsername;
		this.toFollowUsername = toFollowUsername;
	}
	
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.followFriend(currentUsername, toFollowUsername);
		return new ServerReply("Bool", success);
	}
	
	public String getCurrentUsername() {
		return currentUsername;
	}
	
	public String getToFollowUsername() {
		return toFollowUsername;
	}

}
