package com.example.nayhakamboj.jukebox;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Typeface;
import android.content.Context;

public class GuestMainMenu extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main_menu);

        final Button joinParty = (Button) findViewById(R.id.joinPartyButton);
        //assert joinParty != null;

        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
        joinParty.setTypeface(fontBiko);

        joinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView user = (TextView) findViewById(R.id.editText2);

                //if (mClient.joinPlaylist(userName)) {
                Intent joinparty = new Intent(GuestMainMenu.this, JoinPartyActivity.class);
                GuestMainMenu.this.startActivity(joinparty);
                //}
                //else {
                //error popup message for user
                AlertDialog.Builder errormessage = new AlertDialog.Builder(GuestMainMenu.this);
                errormessage.setTitle("Join Party Error");
                errormessage.setMessage("Could not join playlist!");
                errormessage.show();
                //}
            }
        });
    }

}
