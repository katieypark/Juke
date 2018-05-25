package hostCommands;

import client.Playlist;
import client.Song;

public class UpvoteCommand extends HostCommand {

	private static final long serialVersionUID = -1256454814346385895L;

	public UpvoteCommand(Song song) {
		super(song);
	}

	// upvotes a song in the playlist
	@Override
	public boolean execute(Playlist playlist) {
		return playlist.upvote(song);
	}
	
}
