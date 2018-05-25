package client;

import java.io.Serializable;

public class PlaylistSong implements Serializable {
	//this class represents a song in the context of a playlist
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Song song;//the song it represents;
	private Playlist owner;//the playlist that contains this playlistsong
	private int voteScore;
	private boolean played; //stores if this song has started playing or not. NOTE: if currently playing, this is set to true
	
	public PlaylistSong(Song song){
		this.song = song;
		this.played = false;
		this.voteScore = 0;
		this.owner = null;
		//the owner gets set when you add it to the playlist
	}
	
	//copy constructor
	public PlaylistSong(PlaylistSong song){
		this.song = song.song;
		this.played = song.played;
		this.voteScore = song.voteScore;
		this.owner = song.owner;
	}
	
	public synchronized void upvote(){
		this.voteScore += 1;
	}
	
	public synchronized void downvote(){
		this.voteScore -= 1;
	}
	
	public synchronized void setVoteScore(int score){
		this.voteScore = score;
	}
	
	public synchronized void setOwner(Playlist owner){
		this.owner = owner;
	}
	
	public synchronized void setPlayed(boolean played){
		this.played = played;
	}
	
	public synchronized Song getSong(){
		return this.song;
	}
	
	public synchronized int getScore(){
		return this.voteScore;
	}
	
	public synchronized Playlist getOwner(){
		return this.owner;
	}
	
	public synchronized boolean hasPlayed(){
		return this.played;
	}
	
	public synchronized String toString(){
		return "Score("+voteScore+") played(" + played + ") " + song;
	}
	
}
