package com.mymaraichermobile.model.ProtocoleThreaded;

import com.mymaraichermobile.model.Achats;
import com.mymaraichermobile.model.ProtocoleClient;

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
