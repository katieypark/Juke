package Testing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import client.Playlist;
import client.Song;

public class SendPlaylistTest {
	public static void main(String[] args) {
		
		Playlist p = new Playlist();
		p.addSong(new Song("TEST", "TestSong", "Rama Gosula"));
		p.addSong(new Song("TEST2", "SongTest", "Gosula Rama"));
		

		
		try {
			ServerSocket ss = new ServerSocket(8888);
			Socket s = new Socket("localhost",8888);
			
			Socket a = ss.accept();
			
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(a.getInputStream());
			
			oos.reset();
			oos.writeObject(p);
			oos.flush();

			Playlist y = (Playlist) ois.readObject();
			
			System.out.println(p);
			System.out.println(y);
			
			p.addSong(new Song("TEST3", "adslkjf", "afds"));
			
			oos.reset();
			oos.writeObject(p);
			oos.flush();

			//y = (Playlist) ois.readObject();
			
			System.out.println(p);
			//System.out.println(y);
			System.out.println((Playlist) ois.readObject());
			
			oos.close();
			ois.close();
			s.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
