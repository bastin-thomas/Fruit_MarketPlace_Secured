package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.Achats;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendAchatRequest extends RequestTemplate {
    private final int idArticle;
    private final int quantitee;
    private Achats achat;

    public sendAchatRequest(ProtocoleClient prot, int idArticle, int quantitee){
        super(prot);
        this.achat = null;
        this.idArticle = idArticle;
        this.quantitee = quantitee;
    }

    @Override
    public void run() {
        try {
            this.achat = prot.sendAchat(idArticle, quantitee);
        } catch (Exception e) {
            throwedException = e;
        }
    }

    public Achats getAchat() {
        return achat;
    }
}
