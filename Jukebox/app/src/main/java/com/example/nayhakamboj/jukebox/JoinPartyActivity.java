package com.example.nayhakamboj.jukebox;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;


//import android.support.v7.app.AlertController;
//some small change
public class JoinPartyActivity extends AppCompatActivity {

    private JukeApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_party2);
        app = (JukeApplication)getApplication();

        Button joinParty = (Button) findViewById(R.id.button3);
        TextView username =(TextView) findViewById(R.id.usernameText);
        EditText enter = (EditText) findViewById(R.id.editText2);

        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
        joinParty.setTypeface(fontBiko);
        username.setTypeface(fontBiko);
        enter.setTypeface(fontBiko);

        joinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView user = (TextView) findViewById(R.id.editText2);
                String userName = user.getText().toString();
                new JoinPlaylistTask().execute(userName);
            }
        });
    }

    private class JoinPlaylistTask extends AsyncTask<Object, Void, Integer> {
        private static final int ERROR = -1;
        private static final int FAILED = 1;
        private static final int WORKED = 0;

        @Override
        protected Integer doInBackground(Object... params) {

            String username = (String) params[0];
                if(app.getClient().joinPlaylist(username)) {
                    Log.d("debug","WORKED");
                    return WORKED;
                }
                else {
                    Log.d("debug","FAILED" );
                    return FAILED;
                }
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            switch (i){
                case WORKED:
                    //TODO
                    Log.d("debug","PRE JOIN PARTY");
                    Intent joinparty = new Intent(JoinPartyActivity.this, SongListActivity.class);
                    JoinPartyActivity.this.startActivity(joinparty);
                    Log.d("debug", "POST JOIN PARTY");
                    break;
                case FAILED :
                    Toast t = Toast.makeText(getApplicationContext(), "Failed to join playlist", Toast.LENGTH_SHORT);
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


}
