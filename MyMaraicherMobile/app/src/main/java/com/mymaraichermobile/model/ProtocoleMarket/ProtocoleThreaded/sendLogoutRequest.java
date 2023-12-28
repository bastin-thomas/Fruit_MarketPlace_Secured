package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendLogoutRequest extends RequestTemplate {

    public sendLogoutRequest(ProtocoleClient prot){
        super(prot);
    }

    @Override
    public void run() {
        try {
            prot.sendLogout();
        } catch (Exception e) {
            throwedException = e;
        }
    }
}
