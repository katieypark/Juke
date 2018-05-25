package com.example.nayhakamboj.jukebox;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;

import com.example.nayhakamboj.jukebox.client.Client;
import com.example.nayhakamboj.jukebox.client.ServerConnector;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private JukeApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        app = (JukeApplication)getApplication();

        //get all the views
        final Button registerB = (Button) findViewById(R.id.button2);
        final EditText usernameE = (EditText) findViewById(R.id.editText5);
        final EditText emailE = (EditText) findViewById(R.id.editText6);
        final EditText passE = (EditText) findViewById(R.id.editText7);
        final EditText repeatE = (EditText) findViewById(R.id.editText8);

        //set fonts
        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
        registerB.setTypeface(fontBiko);
        usernameE.setTypeface(fontBiko);
        emailE.setTypeface(fontBiko);
        passE.setTypeface(fontBiko);
        repeatE.setTypeface(fontBiko);
        registerB.setTextColor(Color.WHITE);

        //disable autocorrect



        //add actionlistener

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameE.getText().toString();
                String email = emailE.getText().toString();
                String pass = passE.getText().toString();
                String repeat = repeatE.getText().toString();
                //Perform any checks on validity eg pass and repeat match
                if (username.trim().equals("") || email.trim().equals("") || pass.trim().equals("") || repeat.trim().equals("")) {
                    Toast t = Toast.makeText(getApplicationContext(), "Signup Failed, Missing Information", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (username.trim().length() < 4) {
                    Toast t = Toast.makeText(getApplicationContext(), "Signup Failed, Username Too Short", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast t = Toast.makeText(getApplicationContext(), "Signup Failed, Invalid Email", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (pass.trim().length() < 4) {
                    Toast t = Toast.makeText(getApplicationContext(), "Signup Failed, Password Too Short", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (!pass.trim().equals(repeat.trim())) {
                    Toast t = Toast.makeText(getApplicationContext(), "Signup Failed, Passwords Do Not Match", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                new SigninClass().execute(username,pass,email);
            }
        });


    }

    //Rama: I only implemented the connection to the server. Everything else is on you guys. TODO

    private class SigninClass extends AsyncTask<Object, Void, Integer> {
        private static final int NOSERVER = -1;
        private static final int FAILED = 1;
        private static final int WORKED = 0;
        private static final int MISMATCH = 2;

        @Override
        protected Integer doInBackground(Object... params) {
            ServerConnector sc;
            try {
                String hostname = "159.203.212.48";
                sc = new ServerConnector(hostname, 7654);
                String userName = (String)params[0];
                String passWord = (String) params[1];
                String email = (String) params[2];

                if(sc.createUser(userName,passWord,email)) {
                    Client client = new Client(sc, userName);
                    app.setClient(client); app.setServer(sc);
                    return WORKED;
                }
                else {
                    return FAILED;
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
                    Intent signup = new Intent(RegisterActivity.this, StartJoinPartyMainMenu.class);
                    signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    RegisterActivity.this.startActivity(signup);
                    finish();
                    break;
                case FAILED :
                    Toast t = Toast.makeText(getApplicationContext(), "Signup Failed. User already exists", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                case NOSERVER:
                    Toast d =  Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_SHORT);
                    d.show();
                    break;
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




