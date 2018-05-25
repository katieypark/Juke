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
		//String hostname = "159.203.212.48";
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
			
			String playlistHost = "localhost";
			int playlistPort = 6543;

			boolean joinPlaylist = client.joinPlaylist("Tina");
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
			client.addSong(available.get(rand.nextInt(available.size())));
			client.addSong(available.get(rand.nextInt(available.size())));
			client.downvoteSong(available.get(rand.nextInt(available.size())));
			client.upvoteSong(available.get(rand.nextInt(available.size())));
			
			
			Thread.sleep(500);
			
			p = client.refreshPlaylist();
			System.out.println(p);
			
			client.exitPlaylist();
			
			boolean joinPlaylist2 = client.joinPlaylist("Tina");
			if(joinPlaylist2){
				System.out.println("playlist joined");
			}else{
				System.out.println("playlist failed to join");
				return;
			}
			
			client.removeSong(available.get(rand.nextInt(available.size())));
			client.removeSong(available.get(rand.nextInt(available.size())));
			
			Thread.sleep(5000);
			
			p = client.refreshPlaylist();
			System.out.println(p);
			
			client.exitPlaylist();
					

			
		} catch (IOException | InterruptedException e) {
			System.out.println("Failed to connect to server: EXITING PROGRAM");
			return;
		}		
		
	}
}
