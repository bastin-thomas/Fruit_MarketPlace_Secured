/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.OnDemandServer;

import be.hepl.cryptolibrary.TLSUtils;
import be.hepl.generic_server_tcp.ListenThread;
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
public class ListenThreadOnDemand extends ListenThread {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ListenThreadOnDemand(int port, Protocol protocole, Logger logger) throws IOException {
        super(port, protocole, logger);
    }
    
    
    //Secure Constructor
    public ListenThreadOnDemand(int port, Protocol protocole, Logger logger, String cypherSuit, String sslVersion, String provider, KeyStore store, String keystorePassword)
    {
        super(port, protocole, logger, cypherSuit, sslVersion, provider, store, keystorePassword);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public void run() {
        logger.Trace("Démarrage du TH Listen (Demande)...");
        while (!this.isInterrupted()) {
            Socket csocket;
            try {
                serverSocket.setSoTimeout(2000);
                csocket = serverSocket.accept();
                logger.Trace("Connexion acceptée, création TH Client");
                Thread th = new ServiceThreadOnDemand(protocole, csocket, logger);
                th.start();
            } catch (SocketTimeoutException ex) {
                // Pour vérifier si le thread a été interrompu
            } catch (Exception ex) {
                logger.Trace("Erreur : " + ex.getMessage());
            }
        }
        logger.Trace("TH Listen (Demande) interrompu.");
        try {
            serverSocket.close();
        } catch (IOException ex) {
            logger.Trace("Erreur I/O");
        }
    }
    // </editor-fold>
}
