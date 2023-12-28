package com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded;

import com.mymaraichermobile.model.Achats;
import com.mymaraichermobile.model.Articles;
import com.mymaraichermobile.model.CaddieRows;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;

import java.util.ArrayList;

public class ProtocoleClientThreaded {
    //region Properties
    protected ProtocoleClient prot;
    //endregion

    //region Constructors
    public ProtocoleClientThreaded(ProtocoleClient prot) {
        this.prot = prot;
    }
    //endregion

    //region Getters
    public ProtocoleClient getProt() {
        return prot;
    }
    //endregion

    //region Protocol Methods
    public void close() throws Exception{
        prot.close();
    }


    public void sendLogin(String nom, String mdp) throws Exception {
        sendLoginRequest request = new sendLoginRequest(prot, nom, mdp);
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }
    }


    public void sendCreateLogin(String nom, String mdp) throws Exception {
        sendCreateLoginRequest request = new sendCreateLoginRequest(prot, nom, mdp);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }
    }


    public Articles sendConsult(int idArticle) throws Exception {
        sendConsultRequest request = new sendConsultRequest(prot, idArticle);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }

        return request.getArticle();
    }


    public Achats sendAchat(int idArticle, int quantitee) throws Exception {
        sendAchatRequest request = new sendAchatRequest(prot, idArticle, quantitee);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }

        return request.getAchat();
    }


    public ArrayList<CaddieRows> sendCaddie() throws Exception {
        sendCaddieRequest request = new sendCaddieRequest(prot);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }

        return request.getCaddie();
    }


    public void sendCancel(int idArticle) throws Exception {
        sendCancelRequest request = new sendCancelRequest(prot, idArticle);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }
    }


    public void sendCancelAll() throws Exception {
        sendCancelAllRequest request = new sendCancelAllRequest(prot);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }
    }


    public void sendConfirmer(String nom) throws Exception {
        sendConfirmerRequest request = new sendConfirmerRequest(prot, nom);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }
    }


    public void sendLogout() throws Exception {
        sendLogoutRequest request = new sendLogoutRequest(prot);

        //Execute and wait the thread
        Thread handler = new Thread(request);
        handler.start();
        handler.join();

        if(request.isExceptionRaised()){
            throw request.getThrowedException();
        }
    }

    //endregion
}
