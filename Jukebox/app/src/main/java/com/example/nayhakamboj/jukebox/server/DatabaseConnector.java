package com.example.nayhakamboj.jukebox.server;

import com.example.nayhakamboj.jukebox.client.PlaylistSong;
import com.example.nayhakamboj.jukebox.client.Song;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class DatabaseConnector {
	
	private Connection conn;
	
	public DatabaseConnector() {     
		this.conn = null;
	}
		
	// checks if a user exists in the database by comparing against a given username
	public boolean userExists(String username) {
		boolean exists = false;
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				exists = true;
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		
		return exists;
	}
	
	
	// Creates a new user in the database with the given username, email, and password
	// Assumes no QR Code in Database
	public boolean createUser(String username, String email, String password) {
		boolean created = false;
		
		// hash password
		password = hashPassword(password);
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) { // if user doesn't exist
				ps = conn.prepareStatement("INSERT INTO User (username, passhash, email) VALUES (?,?,?)");
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, email);
				int added = ps.executeUpdate();
				if (added > 0)
					created = true;
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
	
		return created;
	}
	
	
	// Returns whether a user with a given username and password exists in the database
	public boolean login(String username, String password) {
		boolean loggedin = false;
		
		// hash password
		password = hashPassword(password);
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=? AND passhash=?");
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if user exists
				loggedin = true;
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		
		return loggedin;
	}
	
	
	// Returns an ArrayList containing a given user's friends
	public List<String> getFriends(String username) {
		ArrayList<String> friends = new ArrayList<String>();
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if user exists
				int userID = rs.getInt("userId");
				ps = conn.prepareStatement("SELECT * FROM User JOIN Friend ON (User.userID = Friend.followedID) where followerID=?");
				ps.setInt(1, userID);
				rs.close();
				rs = ps.executeQuery();
				while(rs.next()) {
					String friendID = rs.getString("username");
					friends.add(friendID);
				}
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		
		return friends;
	}
	
	
	// Updates a current user to follow another user in the database
	public boolean followFriend(String currentUsername, String toFollowUsername) {
		boolean followed = false;
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, currentUsername);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if user exists
				int followerID = rs.getInt("userId");
				ps.setString(1, toFollowUsername);
				rs = ps.executeQuery();
				if(rs.next()){
					int toFollowID = rs.getInt("userId");
					ps.setString(1, toFollowUsername);
					rs.close();
					rs = ps.executeQuery();
					if (rs.next()) { // if user to follow exists
						rs.close();
						
						//if friendship doesn't already exist
						ps = conn.prepareStatement("SELECT * FROM Friend WHERE followerId=? AND followedId = ?");
						ps.setInt(1, followerID);
						ps.setInt(2, toFollowID);
						rs = ps.executeQuery();
						
						if(!rs.next()){
							ps = conn.prepareStatement("INSERT INTO Friend (followerID, followedID) VALUES (?,?)");
							ps.setInt(1, followerID);
							ps.setInt(2,toFollowID);
							int added = ps.executeUpdate();
							if (added > 0) { // if completed update successfully
								followed = true;
							}
						}
			
					}
				}

			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		
		return followed;
			
	}
	
	// checks if a playlist exists for a specific playlistname and username in the database
	public boolean playlistExists(String playlistname,String username) {
		boolean exists = false;
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Playlist WHERE playlistname=? AND userID=?");
			ps.setString(1, playlistname);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if user exists
				exists = true;
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		
		return exists;
	}

	// inserts a new playlist into the database given a playlistname, username, and filepath (songs in playlist is also required
	public boolean createPlaylist(String playlistname, String username, List<PlaylistSong> songs) {
		boolean created = false;
		
		System.out.println("create");
		//System.out.flush();

		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);

			// check if user exists
			// check if user exists and get userID
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if user exists
				int userID = rs.getInt("userID");
				// check if playlist exists
				 ps = conn.prepareStatement("SELECT * FROM Playlist WHERE playlistname=? AND userID=?");
				ps.setString(1, playlistname);
				ps.setInt(2, userID);
				rs.close();
				rs = ps.executeQuery();
				if (!rs.next()) {
					// Insert into Playlist Table
					ps = conn.prepareStatement("INSERT INTO Playlist (playlistname, userID) VALUES (?,?)");
					ps.setString(1, playlistname);
					ps.setInt(2, userID);
					int added = ps.executeUpdate();
					// Get playlistID
					ps = conn.prepareStatement("SELECT * FROM Playlist WHERE playlistname=? AND userID=?");
					rs.close();
					ps.setString(1, playlistname);
					ps.setInt(2, userID);
					rs = ps.executeQuery();
					rs.next();
					Integer playlistID = rs.getInt("playlistID");						
					// Add each song to PlaylistContent Table
					ps = conn.prepareStatement("INSERT INTO PlaylistContent (playlistID, songIndex, songName, songArtist) VALUES (?,?,?,?)");
					for (int i = 1; i <= songs.size(); i++) {
						Song song = songs.get(i-1).getSong();
						String artist = song.getSongArtist();
						String name = song.getSongName();
						ps.setInt(1, playlistID);
						ps.setInt(2, i);
						ps.setString(3, name);
						ps.setString(4, artist);
						ps.executeUpdate();	
					}
					if (added > 0)
						created = true;
				}
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
	
		return created;
		
	}
	
	// Copies one user's playlist//TODO
	public boolean copyPlaylist(String currentUserName, String SelectedUserName, String playlistname) {
		boolean copied = false;
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if playlist exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Playlist JOIN User ON (Playlist.UserId = User.userId) WHERE Playlist.playlistname=? AND User.username=?");
			ps.setString(1, playlistname);
			ps.setString(2, SelectedUserName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if playlist exists in the selected user
				int origPlaylistID = rs.getInt("playlistId");
				
				rs.close(); 
				ps.setString(2, currentUserName);
				boolean uniquename = false;
				while(!uniquename){
					ps.setString(1, playlistname);
					rs = ps.executeQuery();
					if (rs.next()) { // if playlist exists in the current user, append a 0 to the end;
						playlistname += "0";
					}else{
						uniquename = true;
					}
					rs.close();
				}

				// Get this user's ID
				ps = conn.prepareStatement("SELECT userId FROM User WHERE username = ?");
				ps.setString(1, currentUserName);
				rs = ps.executeQuery();
				if(rs.next()){
					int userId = rs.getInt("userId");
					rs.close();

					// Insert into Playlist Table
					ps = conn.prepareStatement("INSERT INTO Playlist (playlistname, userID) VALUES (?,?)");
					ps.setString(1, playlistname);
					ps.setInt(2, userId);
					int added = ps.executeUpdate();
					
					//get new playlistID
					ps = conn.prepareStatement("SELECT * FROM Playlist JOIN User ON (Playlist.UserId = User.userId) WHERE Playlist.playlistname=? AND User.username=?");
					ps.setString(1,playlistname);
					ps.setString(2,currentUserName);
					rs = ps.executeQuery();
					if(rs.next()){
						int playlistID = rs.getInt("playlistId");
						rs.close();
						
						// Insert into PlaylistContent Table
						ps = conn.prepareStatement("SELECT * FROM PlaylistContent WHERE playlistID=?");	
						ps.setInt(1, origPlaylistID);
						rs = ps.executeQuery();
						while (rs.next()) {
							int songIndex = rs.getInt("songIndex");
							String songName = rs.getString("songName");
							String songArtist = rs.getString("songArtist");
							
							PreparedStatement ps2 = conn.prepareStatement("INSERT INTO PlaylistContent (playlistID, songIndex, songName, songArtist) VALUES (?,?,?,?)");
							ps2.setInt(1, playlistID);
							ps2.setInt(2, songIndex);
							ps2.setString(3, songName);
							ps2.setString(4, songArtist);
							ps2.executeUpdate();
						}
						if (added > 0)
							copied = true;
					}
				}				
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
	
		return copied;
		
	}
	
	// removes a specific playlist from the database
	public boolean removePlaylist(String playlistname, String username) {
		boolean deleted = false;
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if playlist exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Playlist JOIN User ON (User.userId = Playlist.userId) WHERE playlistname=? AND username=?");
			ps.setString(1, playlistname);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if playlist does exist
				// get playlistID
				int playlistID = rs.getInt("playlistID");
				// Remove from PlaylistContent Table
				ps = conn.prepareStatement("DELETE FROM PlaylistContent WHERE playlistID=?");
				ps.setInt(1, playlistID);
				ps.executeUpdate();
				// Remove from Playlist Table
				ps = conn.prepareStatement("DELETE FROM Playlist WHERE playlistID=?");
				ps.setInt(1, playlistID);
				int result = ps.executeUpdate();
				if (result > 0) {
					deleted = true;
				}
			}
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		
		return deleted;
	}
	
	// Returns a specified playlist if it exists (playlist given by username, playlistname) 
	// If playlist doesn't exist, returns null
	public List<Song> getPlaylist(String playlistname, String username) {
		boolean exists = false;
		List<Song> playlist = new ArrayList<Song>();
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if playlist exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Playlist JOIN User ON (Playlist.UserId = User.userId) WHERE Playlist.playlistname=? AND User.username=?");
			ps.setString(1, playlistname);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // if playlist does exists
				exists = true;
				// get playlistID
				Integer playlistID = rs.getInt("playlistID");
				// add songs to playlist list
				ps = conn.prepareStatement("SELECT * FROM PlaylistContent WHERE playlistID=? ORDER BY songIndex ASC");
				ps.setInt(1, playlistID);
				rs.close();
				rs = ps.executeQuery();
				while(rs.next()) {
					Song s = new Song(null, rs.getString("songName"), rs.getString("songArtist"));
					playlist.add(s);
				}
			}
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		if (!exists) // if playlist doesn't exist
			return null;
		else
			return playlist;	
	}
	
	// Returns a list of playlist names for a specific user
	// Returns null if user does not exist
	public List<String> getPlaylists(String username) {
		Boolean exists = false;
		List<String> playlists = new ArrayList<String>();
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				exists = true;
				rs.close();
				// add playlists to list
				ps = conn.prepareStatement("SELECT * FROM Playlist JOIN User ON (Playlist.UserId = User.userId) WHERE User.username=?");
				ps.setString(1, username);
				rs = ps.executeQuery();
				while(rs.next()) {
					playlists.add(rs.getString("playlistname"));
				}	
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		if (!exists) // if user doesn't exist
			return null;
		else
			return playlists;	
	}
	
	// Returns a list of user's that are following a given user
	// Returns null if user doesn't exist
	public List<String> getFollowing(String username) {
		Boolean exists = false;
		List<String> following = new ArrayList<String>();
		
		try {
			Class.forName(Constants.jdbc);
			conn = DriverManager.getConnection(Constants.connection);
			
			// check if user exists
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE username=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int userID = rs.getInt("userId");

				exists = true;
				rs.close();
				// add followers to list
				ps = conn.prepareStatement("SELECT * FROM User JOIN Friend ON (User.userID = Friend.followerID) WHERE followedID=?");
				ps.setInt(1, userID);
				rs = ps.executeQuery();
				while(rs.next()) {
					following.add(rs.getString("username"));
				}	
			} 
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
		if (!exists) // if user doesn't exist
			return null;
		else
			return following;	
	}
	
	
	// Internal Functions
	private String hashPassword(String password) {
		// hash password
		String hashedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashedPassword = sb.toString();
		} catch (NoSuchAlgorithmException NSAE) {
			System.out.println("NSAE: " + NSAE.getMessage());
		}
		
		return hashedPassword;
	}
	
}
