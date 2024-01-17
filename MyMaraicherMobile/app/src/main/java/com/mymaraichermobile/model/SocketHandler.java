package com.mymaraichermobile.model;

import android.app.Application;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class SocketHandler extends Application {
    private static ProtocoleClient client;

    public static synchronized ProtocoleClient getProtocol(){
        return client;
    }

    public static synchronized void setProtocol(ProtocoleClient client) {
        SocketHandler.client =  client;
    }
}
