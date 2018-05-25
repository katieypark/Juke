package com.example.nayhakamboj.jukebox.client;

import android.util.Log;

import com.example.nayhakamboj.jukebox.hostCommands.HostCommand;
import com.example.nayhakamboj.jukebox.hostCommands.ImmediateCommand;
import com.example.nayhakamboj.jukebox.serverCommands.ServerReply;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CommandThread extends Thread {
	
	private static final int TIMEOUTATTEMPTS = 5;
	private static final int CONNECTIONSLEEP = 500;
	
	private ConcurrentLinkedQueue<HostCommand> commands;
	private Notifier notifier;
	private Socket socket;
	private String hostname;
	private int port;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private List<Song> cachedSongs;
	
	
	//TODO add some way to notify that the connection has been timed out
	
	CommandThread(Socket s,String hostname,int port, Notifier n) throws IOException {
		this.notifier = n;
		this.socket = s;
		System.out.println(s.getInputStream());
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.oos.flush();
		this.ois = new ObjectInputStream(socket.getInputStream());
		this.hostname = hostname;
		this.port = port;
		this.commands = new ConcurrentLinkedQueue<>();
		this.cachedSongs = null;				
	}
	
	//Try to reconnect socket. If fails, decrease timeout attempts number
	//if timeoutattempts runs out, notify the client TODO
	private void reconnect(){	
		boolean connected = false;

		//close the current socket
		if(socket != null || !socket.isClosed()){
			int attempts = TIMEOUTATTEMPTS;
			while(!connected && attempts > 0){
				try {
					//closet the socket and streams and create a new connection
					if(ois != null && !socket.isClosed()) ois.close();
					if(oos != null && !socket.isClosed()) oos.close();
					if(!socket.isClosed())socket.close();
					
					//try to make a new socket
					socket = new Socket(hostname, port);
					ois = new ObjectInputStream(socket.getInputStream());
					oos = new ObjectOutputStream(socket.getOutputStream());
					
					//try to send and receive a message, if succeeded set success to true
					oos.reset();
					oos.writeObject(true);
					oos.flush();

					Object reply = ois.readObject();
					
					//if a reply was received, you are connected
					if(reply != null){
						connected = true;
					}
				}catch(ConnectException ce){
					Log.d("back", "Couldnt connect: " + ce.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}finally{
					//if you couldn't connect subtract from the number of attempts remaining
					if(!connected)--attempts;
					try {
						Thread.sleep(CONNECTIONSLEEP);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(!connected){
			Log.d("back","TIMEOUT");
			//TODO notify that timed out
			notifier.alert();
		}
	}
	
	synchronized public void addCommand(HostCommand command) {
		commands.add(command);
	}
	
	@SuppressWarnings("unchecked")
	public List<Song> getAvailableSongs(){
		if(cachedSongs != null){
			return cachedSongs;
		}
		ServerReply r = executeImmediate(new ImmediateCommand("GetAvailableSongs"));
		if(r != null){
			return (List<Song>)r.getData();
		}
		return null;
	}
	
	public synchronized ServerReply executeImmediate(ImmediateCommand command){
		ServerReply r = null;
		try {
			oos.reset();
			oos.writeObject(command);
			r = (ServerReply)ois.readObject();
			return r;
		} catch (ClassNotFoundException cnfe){
			cnfe.printStackTrace();
			return r;
		} catch(IOException e){
			reconnect();
			return r;
		}
	}
			
	@SuppressWarnings("finally")
	public synchronized ServerReply executeCommand(HostCommand command) {
		ServerReply r = null;
		try {
			oos.reset();
			oos.writeObject(command);
			r = (ServerReply)ois.readObject();
		} catch (ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}catch(IOException e){
			//reconnect();
			socket.close();
		}finally{
			return r;
		}
	}
	
	public void exitPlaylist(){
		if(socket != null && !socket.isClosed()){
			try {
				socket.shutdownInput();
				socket.close();
				Log.d("back", "close socket");
				socket = null;
			} catch (IOException e) {
				Log.d("back","ioe exiting playlist: " + e.getMessage());
			}
		}
	}
	
	public void run() {
		while (socket != null) {
			synchronized(commands){				
				HostCommand current = commands.poll();
				if(current != null){
					executeCommand(current);
				}
			}
		}
	}
}
