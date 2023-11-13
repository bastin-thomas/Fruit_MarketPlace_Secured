/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package be.hepl.payement_protocol.protocol;

import be.hepl.generic_server_tcp.Exceptions.EndConnectionException;
import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import be.hepl.generic_server_tcp.Request;
import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.Utils.DBPayement;
import be.hepl.payement_protocol.protocol.request.LoginRequest;
import be.hepl.payement_protocol.protocol.request.LogoutRequest;
import be.hepl.payement_protocol.protocol.response.LoginResponse;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Arkios
 */
public class Payement implements Protocol 
{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    public final DBPayement db;
    private final Logger logger;
    private HashMap<String, Boolean> connectedClients;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Payement(Logger log, be.hepl.payement_protocol.Utils.DBPayement db)
    {
        logger = log;
        connectedClients = new HashMap<>();
        this.db = db;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    @Override
    public String getNom() {
        return "Payement";
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public synchronized Response RequestTreatment(Request requete, Socket socket) throws EndConnectionException 
    {
        if(requete instanceof LoginRequest loginRequest)
        {
            return LoginRequestTreatment(loginRequest, socket);
        }
        
        if(requete instanceof LogoutRequest logoutRequest)
        {
            LogoutRequestTreatment(logoutRequest, socket);
        }
        
        return null;
    }
    
    /*
    « Get Facture » idFacture         idFacture, date, montant, payé, Liste<article>   Permettrait de récupérer l’ensemble des articles 
                                                                                concernant une facture dont on fournirait l’id au serveur.
    */
    
    /*
    « Get Factures » idClient         Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                  factures du client dans la table
                                                                                       factures (sans le contenu détaillé
                                                                                       de la commande donc)
    */
    
    /*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                                   (carte VISA invalide)    la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
    */
    
    
    /***
     * « Login »   Login, password       Oui ou non              Vérification du login et du mot
     *             (d’un employé)                                passe dans la table des employés
     * 
     * @param loginRequest
     * @param socket
     * @return 
     */
    private Response LoginRequestTreatment(LoginRequest loginRequest, Socket socket) {
        boolean logged = false;
        String message = "";
        String response = "";
        
        
        try {
            logged = db.Login(loginRequest.getLogin(), loginRequest.getPassword());
        } catch (Exception ex) {
            switch(ex.getMessage())
            {
                case "SQL_ERROR" -> {
                    response = "SQL_ERROR";
                    message = ex.getMessage();
                }
                
                case "NO_LOGIN" -> {
                    response = "NO_LOGIN";
                    message = ex.getMessage();
                }
                
                case "BAD_LOGIN" -> {
                    response = "BAD_LOGIN";
                    message = ex.getMessage();
                }
                
                default ->{
                    response = "UNKOWN";
                    message = ex.getMessage();
                }
            }
        }
        
        
        if(logged){
            connectedClients.put(loginRequest.getLogin(), true);
            return new LoginResponse(true);
        } else {
            return new LoginResponse(false, (response + ": " + message));
        }   
    }
    
    
    
    /***
     * « Logout »
     * @param logoutRequest
     * @param socket
     * @return 
     */
    private void LogoutRequestTreatment(LogoutRequest logoutRequest, Socket socket) {
        if(connectedClients.get(logoutRequest.getLogin()) == true)
        {
            connectedClients.remove(logoutRequest.getLogin());
        }
        
    }
    
    // </editor-fold>
}
