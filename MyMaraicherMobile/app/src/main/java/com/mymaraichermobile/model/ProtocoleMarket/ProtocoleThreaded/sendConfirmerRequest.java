package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendConfirmerRequest extends RequestTemplate {
    private final String nom;

    public sendConfirmerRequest(ProtocoleClient prot, String nom){
        super(prot);
        this.nom = nom;
    }

    @Override
    public void run() {
        try {
            prot.sendConfirmer(nom);
        } catch (Exception e) {
            throwedException = e;
        }
    }
}
