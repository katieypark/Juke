package host;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import client.Song;

public class HostServer extends Thread{
	private UpdateThread updater;//TODO maybe this is unclean
	private ServerSocket ss;
	private List<Song> availableSongs;
	
	
	public HostServer(int port, UpdateThread updater, List<Song> availableSongs){
		this.availableSongs = availableSongs;
		this.updater = updater;
		try {
			ss = new ServerSocket(port);
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
				System.out.println(" Host waiting for connections...");
				
				Socket s = ss.accept();
				
				//create receiver thread with s and the update thread TODO
				ReceiverThread receiver = new ReceiverThread(updater,s,availableSongs);
				receiver.start();
				
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
}