package com.example.nayhakamboj.jukebox;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class StartJoinPartyMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_join_party_main_menu);


        final Button startParty = (Button) findViewById(R.id.startPartyButton);
        final Button joinParty = (Button) findViewById(R.id.joinPartyButton);
        //assert joinParty != null;

        Typeface fontBiko = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.ttf");
        startParty.setTypeface(fontBiko);
        joinParty.setTypeface(fontBiko);
        startParty.setTextColor(Color.WHITE);
        joinParty.setTextColor(Color.WHITE);

        startParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startparty = new Intent(StartJoinPartyMainMenu.this, StartParty.class);
                StartJoinPartyMainMenu.this.startActivity(startparty);
            }
        });

        joinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinparty = new Intent(StartJoinPartyMainMenu.this, JoinPartyActivity.class);
                StartJoinPartyMainMenu.this.startActivity(joinparty);
            }
        });
    }
}
