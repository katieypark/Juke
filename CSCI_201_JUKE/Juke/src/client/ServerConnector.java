package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import server.Host;
import serverCommands.ServerReply;
import serverCommands.addHostCommand;
import serverCommands.addUserCommand;
import serverCommands.connectCommand;
import serverCommands.copyPlaylistCommand;
import serverCommands.createPlaylistCommand;
import serverCommands.followUserCommand;
import serverCommands.getFollowingCommand;
import serverCommands.getPlaylistCommand;
import serverCommands.getPlaylistsCommand;
import serverCommands.loginCommand;
import serverCommands.removeHostCommand;
import serverCommands.removePlaylistCommand;

public class ServerConnector {
	
	private Socket server;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ServerConnector(String hostname, int port) throws IOException{
		server = new Socket(hostname, port);
		oos = new ObjectOutputStream(server.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(server.getInputStream());	 
	}
	
	public boolean login(String username, String password) {
		ServerReply reply = null;
		try {
			sendMessage(new loginCommand(username, password));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean createUser(String username, String password, String email) {
		ServerReply reply = null;
		try {
			sendMessage(new addUserCommand(username, password, email));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean addHost(String username, String hostname, int port) {
		ServerReply reply = null;
		try {
			sendMessage(new addHostCommand(username, hostname, port));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public Host connect(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new connectCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null && reply.getData() instanceof Host)
			return (Host) reply.getData();
		return null;
	}
	
	public boolean removeHost(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new removeHostCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Song> getPlaylist(String username, String playlistname) {
		ServerReply reply = null;
		try {
			sendMessage(new getPlaylistCommand(username, playlistname));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (List<Song>) reply.getData();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPlaylists(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new getPlaylistsCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (List<String>) reply.getData();
		return null;
	}
	
	public boolean removePlaylist(String username, String playlistname) {
		ServerReply reply = null;
		try {
			sendMessage(new removePlaylistCommand(username, playlistname));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean followUser(String currentUsername, String toFollowUsername) {
		ServerReply reply = null;
		try {
			sendMessage(new followUserCommand(currentUsername, toFollowUsername));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	List<String> getFollowing(String username) {
		ServerReply reply = null;
		try {
			sendMessage(new getFollowingCommand(username));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return null; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (List<String>) reply.getData();
		return null;
	}
	
	public boolean createPlaylist(String username, String playlistname, Playlist playlist) {
		List<PlaylistSong> playlistsongs = playlist.getPlayedSongs();
		ServerReply reply = null;
		try {
			sendMessage(new createPlaylistCommand(username, playlistname, playlistsongs));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			System.out.println("couldn't connect");
			
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	public boolean copyPlaylist(String currentUsername, String selectedUsername, String playlistname) {
		ServerReply reply = null;
		try {
			sendMessage(new copyPlaylistCommand(currentUsername, selectedUsername, playlistname));
			reply = (ServerReply) ois.readObject();
		} catch (IOException ioe) {
			return false; // couldn't connect
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (reply != null)
			return (boolean) reply.getData();
		return false;
	}
	
	// Send a message on the output stream
	private void sendMessage(Object object) {
		try {
			oos.reset();
			oos.writeObject(object);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
}