package com.example.nayhakamboj.jukebox;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.example.nayhakamboj.jukebox.client.Song;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class StartParty extends AppCompatActivity {

    private JukeApplication app;
    private final static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_party);
        app = (JukeApplication)getApplicationContext();

        final Button start = (Button) findViewById(R.id.letsGetFunkyButton);
        //final TextView desc = (TextView) findViewById(R.id.startPartyText);
        //assert joinParty != null;

        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
       // desc.setTypeface(fontBiko);
        start.setTypeface(fontBiko);
        start.setTextColor(Color.WHITE);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request permissions
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d("debug","PERMISSION NOT GRANTED");

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to read the contacts
                    }

                    StartParty.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant
                }else{
                    letsGetFunky();
                }
            }

        });


    }

    private void letsGetFunky(){
        //get the list of songs
        List<Song> songList = new ArrayList<>();

        ContentResolver musicResolver = getContentResolver();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //get Songs from device
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";


        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
        };


        Cursor musicCursor = musicResolver.query(musicUri,projection,selection,null,null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int dataColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            //add songs to list
            do {
                String thisData = musicCursor.getString(dataColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisData, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }


        //start the playlist
        new StartPlaylistTast().execute(songList);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("debug","permission granted dudes");
                    letsGetFunky();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private class StartPlaylistTast extends AsyncTask<Object, Void, Integer> {
        private static final int ERROR = -1;
        private static final int FAILED = 1;
        private static final int WORKED = 0;

        @Override
        protected Integer doInBackground(Object... params) {
            List<Song> songList = (List<Song>)params[0];

            //get the host's hostname
            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            int ip = wm.getConnectionInfo().getIpAddress();
            byte[] bytes = BigInteger.valueOf(ip).toByteArray();
            try {
                InetAddress address = InetAddress.getByAddress(bytes);

                //String hostname = "192.168.70.1";
                String hostname = getIpAddr();
                int port = 6543;

                Log.d("debug",hostname);

                if(app.getClient().startPlaylist(hostname,port,songList)) {
                    Log.d("debug","WORKED");
                    return WORKED;
                }
                else {
                    Log.d("debug","FAILED" );
                    return FAILED;
                }
            } catch (UnknownHostException e) {
                return ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            switch (i){
                case WORKED:
                    Intent Start = new Intent(StartParty.this, HostList.class);
                    StartParty.this.startActivity(Start);
                    finish();
                    break;
                case FAILED :
                    Toast t = Toast.makeText(getApplicationContext(), "Failed to start playlist", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                case ERROR:
                    Toast e = Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT);
                    e.show();
                default:
                    break;
            }
        }
    }

    public String getIpAddr() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        String ipString = String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return ipString;
    }

}

