/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.HttpServer;

import be.hepl.cryptolibrary.TLSUtils;
import be.hepl.generic_server_tcp.Logger;
import be.hepl.stockmanagement.Utils.DBStock;
import java.io.IOException;
import java.security.KeyStore;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Arkios
 */
public class StockManagement_HttpsServer extends StockManagement_HttpServer{
    public StockManagement_HttpsServer(String ip, int port, int nthread, int maxPending, Logger log, DBStock db, String CypherSuit, String TLSVersion, String SecurityTLSProvider, KeyStore store, String keyStorePassword) throws IOException, Exception {
        super(log, db);
        
        http = TLSUtils.createWebServerSocket(port, maxPending, CypherSuit, TLSVersion, SecurityTLSProvider, store, keyStorePassword);
        
        Executor executor = Executors.newFixedThreadPool(nthread);
        http.setExecutor(executor);
        
        this.log.Trace("Lancement du serveur HTTPS sur les ports: " + ip + ":" + port);
        
        http.createContext("/", new IndexHandler(log, db));
        http.createContext("/api/v1/", new ApiHandler(log, db));
        
        http.start();
        
        this.log.Trace("Serveur HTTPS lancé sur les ports: " + ip + ":" + port);
    }
    
    @Override
    public void close() {
        http.stop(0);
    }
}