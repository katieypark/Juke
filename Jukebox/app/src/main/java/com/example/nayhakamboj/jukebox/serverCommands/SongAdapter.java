package com.example.nayhakamboj.jukebox.serverCommands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nayhakamboj.jukebox.ChecklistSongs;
import com.example.nayhakamboj.jukebox.R;
import com.example.nayhakamboj.jukebox.client.Song;

import java.util.List;

/**
 * Created by nayhakamboj on 4/17/16.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    private List<Song> songList;
    private Context context;

    public SongAdapter(List<Song> songList, Context context) {
        super(context, R.layout.activity_checklist_songs, songList);
        this.songList = songList;
        this.context = context;
    }
    private static class SongHolder {
        public TextView songName;
        public TextView songArtist;
        //public CheckBox chkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        SongHolder holder = new SongHolder();

        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_checklist_songs, null);

            holder.songName = (TextView) v.findViewById(R.id.name);
            holder.songArtist = (TextView) v.findViewById(R.id.artist);
            //holder.chkBox = (CheckBox) v.findViewById(R.id.chk_box);

            //holder.chkBox.setOnCheckedChangeListener((ChecklistSongs) context);
            v.setOnClickListener((ChecklistSongs) context);
            v.setTag(holder);

        } else {
            holder = (SongHolder) v.getTag();
        }

        Song song = songList.get(position);

        if (song.getSongName() == null || song.getSongName() == " " || song.getSongName() == ""){
            holder.songName.setText("Unknown");
        }

        else {
            holder.songName.setText(song.getSongName());
        }

        if (song.getSongArtist() == null || song.getSongArtist().trim() == ""){
            holder.songArtist.setText("Unknown");
        }

        else {
            holder.songArtist.setText(" " + song.getSongArtist());
        }

        //holder.chkBox.setTag(song);

        return v;
    }
}
