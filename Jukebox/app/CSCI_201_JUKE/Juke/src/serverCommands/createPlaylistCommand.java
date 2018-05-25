package serverCommands;

import java.util.List;
import client.PlaylistSong;
import server.DatabaseConnector;

public class createPlaylistCommand extends ServerCommand {

	private static final long serialVersionUID = -1608848695451206837L;
	
	private String username;
	private String playlistname;
	List<PlaylistSong> songs;
	
	public createPlaylistCommand(String username, String playlistname, List<PlaylistSong> songs) {
		this.username = username;
		this.playlistname = playlistname;
		this.songs = songs;
	}
	
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.createPlaylist(playlistname, username, songs); 
		return new ServerReply("Bool", success);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPlaylistname() {
		return playlistname;
	}
	
	public List<PlaylistSong> getSongs() {
		return songs;
	}

}
