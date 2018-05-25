package client;

import Testing.JoinPlaylistTest;

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
