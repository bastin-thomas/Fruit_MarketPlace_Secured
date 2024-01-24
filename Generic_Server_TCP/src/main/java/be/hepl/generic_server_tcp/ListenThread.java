/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp;

import be.hepl.cryptolibrary.TLSUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyStore;

/**
 *
 * @author Arkios
 */
public abstract class ListenThread extends Thread {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected int port;
    protected Protocol protocole;
    protected Logger logger;
    protected ServerSocket serverSocket;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ListenThread(String name){
        super(name);
    }
    
    public ListenThread(int port, Protocol protocole, Logger logger)
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");

        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        
        try{
            serverSocket = new ServerSocket(port);
        }
        catch(IOException ex)
        {
            logger.Trace("Error Creation Socket: " + ex.getMessage());
        }
    }
    
    
    //Secure Constructor
    public ListenThread(int port, Protocol protocole, Logger logger, String cypherSuit, String sslVersion, String provider, KeyStore store, String keystorePassword)
    {
        super("TH_Secured Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        
        try{            
            serverSocket = TLSUtils.createServerSocket(port, cypherSuit, sslVersion, provider, store, keystorePassword);
        }
        catch(Exception ex)
        {
            logger.Trace("Error Creation Socket: " + ex.getMessage());
        }
    }
    
    public void close()
    {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            logger.Trace(ex.getMessage());
        }
    }
    // </editor-fold>
}
