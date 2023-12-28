package com.mymaraichermobile.model;

import android.app.Application;

public class SocketHandler extends Application {
    private static ProtocoleClient client;

    public static synchronized ProtocoleClient getProtocol(){
        return client;
    }

    public static synchronized void setProtocol(ProtocoleClient client) {
        SocketHandler.client =  client;
    }
}
