package com.example.nayhakamboj.jukebox.serverCommands;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nayhakamboj.jukebox.HostList;
import com.example.nayhakamboj.jukebox.R;
import com.example.nayhakamboj.jukebox.client.PlaylistSong;

import java.util.List;

/**
 * Created by ramag_000 on 4/20/2016.
 */
public class HostSongAdapter extends ArrayAdapter<PlaylistSong> {
    private static class PlaylistSongHolder {
        public TextView songName;
        public TextView songArtist;
        public TextView songScore;
    }

    private List<PlaylistSong> songList;
    private Context context;

    public HostSongAdapter(List<PlaylistSong> songList, Context context) {

        super(context, R.layout.activity_hostlist_songs, songList);
        this.songList = songList;
        this.context = context;
    }

    public List<PlaylistSong> getSongList(){
        return songList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        PlaylistSongHolder holder = new PlaylistSongHolder();

        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_hostlist_songs, null);

            holder.songName = (TextView) v.findViewById(R.id.name);
            holder.songArtist = (TextView) v.findViewById(R.id.artist);
            holder.songScore = (TextView) v.findViewById(R.id.score);
            //holder.chkBox = (CheckBox) v.findViewById(R.id.chk_box);

            //holder.chkBox.setOnCheckedChangeListener((ChecklistSongs) context);
            v.setOnClickListener((HostList) context);
            v.setTag(holder);


        } else {
            holder = (PlaylistSongHolder) v.getTag();
        }

        PlaylistSong song = songList.get(position);

        if (song.getSong() == null || song.getSong().getSongName() == null || song.getSong().getSongName() == " " || song.getSong().getSongName() == ""){
            holder.songName.setText("Unknown");
        }

        else {
            holder.songName.setText(song.getSong().getSongName());
        }

        if (song.getSong() == null || song.getSong().getSongArtist() == null || song.getSong().getSongArtist().trim() == ""){
            holder.songArtist.setText("Unknown");
        }

        else {
            holder.songArtist.setText(" " + song.getSong().getSongArtist());
        }
        Log.d("debug", holder.songScore.getText().toString());
        holder.songScore.setText("" +song.getScore() );


        return v;
    }
}
