package com.example.nayhakamboj.jukebox.serverCommands;

import com.example.nayhakamboj.jukebox.server.DatabaseConnector;

import java.io.Serializable;

public abstract class ServerCommand implements Serializable {

	private static final long serialVersionUID = 8252737106450144374L;
	
	public abstract ServerReply execute(DatabaseConnector dc);

}
