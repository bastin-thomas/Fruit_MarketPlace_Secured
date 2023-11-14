/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Arkios
 */
public abstract class ListenThread extends Thread {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected int port;
    protected Protocol protocole;
    protected Logger logger;
    protected ServerSocket listenSocket;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ListenThread(int port, Protocol protocole, Logger logger)
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");

        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        
        try{
            listenSocket = new ServerSocket(port);
        }
        catch(IOException ex)
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
    // </editor-fold>
}
