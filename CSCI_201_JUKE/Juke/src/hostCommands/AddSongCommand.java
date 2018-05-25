package hostCommands;

import client.Playlist;
import client.Song;

public class AddSongCommand extends HostCommand {
	
	private static final long serialVersionUID = 4436729219409091532L;

	public AddSongCommand(Song song) {
		super(song);
	}
	
	// adds song to playlist, otherwise upvotes song in playlist
	@Override
	public boolean execute(Playlist playlist) {
		return playlist.addSong(song);
	}

}
