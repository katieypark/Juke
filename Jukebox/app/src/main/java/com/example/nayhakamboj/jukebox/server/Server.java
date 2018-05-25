package com.example.nayhakamboj.jukebox.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread {

	private ServerSocket ss;
	private Map<String, Host> activeHosts;
	
	public Server(int Port) {
		activeHosts = new ConcurrentHashMap<String,Host>();
		try {
			ss = new ServerSocket(Port);
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} 
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				// TEST
				System.out.println("waiting for connections...");
				
				Socket s = ss.accept();
				
				// TEST
				System.out.println("connection from " + s.getInetAddress());
				
				ServerThread st = new ServerThread(s, this);
			}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} finally {
			try {
				if (ss != null)
					ss.close();
			} catch (IOException ioe) {
				System.out.println("ioe closing ss: " + ioe.getMessage());
			}
		}
	}
	
	// If we want to shutdown the server
	@SuppressWarnings("deprecation")
	public void shutDownServer() {
		try {
			ss.close();
			stop();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	// adds a host to the activeHosts map
	public void addHost(String username, String hostname, int port) {
		Host h = new Host(hostname, port);
		activeHosts.put(username, h);
	}
	
	public void removeHost(String username) {
		activeHosts.remove(username);
	}
	
	// returns true if given username currently has an active playlist (is in activeHosts
	public boolean contains(String username) {
		return activeHosts.containsKey(username);
	}
	
	// get active host's usernames
	public List<String> getActiveHostnames() {
		ArrayList<String> hosts = new ArrayList<String>();
		for (String s : activeHosts.keySet()) {
			hosts.add(s);
		}
		return hosts;
	}
	
	// get activeHosts map
	public Map<String, Host> getActiveHosts() {
		return new HashMap<String, Host>(activeHosts);
	}
	
	// get a Host based on a given username, returns null if username not present
	public Host getHost(String username) {
		synchronized(activeHosts) {
			if (!contains(username)) // if username is not in activeHosts map
				return null;
			return activeHosts.get(username);
		}
	}
	
	
	public static void main(String[] args) {
		configurationFileReader cfr = new configurationFileReader();
		cfr.readConfig(Constants.serverconfig);
		int port = cfr.getPort();
		if (port == -1) {
			System.out.println("Unable to obtain port number from configuration file");
			return;
		}
		Server s = new Server(port);
		s.start();
	}
}
