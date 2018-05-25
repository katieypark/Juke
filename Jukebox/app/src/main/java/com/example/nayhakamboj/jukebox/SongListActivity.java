package com.example.nayhakamboj.jukebox;

import android.app.ListActivity;
import android.content.Intent;
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
import com.example.nayhakamboj.jukebox.serverCommands.ClientSongAdapter;

public class SongListActivity extends ListActivity implements View.OnClickListener{
    private JukeApplication app;
    private PlaylistSong selectedSong;
    private ClientSongAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        //Log.d("debug","Started SonglistActivity");
        app = (JukeApplication) getApplication();
        Client client = app.getClient();
        new refreshplaylist().execute();

        selectedSong = null;
        // TODO
        // TEST, attempt to make songs selectable
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        final TextView add = (TextView) findViewById(R.id.addSongsText);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinparty = new Intent(SongListActivity.this, ChecklistSongs.class);
                SongListActivity.this.startActivity(joinparty);
            }
        });
        final TextView ref = (TextView) findViewById(R.id.refreshPlaylistText);
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new refreshplaylist().execute();
            }
        });
        final TextView end = (TextView) findViewById(R.id.leavePartyText);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new endall().execute();
            }
        });

        final Button upvote = (Button) findViewById(R.id.upvoteButton);
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new upvote().execute();
            }
        });

        final Button downvote = (Button) findViewById(R.id.downvoteButton);
        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new downvote().execute();

            }
        });
    }
    private void sendback() {
        finish();
        //StartJoinPartyMainMenu.finish();
    }
    private void refresh(Playlist playlist) {

        Log.d("debug", "refresh");
        if (playlist != null && playlist.getToPlay() != null) {
            adapter = new ClientSongAdapter(playlist.getToPlay(), this);
            getListView().setAdapter(adapter);
            Log.d("debug",playlist.toString());
            if (playlist.getCurrentSong() != null) {
                if(playlist.getCurrentSong().getSong() != null){
                    TextView t = (TextView) findViewById(R.id.songsListText);
                    String s = playlist.getCurrentSong().getSong().getSongName();
                    if(s.length() > 30){
                        s = playlist.getCurrentSong().getSong().getSongName().substring(0,30) + "...";
                    }
                    t.setText(s);
                }

            }
            else {
                TextView t = (TextView) findViewById(R.id.songsListText);
                t.setText("Playlist");
            }
        }


    }
    @Override
    public void onClick(View v) {
        int pos = getListView().getPositionForView(v);


        if (pos != ListView.INVALID_POSITION) {

            selectedSong = adapter.getItem(pos);
            if(selectedSong != null){
                Toast.makeText(
                        this,
                        "Song: " + selectedSong.getSong().getSongName() + " selected", Toast.LENGTH_SHORT).show();
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
            sendback();
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