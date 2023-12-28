package com.mymaraichermobile.model;

import android.app.Application;

import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded.ProtocoleClientThreaded;

public class SocketHandler extends Application {
    private static ProtocoleClientThreaded client;

    public static synchronized ProtocoleClientThreaded getProtocol(){
        return client;
    }

    public static synchronized void setProtocol(ProtocoleClientThreaded client) {
        SocketHandler.client =  client;
    }
}
