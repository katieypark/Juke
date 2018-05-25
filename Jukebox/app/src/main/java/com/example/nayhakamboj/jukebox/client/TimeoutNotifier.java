package com.example.nayhakamboj.jukebox.client;

public class TimeoutNotifier implements Notifier{
	private Client c;
	
	public void bindClient(Client c){
		this.c = c;
	}

	@Override
	public void alert() {
		c.exitPlaylist();	
	}
	
	
}
