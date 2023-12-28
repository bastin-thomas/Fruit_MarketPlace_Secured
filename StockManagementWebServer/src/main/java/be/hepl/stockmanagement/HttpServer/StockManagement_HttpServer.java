/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.HttpServer;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.stockmanagement.Utils.DBStock;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Arkios
 */
public class StockManagement_HttpServer{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected HttpServer http;
    protected DBStock db;
    protected Logger log;
    // </editor-fold>
    
    public StockManagement_HttpServer(Logger log, DBStock db){
        this.db = db;
        this.log = log;
        this.http = null;
    }
    
    public StockManagement_HttpServer(String ip, int port, int nthread, int maxPending, Logger log, DBStock db) throws IOException {
        this.log = log;
        this.db = db;
        
        InetSocketAddress addr = new InetSocketAddress(ip, port);
        http = HttpServer.create(addr, maxPending);
        
        Executor executor = Executors.newFixedThreadPool(nthread);
        http.setExecutor(executor);
        
        this.log.Trace("Lancement du serveur HTTP sur les ports: " + ip + ":" + port);
        
        http.createContext("/", new IndexHandler(log, db));
        http.createContext("/api/v1/", new ApiHandler(log, db));
        http.start();
        
        this.log.Trace("Serveur HTTP lancé sur les ports: " + ip + ":" + port);
    }
    
    public void close() {
        http.stop(0);
    }
}
