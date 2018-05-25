package Testing;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import client.Client;
import client.Playlist;
import client.ServerConnector;
import client.Song;

public class JoinPlaylistTest {
	public static void main(String[] args) {

		int port = 7654;
		String hostname = "localhost";
		
		try {
			ServerConnector sc = new ServerConnector(hostname, port);
			
			String username = "Tina";
			String pass = "fighton";
			String email = "tina@usc.edu";
			
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
			}
			
			
			//create client
			Client client = new Client(sc, username);
			
			//get hostname of this computer
			
			String playlistHost = hostname;
			int playlistPort = 6543;

			boolean joinPlaylist = client.joinPlaylist("Tommy");
			if(joinPlaylist){
				System.out.println("playlist joined");
			}else{
				System.out.println("playlist failed to join");
				return;
			}
			
			//try to get available songs
			List<Song> available = client.getAvailableSongs();
			System.out.println("Available songs");
			for (Song s : available){
				System.out.println(s);
			}
			
			//print existing playlist
			Playlist p = client.refreshPlaylist();
			System.out.println(p);
			
			//add songs randomly
			Random rand = new Random();
			client.addSong(available.get(rand.nextInt(3)));
			client.addSong(available.get(rand.nextInt(3)));
			client.downvoteSong(available.get(rand.nextInt(3)));
			
			p = client.refreshPlaylist();
			System.out.println(p);

			
		} catch (IOException e) {
			System.out.println("Failed to connect to server: EXITING PROGRAM");
			return;
		}		
		
	}
}
