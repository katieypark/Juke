package com.example.nayhakamboj.jukebox.serverCommands;

import java.io.Serializable;

public class ServerReply implements Serializable {

	private static final long serialVersionUID = -5560862424187054160L;
	
	private String label;
	private Object data;
	
	public ServerReply(String label, Object data) {
		this.data = data;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Object getData() {
		return data;
	}
}

