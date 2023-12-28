package com.mymaraichermobile.model.ProtocoleThreaded;

import com.mymaraichermobile.model.CaddieRows;
import com.mymaraichermobile.model.ProtocoleClient;

import java.util.ArrayList;

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
