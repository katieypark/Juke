package Testing;

import java.io.IOException;
import client.Client;
import client.Playlist;
import client.ServerConnector;
import client.Song;

public class CreatePlaylistTest {
	public static void main(String[] args) {

		int port = 7654;
		String hostname = "localhost";// "159.203.212.48";

		ServerConnector sc;
		try {
			sc = new ServerConnector(hostname, port);
			System.out.println("Created ServerConnector");

			String username = "Tina";
			String pass = "fighton";
			String email = "tina@usc.edu";
			
			
			boolean created = sc.createUser(username, pass, email);
			if(created){
				System.out.println("Created user " + username);
			}else{
				System.out.println("Failed to create user : " + username);
			}
			

			boolean loginsuccess = sc.login(username, pass);
			if(loginsuccess){
				System.out.println("logged in user: " + username);
			}else{
				System.out.println("Failed to login user : " + username);
			}

			String playlistname = "Joyce's_Playlist";
			Playlist playlist = new Playlist();
			playlist.addSong(new Song("filepath", "Teach me howto love", "WootWoot"));
			playlist.addSong(new Song("filepath2", "Sippy Cup", "Aesop Rocky"));

			//create client
			Client client = new Client(sc, username);
			boolean createPlaylist = client.createPlaylist(playlistname, playlist);
			if (createPlaylist) {
				System.out.println("playlist created");
			} else {
				System.out.println("failed to create playlist");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}			
}