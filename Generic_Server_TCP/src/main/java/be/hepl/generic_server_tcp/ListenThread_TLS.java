/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp;

import be.hepl.cryptolibrary.TLSUtils;
import java.io.IOException;
import java.security.KeyStore;

/**
 *
 * @author Arkios
 */
public abstract class ListenThread_TLS extends ListenThread {
    
    public ListenThread_TLS(int port, Protocol protocole, Logger logger, String cypherSuit, String sslVersion, String provider, KeyStore store, String keystorePassword)
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        
        try{            
            listenSocket = TLSUtils.createServerSocket(port, cypherSuit, sslVersion, provider, store, keystorePassword);
        }
        catch(Exception ex)
        {
            logger.Trace("Error Creation Socket: " + ex.getMessage());
        }
    }
    
    public void close()
    {
        try {
            listenSocket.close();
        } catch (IOException ex) {
            logger.Trace(ex.getMessage());
        }
    }
}
