package serverCommands;

import java.util.List;

import server.DatabaseConnector;

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
