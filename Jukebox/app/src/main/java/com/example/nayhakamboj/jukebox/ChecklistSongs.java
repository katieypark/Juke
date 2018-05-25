package com.example.nayhakamboj.jukebox;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.example.nayhakamboj.jukebox.client.Song;
import com.example.nayhakamboj.jukebox.serverCommands.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class  ChecklistSongs extends ListActivity implements
        View.OnClickListener{
    private ListView lv;
    protected List<Song> songList;
    private JukeApplication app;
    private SongAdapter songAdapter;
    protected List<Song> checkedSongs;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        app = (JukeApplication)getApplication();
       // songList = app.getClient().getAvailableSongs();

        new getSongList().execute();

        checkedSongs = new ArrayList<Song>();

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < checkedSongs.size(); i++){
                    new upvoteSongClass().execute(checkedSongs.get(i));
                }
                finish();
            }
        });
    }


    private void displaySongs(){
        songAdapter = new SongAdapter(songList, this);
        lv.setAdapter(songAdapter);
    }

    /*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int pos = lv.getPositionForView(buttonView);

        if (pos != ListView.INVALID_POSITION) {
            Song song = songList.get(pos);
            checkedSongs.add(song);

            Toast.makeText(
                    this,
                    "Song: " + song.getSongName() + " added!", Toast.LENGTH_SHORT).show();
        }

    }
    */
    @Override
    public void onClick(View v) {
        int pos = lv.getPositionForView(v);

        if (pos != ListView.INVALID_POSITION) {
            Song song = songList.get(pos);
            checkedSongs.add(song);

            Toast.makeText(
                    this,
                    "Song: " + song.getSongName() + " added!", Toast.LENGTH_SHORT).show();
        }
    }

    private class downvoteSongClass extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            Song song =  (Song)params[0];
            app.getClient().removeSong(song);

            return true;

        }
    }


    private class upvoteSongClass extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            Song song =  (Song)params[0];
            app.getClient().addSong(song);
            return true;

        }
    }

    private class getSongList extends AsyncTask<Object, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Object... params){
            songList = app.getClient().getAvailableSongs();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            lv = getListView();//(ListView) findViewById(R.id.list);
            displaySongs();
        }
    }
}
