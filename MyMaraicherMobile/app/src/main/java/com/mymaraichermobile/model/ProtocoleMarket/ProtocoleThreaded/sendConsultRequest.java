package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.Articles;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

public class sendConsultRequest extends RequestTemplate {
    private final int idArticle;
    private Articles article;

    public sendConsultRequest(ProtocoleClient prot, int idArticle){
        super(prot);
        this.article = null;
        this.idArticle = idArticle;
    }

    @Override
    public void run() {
        try {
            this.article = prot.sendConsult(idArticle);
        } catch (Exception e) {
            throwedException = e;
        }
    }

    public Articles getArticle() {
        return article;
    }
}
