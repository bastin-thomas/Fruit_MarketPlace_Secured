/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.PooledServer;

import be.hepl.generic_server_tcp.ListenThread;
import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Arkios
 */
public class ListenThreadPooled extends ListenThread {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private WaitingList waitingConnexions;
    private ThreadGroup pool;
    private int poolSize;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ListenThreadPooled(int port, Protocol protocol, Logger logger, int poolSize) throws IOException
    {
        super(port, protocol, logger);
        
        waitingConnexions = new WaitingList();
        pool = new ThreadGroup("POOL");
        this.poolSize = poolSize;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public void run(){
        logger.Trace("Démarrage du TH Listen (Pool)...");
        // Création du pool de threads
        try{
            for (int i=0 ; i<poolSize ; i++)
            {
                ServiceThreadPooled sThread = new ServiceThreadPooled(protocole,
                                                                        waitingConnexions,pool,logger);
                sThread.start();
            }
        } catch (IOException ex) {
            logger.Trace("Erreur I/O lors de la création du pool de threads");
            return;
        }

        // Attente des connexions
        while(!this.isInterrupted()){
            Socket serviceSocket;
            try{
                serverSocket.setSoTimeout(2000);
                serviceSocket = serverSocket.accept();
                logger.Trace("Connexion acceptée, mise en file d'attente.");
                waitingConnexions.addConnexion(serviceSocket);
            }
            catch (SocketTimeoutException ex)
            {
                // Pour vérifier si le thread a été interrompu
            }
            catch (IOException ex)
            {
                logger.Trace("Erreur I/O");
            }
        }
        
        pool.interrupt();
        this.close();
        logger.Trace("TH Listen (Pool) se termine.");
    }
    // </editor-fold>
}
