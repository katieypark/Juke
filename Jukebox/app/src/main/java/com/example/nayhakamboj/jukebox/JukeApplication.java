package com.example.nayhakamboj.jukebox;

import android.app.Application;

import com.example.nayhakamboj.jukebox.client.Client;
import com.example.nayhakamboj.jukebox.client.ServerConnector;

/**
 * Created by Chris Cognetta on 4/16/2016.
 */
public class JukeApplication extends Application {
    private ServerConnector sc;

    private Client client;

    public void setServer(ServerConnector sc) {
        this.sc = sc;
    }

    public void setClient(Client client) {
        this.client = client;

    }

    public ServerConnector getServer() {
        return sc;
    }

    public Client getClient() {
        return client;
    }
/*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
*/
}
