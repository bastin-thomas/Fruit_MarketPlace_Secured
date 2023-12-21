/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.Exceptions.OnDemandServer;

import be.hepl.generic_server_tcp.ListenThread_TLS;
import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyStore;

/**
 *
 * @author Arkios
 */
public class ListenThreadOnDemand_TLS extends ListenThread_TLS {
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ListenThreadOnDemand_TLS(int port, Protocol protocole, Logger logger, String sslVersion, String provider, KeyStore store, String keystorePassword) throws IOException {
        super(port, protocole, logger, sslVersion, provider, store, keystorePassword);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public void run() {
        logger.Trace("Démarrage du TH_TLS Listen (Demande)...");
        while (!this.isInterrupted()) {
            Socket csocket;
            try {
                listenSocket.setSoTimeout(2000);
                csocket = listenSocket.accept();
                logger.Trace("Connexion acceptée, création TH_TLS Client");
                Thread th = new ServiceThreadOnDemand(protocole, csocket, logger);
                th.start();
            } catch (SocketTimeoutException ex) {
                // Pour vérifier si le thread a été interrompu
            } catch (IOException ex) {
                logger.Trace("Erreur I/O");
            }
        }
        logger.Trace("TH_TLS Listen (Demande) interrompu.");
        try {
            listenSocket.close();
        } catch (IOException ex) {
            logger.Trace("Erreur I/O");
        }
    }
    // </editor-fold>
}
