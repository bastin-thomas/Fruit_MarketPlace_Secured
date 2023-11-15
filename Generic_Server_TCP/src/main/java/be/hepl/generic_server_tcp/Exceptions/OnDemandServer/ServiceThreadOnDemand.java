/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.Exceptions.OnDemandServer;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import be.hepl.generic_server_tcp.ServiceThread;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Arkios
 */
public class ServiceThreadOnDemand extends ServiceThread {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ServiceThreadOnDemand(Protocol protocole, Socket csocket, Logger logger) throws IOException 
    {
        super(protocole, csocket, logger);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public void run() {
        logger.Trace("TH Client (Demande) démarre...");
        super.run();
        logger.Trace("TH Client (Demande) se termine.");
    }
    // </editor-fold>    
}
