package com.example.nayhakamboj.jukebox.hostCommands;


import com.example.nayhakamboj.jukebox.client.Playlist;
import com.example.nayhakamboj.jukebox.client.Song;

public class RemoveSongCommand extends HostCommand {

	private static final long serialVersionUID = -3685517316164360105L;

	public RemoveSongCommand(Song song) {
		super(song);
	}
	
	// removes a song from the playlist
	@Override
	public boolean execute(Playlist playlist) {
		return playlist.deleteSong(song);
	}
}
