package com.example.nayhakamboj.jukebox.hostCommands;


import com.example.nayhakamboj.jukebox.client.Playlist;
import com.example.nayhakamboj.jukebox.client.Song;

public class DownvoteCommand extends HostCommand {

	private static final long serialVersionUID = -8041345788163415178L;
	
	public DownvoteCommand(Song song) {
		super(song);
	}
	
	// Downvotes a song in the playlist
	@Override
	public boolean execute(Playlist playlist) {
		return playlist.downvote(song);
	}

}
