package com.example.nayhakamboj.jukebox.server;

import java.io.Serializable;

// To hold active host identities 
public class Host implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2866407818326731615L;
	private String hostname;
	private int port;
	
	public Host(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	public String getHostname() {
		return hostname;
	}
	public int getPort() {
		return port;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
