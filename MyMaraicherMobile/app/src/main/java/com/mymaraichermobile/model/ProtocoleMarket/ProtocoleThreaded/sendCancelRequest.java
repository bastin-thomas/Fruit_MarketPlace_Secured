package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendCancelRequest extends RequestTemplate {
    private final int idArticle;

    public sendCancelRequest(ProtocoleClient prot, int idArticle){
        super(prot);
        this.idArticle = idArticle;
    }

    @Override
    public void run() {
        try {
            prot.sendCancel(idArticle);
        } catch (Exception e) {
            throwedException = e;
        }
    }
}
