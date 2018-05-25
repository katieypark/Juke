package com.example.nayhakamboj.jukebox;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayhakamboj.jukebox.client.Client;
import com.example.nayhakamboj.jukebox.client.Playlist;
import com.example.nayhakamboj.jukebox.client.PlaylistSong;
import com.example.nayhakamboj.jukebox.client.Song;
import com.example.nayhakamboj.jukebox.serverCommands.HostSongAdapter;

import java.io.IOException;
import java.util.List;

public class HostList extends ListActivity  implements View.OnClickListener {
private JukeApplication app;
    private Song song; MediaPlayer mp;

    private List<PlaylistSong> play;
    private HostSongAdapter adapter;
    private PlaylistSong selectedSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_list);
        app = (JukeApplication)getApplication();

        selectedSong = null;
        // TODO
        // TEST, attempt to make songs selectable
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);


        new refreshplaylist().execute();

        final TextView add = (TextView) findViewById(R.id.addSongsText2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug","CLICKED ADD");
                Intent addsong = new Intent(HostList.this, ChecklistSongs.class);
                HostList.this.startActivity(addsong);
            }
        });
        final TextView ref = (TextView) findViewById(R.id.refreshPlaylistText2);
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug","clickrefresh");
                new refreshplaylist().execute();
            }
        });
        final TextView end = (TextView) findViewById(R.id.leavePartyText2);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new endall().execute();
            }
        });
        final Button pauseplay = (Button) findViewById(R.id.button4);

        final Button upvote = (Button) findViewById(R.id.upvoteButton);
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new upvote().execute();
            }
        });

        final Button nextSong = (Button) findViewById(R.id.nextSongButton);
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new advanceSong().execute();
            }
        });

        final Button downvote = (Button) findViewById(R.id.downvoteButton);
        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new downvote().execute();

            }
        });

        final TextView playlist = (TextView) findViewById(R.id.songsListText);

        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
        add.setTypeface(fontBiko);
        ref.setTypeface(fontBiko);
        end.setTypeface(fontBiko);
        upvote.setTypeface(fontBiko);
        nextSong.setTypeface(fontBiko);
        downvote.setTypeface(fontBiko);
        playlist.setTypeface(fontBiko);
        pauseplay.setTypeface(fontBiko);
        upvote.setTextColor(Color.WHITE);
        downvote.setTextColor(Color.WHITE);
        nextSong.setTextColor(Color.WHITE);
        pauseplay.setTextColor(Color.WHITE);


        mp = new MediaPlayer();
        pauseplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mp.isPlaying()){
                    if (song == null) {
                        new advanceSong().execute();
                        pauseplay.setText("Pause");
                    }
                    else {
                        pauseplay.setText("Pause");
                        mp.start();
                    }
                } else {
                    if(mp.isPlaying()){
                        mp.pause();
                        pauseplay.setText("Play");
                    }
                }
            }
        });


        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                new advanceSong().execute();
                new refreshplaylist().execute();

            }
        });

    }
    private void reloadSong(Song song) {
        this.song = song;
        try {
            mp.reset();
            mp.setDataSource(song.getURI());
            mp.prepare();
            mp.start();
            //set the view to show the current song
            TextView t = (TextView) findViewById(R.id.songsListText);
            String s = song.getSongName();
            if(s.length() > 30){
                s = song.getSongName().substring(0,30) + "...";
            }
            t.setText(s);
       } catch (IOException ioe) {
           ioe.printStackTrace();
       }
    }

    private void refresh(Playlist playlist) {

        Log.d("debug","refresh");
        if (playlist != null && playlist.getToPlay() != null) {
            adapter = new HostSongAdapter(playlist.getToPlay(), this);
            getListView().setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        int pos = getListView().getPositionForView(v);


        if (pos != ListView.INVALID_POSITION) {

            selectedSong = adapter.getItem(pos);
            Toast.makeText(
                    this,
                    "Song: " + selectedSong.getSong().getSongName() + " selected", Toast.LENGTH_SHORT).show();

        }
    }

    private class advanceSong extends AsyncTask<Object, Void, Song> {

        @Override
        protected Song doInBackground(Object... params) {
            Client client = app.getClient();
            Song song = client.advanceSong();
            return song;
        }

        @Override
        protected void onPostExecute(Song song) {
            super.onPostExecute(song);
            if (song != null) {
                reloadSong(song);
            }

        }
    }
    private class refreshplaylist extends AsyncTask<Object, Void, Playlist> {

        @Override
        protected Playlist doInBackground(Object... params) {
            Client client = app.getClient();
            Playlist playlist = client.refreshPlaylist();
            return playlist;
        }

        @Override
        protected void onPostExecute(Playlist playlist) {
            super.onPostExecute(playlist);
            refresh(playlist);
        }
    }
    private class endall extends AsyncTask<Object, Void, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {
            return 1;
        }

        @Override
        protected void onPostExecute(Integer i) {
            Client client= app.getClient();
            client.exitPlaylist();
            if(mp.isPlaying()){
                mp.stop();
            }
            finish();
        }
    }

    private class upvote extends AsyncTask<Void, Void , Boolean >{

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean worked = false;
            if (selectedSong != null) {
                worked = app.getClient().upvoteSong(selectedSong.getSong());
            }
            return worked;

        }

        @Override
        protected void onPostExecute(Boolean worked) {
            if (!worked){
                Log.d("debug", "failed to upvote song: " + selectedSong);
                new refreshplaylist().execute();
            }
        }
    }

    private class downvote extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean worked = false;
            if (selectedSong != null) {
                worked = app.getClient().downvoteSong(selectedSong.getSong());
            }
            return worked;

        }

        @Override
        protected void onPostExecute(Boolean worked) {
            if (!worked){
                Log.d("debug", "failed to downvote song: " + selectedSong);
                new refreshplaylist().execute();
            }
        }
    }
}