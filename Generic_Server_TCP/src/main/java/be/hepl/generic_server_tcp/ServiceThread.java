/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp;

import be.hepl.generic_server_tcp.Exceptions.EndConnectionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Arkios
 */
public abstract class ServiceThread extends Thread {

    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected Protocol protocole;
    protected Socket serviceSocket;
    protected Logger logger;

    private int numero;
    private static int numCourant = 1;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Thread de gestion d'une connexion avec le client
     * @param protocole
     * @param csocket
     * @param logger
     * @throws IOException
     */
    public ServiceThread(Protocol protocole, Socket csocket, Logger logger) throws IOException
    {
        super("TH Service " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.serviceSocket = csocket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    /**
     * Thread de gestion d'une connexion avec le client
     * @param protocole
     * @param groupe
     * @param logger
     * @throws IOException
     */
    public ServiceThread(Protocol protocole, ThreadGroup groupe, Logger logger) throws IOException
    {
        super(groupe,"TH Service " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.serviceSocket = null;
        this.logger = logger;
        this.numero = numCourant++;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public void run()
    {
        try{
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try{
                ois = new ObjectInputStream(serviceSocket.getInputStream());
                oos = new ObjectOutputStream(serviceSocket.getOutputStream());

                while (true){
                    Request requete = (Request) ois.readObject();
                    Response reponse = protocole.RequestTreatment(requete,serviceSocket);
                    oos.writeObject(reponse);
                }
            } catch (EndConnectionException ex){
                logger.Trace("Fin connexion demandée par protocole");
                if (oos != null && ex.getResponse() != null)
                oos.writeObject(ex.getResponse());
            }
        } catch (IOException ex) { 
            logger.Trace("Erreur I/O"); 
        } catch (ClassNotFoundException ex) { 
            logger.Trace("Erreur requete invalide");
        }
        
        finally {
            try { 
                serviceSocket.close(); 
            } catch (IOException ex) { 
                logger.Trace("Erreur fermeture socket"); 
            }
        }
    }

    // </editor-fold>
}
