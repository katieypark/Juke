package com.example.nayhakamboj.jukebox.host;


import android.util.Log;

import com.example.nayhakamboj.jukebox.client.Song;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HostServer extends Thread{
	private UpdateThread updater;//TODO maybe this is unclean
	private ServerSocket ss;
	private List<Song> availableSongs;
	
	
	public HostServer(int port, UpdateThread updater, List<Song> availableSongs){
		this.availableSongs = availableSongs;
		this.updater = updater;
		try {
			//TODO
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopHost(){
		if(ss != null && !ss.isClosed()){
			try {

				ss.close();
				ss = null;
				updater.stopUpdating();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			while (ss != null) {
				Log.d("back", " Host waiting for connections...");
				
				Socket s = ss.accept();
				
				//create receiver thread with s and the update thread TODO
				ReceiverThread receiver = new ReceiverThread(updater,s,availableSongs);
				receiver.start();
				
			}
		} catch (IOException ioe) {
			Log.d("back", "ioe in hostserver: " + ioe.getMessage());
		} finally {
			try {
				if (ss != null)
					ss.close();
			} catch (IOException ioe) {
				Log.d("back", "ioe closing ss: " + ioe.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void shutDownServer() {
		try {
			ss.close();
			stop();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
}