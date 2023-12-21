/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp;

import be.hepl.generic_server_tcp.TLSUtils.TLSUtils;
import java.io.IOException;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocket;
import java.security.KeyStore;
import java.util.logging.Level;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author Arkios
 */
public abstract class ListenThread_TLS extends ListenThread {
    
    public ListenThread_TLS(int port, Protocol protocole, Logger logger, String sslVersion, String provider, KeyStore store, String keystorePassword)
    {
        super(port, protocole, logger);
        logger.Trace("TH Serveur_SSL (port=" + port + ",protocole=" + protocole.getNom() + ")");
        
        
        try{
            SSLContext tlsContext = TLSUtils.getTLSContext(sslVersion, provider, store, keystorePassword);
            SSLServerSocketFactory SslSFac= tlsContext.getServerSocketFactory();
            listenSocket = (SSLServerSocket) SslSFac.createServerSocket(port);
            ((SSLServerSocket)listenSocket).setNeedClientAuth(true);
        }
        catch(Exception ex)
        {
            logger.Trace("Error Creation Socket: " + ex.getMessage());
        }
    }
}
