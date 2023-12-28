package com.mymaraichermobile.model.ProtocoleThreaded;

import com.mymaraichermobile.model.ProtocoleClient;

import kotlin.NotImplementedError;

public abstract class RequestTemplate implements Runnable {
    protected final ProtocoleClient prot;
    protected Exception throwedException;

    public RequestTemplate(ProtocoleClient prot) {
        this.prot = prot;
        this.throwedException = null;
    }

    public boolean isExceptionRaised(){
        return throwedException == null ? false : true;
    }

    public Exception getThrowedException(){
        return throwedException;
    }
}
