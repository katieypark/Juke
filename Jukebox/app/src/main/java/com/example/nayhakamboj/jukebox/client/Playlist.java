package com.example.nayhakamboj.jukebox.client;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 961756421069434912L;
	private List<PlaylistSong> allSongs, playedSongs, toPlay;
	private PlaylistSong current;
	
	public Playlist(){
		this.allSongs = new ArrayList<>();
		this.playedSongs = new ArrayList<>();
		this.toPlay = Collections.synchronizedList(new ArrayList<PlaylistSong>());
		this.current = null;
	}
	
	/** returns true if song could be added, false if already exists in toPlay */
	public synchronized boolean addSong(Song song){
		synchronized(toPlay){
			int index = songIndex(song);
			if(index == -1){
				PlaylistSong ps = new PlaylistSong(song);
				ps.setOwner(this);
				
				allSongs.add(ps);
				toPlay.add(ps);
				return true;
			}
			
			//if song exists, upvote it;
			PlaylistSong ps = toPlay.get(index);
			ps.upvote();
			return false;
		}
	}
	
	/** returns true if song could be removed, false if doesnt exists in toPlay */
	public synchronized boolean deleteSong(Song song){
		synchronized(toPlay){
			int indexToPlay = songIndex(song);
			if(indexToPlay == -1){
				return false;
			}
			
			PlaylistSong selected = toPlay.remove(indexToPlay);
			allSongs.remove(selected);
			return false;
		}
	}
	
	/** returns true if song could be upvoted, false if isnt in toPlay*/
	public synchronized boolean upvote(Song song){
		synchronized(toPlay){
			int index = songIndex(song);
			if(index == -1){
				return false;//couldnt find song
			}
			
			//if song exists, upvote it;
			PlaylistSong ps = toPlay.get(index);
			ps.upvote();
			return true;
		}
	}
	
	/** returns true if song could be downvoted, false if isnt in toPlay*/
	public synchronized boolean downvote(Song song){
		synchronized(toPlay){
			int index = songIndex(song);
			if(index == -1){
				return false;//couldnt find song
			}
			
			//if song exists, upvote it;
			PlaylistSong ps = toPlay.get(index);
			ps.downvote();
			return true;
		}
	}
	
	/** sort the toPlay, then set toPlay equal to the sorted list */
	public synchronized void sortToPlay(){
		synchronized(toPlay){
			//using insertion sort, sort by score in descending order
			for (int i = 0; i < toPlay.size(); ++ i){
				int j = i; 
				while (j > 0 && toPlay.get(j - 1).getScore() < toPlay.get(j).getScore()){
					//swap buffer[j] and buffer [j-1]
					PlaylistSong temp = toPlay.get(j-1);
					toPlay.set(j-1, toPlay.get(j));
					toPlay.set(j, temp);
					--j;
				}
			}
		}
	}
	
	/** advances to the top voted Song in the playlist. 
	 * updates all the arraylists and sets the current song
	 * This returns the current song 
	 * returns null if toPlay is empty
	 * @return
	 */
	public synchronized Song advanceSong(){
		synchronized(toPlay){
			if(toPlay.isEmpty())return null;
			sortToPlay();
			current = toPlay.get(0);
			toPlay.remove(0);
			//playedsongs includes the current song as the last song
			playedSongs.add(current);
			current.setPlayed(true);
			return current.getSong();	
		}	
	}
	
	
	
	/** returns the index if it is in the toPlay List 
	 * returns -1 if song is not in the toPlay List
	 */
	public synchronized int songIndex(Song song){
		synchronized (toPlay){
			for (int i = 0; i < toPlay.size(); ++i){
				if( toPlay.get(i).getSong().equals(song)){
					return i;
				}
			}
		}
		return -1;
	}
	
	public String toString(){
		String out = "";
		out += "----------------\n";
		out += "Played: \n";		
		synchronized(playedSongs){
			for (PlaylistSong p : playedSongs){
				out += p + "\n";
			}
		}
		out += "----------------\n";
		out += "Current: ";
		out += current + "\n";
		out += "----------------\n";
		out += "ToPlay:\n";
		synchronized(toPlay){
			for(PlaylistSong p: toPlay){
				out += p + "\n";
			}
		}
		out += "----------------\n";

		return out;		
	}
	
	public synchronized List<PlaylistSong> getAllSongs(){
		synchronized (allSongs){
			return Collections.unmodifiableList(new ArrayList<PlaylistSong>(allSongs));
		}
	}
	public synchronized List<PlaylistSong> getPlayedSongs(){
		synchronized (playedSongs){
			return Collections.unmodifiableList(new ArrayList<PlaylistSong>(playedSongs));
		}
	}
	public synchronized List<PlaylistSong> getToPlay(){
		synchronized (toPlay){
			return Collections.unmodifiableList(new ArrayList<PlaylistSong>(toPlay));
		}
	}
	public synchronized PlaylistSong getCurrentSong(){
		if(current == null){
			return null;
		}
		return new PlaylistSong(current);
	}	
}
