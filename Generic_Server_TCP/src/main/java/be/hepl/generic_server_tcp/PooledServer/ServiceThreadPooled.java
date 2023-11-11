/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.PooledServer;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import be.hepl.generic_server_tcp.ServiceThread;
import java.io.IOException;

/**
 *
 * @author Arkios
 */
public class ServiceThreadPooled extends ServiceThread {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final WaitingList waitingConnexions;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ServiceThreadPooled(Protocol protocol, WaitingList list, ThreadGroup group, Logger logger) throws IOException {
        super(protocol, group, logger);
        waitingConnexions = list;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public void run()
    {
        logger.Trace("TH Client (Pool) démarre...");
        boolean interrompu = false;
        
        while(!interrompu){
            try{
                logger.Trace("Attente d'une connexion...");
                serviceSocket = waitingConnexions.getConnexion();
                logger.Trace("Connexion prise en charge.");
                
                super.start();
            } catch (InterruptedException ex) {                
                logger.Trace("Demande d'interruption...");
                interrompu = true;
            }
        }
        logger.Trace("TH Client (Pool) se termine.");
    }

    // </editor-fold>
}
