package com.example.nayhakamboj.jukebox.host;


import android.util.Log;

import com.example.nayhakamboj.jukebox.client.Playlist;
import com.example.nayhakamboj.jukebox.client.Song;
import com.example.nayhakamboj.jukebox.hostCommands.HostCommand;
import com.example.nayhakamboj.jukebox.hostCommands.ImmediateCommand;
import com.example.nayhakamboj.jukebox.serverCommands.ServerReply;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ReceiverThread extends Thread{
	private UpdateThread updater; 
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket;
	private List<Song> availableSongs;
	
	public ReceiverThread(UpdateThread updater, Socket s, List<Song> availableSongs){
		this.updater = updater;
		
		this.socket = s;
		
		this.availableSongs = availableSongs;
	}
	
	public void run(){
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			while(ois != null && oos != null){
				Object o = ois.readObject();
				//depending on the type of command, 
				//execute immediately or pass to UpdateThread
				if(o instanceof ImmediateCommand){
					String label = ((ImmediateCommand) o).getLabel();
					if(label.equals("GetAvailableSongs")){
						oos.reset();
						oos.writeObject(new ServerReply("AvailableSongs",availableSongs));
						oos.flush();
					}else if(label.equals("RefreshPlaylist")){
						Playlist p = updater.getRefreshedPlaylist();
						oos.reset();
						oos.writeObject(new ServerReply("RefreshPlaylist",p));
						oos.flush();
					}	
				}else if (o instanceof HostCommand){
					((HostCommand)o).setObjectOutputStream(oos);
					updater.addCommand((HostCommand)o);
				}else{
					oos.reset();
					oos.writeObject(new ServerReply("Error", o));
					oos.flush();
				}
			}
		} catch (ClassNotFoundException e) {
			Log.d("back", "CNFE: " + e.getMessage());
		} catch (IOException e) {
			Log.d("back", "ioe in receiver: " + e.getMessage());
		}finally{
			try {
				/*
				if(oos != null){
					oos.close();
					oos = null;
				}
				if(ois != null){
					ois.close();
					ois = null;
				}
				*/
				socket.shutdownInput();
				if(socket != null)socket.close();

			} catch (IOException e) {
				Log.d("back","ioe closing somethinn:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
