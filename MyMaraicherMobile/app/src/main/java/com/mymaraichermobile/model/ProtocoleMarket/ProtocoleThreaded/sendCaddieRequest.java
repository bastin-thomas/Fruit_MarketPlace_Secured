package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.CaddieRows;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

import java.util.ArrayList;

public class sendCaddieRequest extends RequestTemplate {
    private ArrayList<CaddieRows> caddie;

    public sendCaddieRequest(ProtocoleClient prot){
        super(prot);
        this.caddie = null;
    }

    @Override
    public void run() {
        try {
            this.caddie = prot.sendCaddie();
        } catch (Exception e) {
            throwedException = e;
        }
    }

    public ArrayList<CaddieRows> getCaddie() {
        return caddie;
    }
}
