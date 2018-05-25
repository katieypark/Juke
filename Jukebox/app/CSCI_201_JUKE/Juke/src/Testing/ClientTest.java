package Testing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import client.Client;
import client.Playlist;
import client.ServerConnector;
import client.Song;

public class ClientTest {

	public static void main(String[] args) {
		
		int port = 7654;
		String hostname = "localhost";
		
		try {
			ServerConnector sc = new ServerConnector(hostname, port);
			
			String username = "Tommy";
			String pass = "fighton";
			String email = "trojant@usc.edu";
			
			/*
			boolean created = sc.createUser(username, pass, email);
			if(created){
				System.out.println("Created user " + username);
			}else{
				System.out.println("Failed to create user : " + username);
			}
			*/
			boolean loginsuccess = sc.login(username, pass);
			if(loginsuccess){
				System.out.println("logged in user: " + username);
			}else{
				System.out.println("Failed to login user : " + username);
				return;
			}
			
			//create client
			Client client = new Client(sc, username);
			
			//get hostname of this computer
			
			String playlistHost = hostname;
			int playlistPort = 6543;
			List<Song> availableSongs = new ArrayList<>();
			availableSongs.add(new Song("songs/sorry.mp3", "Sorry", "Justin Bieber"));
			availableSongs.add(new Song("songs/whatdoyoumean.mp3", "What Do You Mean", "Justin Bieber"));
			availableSongs.add(new Song("songs/youastupidho.mp3", "You A Stupid Ho", "Nicki Minaj"));

			boolean playlistStarted = client.startPlaylist(playlistHost, playlistPort, availableSongs);
			if(playlistStarted){
				System.out.println("playlist started");
			}else{
				System.out.println("playlist failed to start");
			}
			
			
			//print existing playlist
			Playlist p = client.refreshPlaylist();
						
			System.out.println(p);
			
			while(true){
				p = client.refreshPlaylist();
				System.out.println(p);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//client.stopPlaylist();


			
			//System.out.println("playlist stopped");
			
			
		} catch (IOException e) {
			System.out.println("Failed to connect to server: EXITING PROGRAM");
			return;
		}		
		
	}
}
