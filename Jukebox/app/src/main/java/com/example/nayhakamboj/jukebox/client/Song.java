package com.example.nayhakamboj.jukebox.client;

import java.io.Serializable;

public class Song implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uri;//this should be some unique identifier
	private String songName;
	private String songArtist;
	
	/* */
	public Song(String uri, String songName, String songArtist){
		this.uri = uri;
		this.songName = songName;
		this.songArtist = songArtist;
	}
	
	public boolean equals(Song song){
		return this.uri.equals(song.getURI());
	}
	
	public String getURI(){
		return this.uri;
	}
	
	public String getSongName(){
		return this.songName;
	}
	
	public String getSongArtist(){
		return this.songArtist;
	}
	
	public String toString(){
		return uri + " : " + songName + " by " + songArtist;
	}

}
