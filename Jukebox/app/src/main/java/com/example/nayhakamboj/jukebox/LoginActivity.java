package com.example.nayhakamboj.jukebox;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.example.nayhakamboj.jukebox.client.Client;
import com.example.nayhakamboj.jukebox.client.ServerConnector;

import java.io.IOException;

//import client.Client;
//import client.ServerConnector;
//import server.Server;

public class LoginActivity extends AppCompatActivity {
    private JukeApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (JukeApplication)getApplication();
        final TextView register = (TextView) findViewById(R.id.textView2);
        //final TextView guestregister = (TextView) findViewById(R.id.textView3);
        final Button signin = (Button) findViewById(R.id.signInButton);
        //final TextView tv = (TextView) findViewById(R.id.textView);
        final EditText username = (EditText) findViewById(R.id.editText3);
        final EditText pass = (EditText) findViewById(R.id.editText3);

        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
        register.setTypeface(fontBiko);
        signin.setTypeface(fontBiko);
        //tv.setTypeface(fontBiko);
        username.setTypeface(fontBiko);
        pass.setTypeface(fontBiko);
        //guestregister.setTypeface(fontBiko);
        signin.setTextColor(Color.WHITE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerlogin = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerlogin);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText)findViewById(R.id.editText3);
                String userName = username.getText().toString();
                EditText password = (EditText)findViewById(R.id.editText4);
                String passWord = password.getText().toString();

                new SigninClass().execute(userName,passWord);
            }
        });

        final TextView guest = (TextView) findViewById(R.id.textView);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GuestLogin().execute();
            }
        });

    }

    private class SigninClass extends AsyncTask<Object, Void, Integer> {
        private static final int NOSERVER = -1;
        private static final int NOUSER = 1;
        private static final int WORKED = 0;

        @Override
        protected Integer doInBackground(Object... params) {
            ServerConnector sc;
            try {
                sc = new ServerConnector("159.203.212.48", 7654);
                String userName = (String)params[0];
                String passWord = (String) params[1];
                if(sc.login(userName, passWord)) {
                    Client client = new Client(sc, userName);
                    app.setClient(client); app.setServer(sc);
                    return WORKED;
                }
                else {
                    return NOUSER;
                }
            } catch (IOException ioe) {
                return NOSERVER;
            }
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            switch (i){
                case WORKED:
                    Intent login = new Intent(LoginActivity.this, StartJoinPartyMainMenu.class);
                    LoginActivity.this.startActivity(login);
                    finish();
                    break;
                case NOUSER :
                    Toast t = Toast.makeText(getApplicationContext(), "Username/Password combo is invalid", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                case NOSERVER:
                    Toast d =  Toast.makeText(getApplicationContext(), "Couldn't connect to server", Toast.LENGTH_SHORT);
                    d.show();
                    break;
                default:
                    break;
            }
        }
    }

    private class GuestLogin extends AsyncTask<Void, Void, Integer> {
        private static final int NOSERVER = -1;
        private static final int WORKED = 0;

        @Override
        protected Integer doInBackground(Void... params) {
            ServerConnector sc;
            try {
                sc = new ServerConnector("159.203.212.48", 7654);
                Client client = new Client(sc, null);
                app.setClient(client); app.setServer(sc);
                return WORKED;
            } catch (IOException ioe) {
                return NOSERVER;
            }
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            switch (i){
                case WORKED:
                    Intent guestlogin = new Intent(LoginActivity.this, GuestMainMenu.class);
                    LoginActivity.this.startActivity(guestlogin);
                    finish();
                    break;
                case NOSERVER:
                    Toast d =  Toast.makeText(getApplicationContext(), "Couldn't connect to server", Toast.LENGTH_SHORT);
                    d.show();
                    break;
                default:
                    break;
            }
        }
    }
}
