package Testing;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.Playlist;
import client.PlaylistSong;
import client.Song;

public class Test {
	
	public static Random rand;
	
	public static void main(String[] args) {
		
		Playlist p = new Playlist();
		
		ExecutorService executor = Executors.newCachedThreadPool();
				
		executor.execute(new AddThread(p, 32));
		executor.execute(new UpvoteThread(p, 60));
		executor.execute(new AddThread(p, 32));
		executor.execute(new DownvoteThread(p, 6000));
		executor.execute(new UpvoteThread(p, 6000));
		executor.execute(new RemoveThread(p, 45));
		
		executor.shutdown();
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printList(p);
		
		while(!executor.isTerminated()){
			Thread.yield();
		}
		
		p.sortToPlay();
		printList(p);
		
		/*
		for (char i = 'a'; i <= 'z'; ++i){
			p.addSong(new Song(i+ "", i+ "", i+ ""));
		}
		//add all letters
		
		printList(p);
		
		
		//upvote mnop
		p.upvote(new Song("m", "m", "m"));
		p.upvote(new Song("n", "n", "n"));
		p.upvote(new Song("o", "o", "o"));
		p.upvote(new Song("p", "p", "p"));
		
		printList(p);
		
		//downvote qrs
		p.downvote(new Song("q", "m", "m"));
		p.downvote(new Song("r", "n", "n"));
		p.downvote(new Song("s", "o", "o"));

		//delete b through e
		p.deleteSong(new Song("b", "b", "b"));
		p.deleteSong(new Song("c", "c", "c"));
		p.deleteSong(new Song("d", "d", "d"));
		p.deleteSong(new Song("e", "e", "e"));
		
		printList(p);
		
		//delete e again
		p.deleteSong(new Song("e", "e", "e"));
		
		printList(p);
		
		//add a
		
		p.addSong(new Song("a","a","a"));
		
		printList(p);
		
		p.sortToPlay();
		
		printList(p);
		
		//add song and upvote twice
		p.addSong(new Song("sorry","d","d"));
		p.upvote(new Song("sorry","d","d"));
		p.upvote(new Song("sorry","d","d"));
		
		printList(p);
		
		Song curr = p.advanceSong();
		
		System.out.println("CURRENT: " + curr);
		
		printList(p);
		*/
				
		
	}
	
	private static void printList(Playlist p){
		//synchronized(p){
			System.out.println("---------");
			int i = 1;
			for (PlaylistSong s : p.getToPlay()){
				System.out.println(i + " " + s);
				++i;
			}
			System.out.println("---------");	
		}
	//}
}

class UpdateThread extends Thread{
	protected Playlist p;
	protected int k;
	
	public UpdateThread(Playlist p, int k){
		this.p = p;
		this.k = k;
	}
	
	public String randomSongName(int n){
		String name = "";
		for (int i = 0; i < n; ++i){
			name += (char)('a' + (new Random()).nextInt(25));
		}
		return name;
	}
}

class UpvoteThread extends UpdateThread{

	public UpvoteThread(Playlist p, int k) {
		super(p,k);
	}
	
	public void run(){
		for (int i = 0; i < k; ++i){
			p.upvote(new Song(randomSongName(2), "a", "a"));
		}
	}
}

class AddThread extends UpdateThread{

	public AddThread(Playlist p, int k) {
		super(p,k);
	}
	
	public void run(){
		for (int i = 0; i < k; ++i){
			p.addSong(new Song(randomSongName(2), "a", "a"));
		}
	}
}

class RemoveThread extends UpdateThread{

	public RemoveThread(Playlist p, int k) {
		super(p,k);
	}
	
	public void run(){
		for (int i = 0; i < k; ++i){
			p.deleteSong(new Song(randomSongName(2), "a", "a"));
		}
	}
}

class DownvoteThread extends UpdateThread{

	public DownvoteThread(Playlist p, int k) {
		super(p,k);
	}
	
	public void run(){
		for (int i = 0; i < k; ++i){
			p.downvote(new Song(randomSongName(2), "a", "a"));
		}
	}
}

