package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendCancelAllRequest extends RequestTemplate {

    public sendCancelAllRequest(ProtocoleClient prot){
        super(prot);
    }

    @Override
    public void run() {
        try {
            prot.sendCancelAll();
        } catch (Exception e) {
            throwedException = e;
        }
    }
}
