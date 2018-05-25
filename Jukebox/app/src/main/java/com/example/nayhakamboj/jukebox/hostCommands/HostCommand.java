package com.example.nayhakamboj.jukebox.hostCommands;


import com.example.nayhakamboj.jukebox.client.Playlist;
import com.example.nayhakamboj.jukebox.client.Song;

import java.io.ObjectOutputStream;
import java.io.Serializable;

abstract public class HostCommand implements Serializable {
	
	private static final long serialVersionUID = 3870097124606937491L;
	
	protected Song song;
	protected ObjectOutputStream oos;
	
	protected HostCommand(Song song) {
		this.song = song;
		this.oos = null;
	}
		
	public abstract boolean execute(Playlist playlist);
	
	public void setObjectOutputStream(ObjectOutputStream oos) {
		this.oos = oos;
	}
	
	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}
	
}
