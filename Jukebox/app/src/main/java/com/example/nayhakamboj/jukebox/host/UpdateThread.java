package com.example.nayhakamboj.jukebox.host;


import com.example.nayhakamboj.jukebox.client.Playlist;
import com.example.nayhakamboj.jukebox.hostCommands.HostCommand;
import com.example.nayhakamboj.jukebox.serverCommands.ServerReply;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UpdateThread extends Thread {

	private ConcurrentLinkedQueue<HostCommand> commands;
	private Playlist playlist;
	private boolean running;
	
	public UpdateThread(Playlist playlist) {
		this.playlist = playlist;
		commands = new ConcurrentLinkedQueue<HostCommand>();
		running = true;
	}
	
	// add a command to the Queue
	synchronized public void addCommand(HostCommand command) {
		commands.add(command);
	}
	
	public void stopUpdating(){
		running = false;
	}
	
	public void run() {
		while (running) {
			synchronized(this) {
				if (commands.size() != 0) {
					// Execute host command
					HostCommand hc = commands.remove();
					boolean success = hc.execute(playlist);
					ObjectOutputStream oos = hc.getObjectOutputStream();
					if (oos != null) {
						sendMessage(new ServerReply("Bool", success), oos);
					}
				
					// sort Playlist
					playlist.sortToPlay();
				}
			}
		}
	}
	
	public Playlist getRefreshedPlaylist() {
		return playlist;
	}
	
	private void sendMessage(Object object, ObjectOutputStream oos) {
		try {
			oos.reset();
			oos.writeObject(object);
			oos.flush();
		} catch (IOException ioe) {
			if(oos != null){
				try {
					running = false;
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}