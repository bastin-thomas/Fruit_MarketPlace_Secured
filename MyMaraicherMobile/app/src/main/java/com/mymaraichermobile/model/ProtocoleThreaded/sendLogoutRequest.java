package com.mymaraichermobile.model.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleClient;

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
