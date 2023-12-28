package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendCreateLoginRequest extends RequestTemplate {
    private final String nom;
    private final String mdp;

    public sendCreateLoginRequest(ProtocoleClient prot, String nom, String mdp){
        super(prot);
        this.nom = nom;
        this.mdp = mdp;
    }

    @Override
    public void run() {
        try {
            prot.sendCreateLogin(nom,mdp);
        } catch (Exception e) {
            throwedException = e;
        }
    }
}
