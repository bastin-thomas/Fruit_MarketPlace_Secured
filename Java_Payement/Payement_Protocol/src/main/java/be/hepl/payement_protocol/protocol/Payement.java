/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package be.hepl.payement_protocol.protocol;

import be.hepl.generic_server_tcp.Exceptions.EndConnectionException;
import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import be.hepl.generic_server_tcp.Request;
import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.protocol.request.LoginRequest;
import be.hepl.payement_protocol.protocol.request.LogoutRequest;
import be.hepl.payement_protocol.protocol.response.LoginResponse;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Arkios
 */
public class Payement implements Protocol 
{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final Logger logger;
    private HashMap<String, Boolean> connectedClients;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Payement(Logger log)
    {
        logger = log;
        connectedClients = new HashMap<>();
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
            return LogoutRequestTreatment(logoutRequest, socket);
        }
        
        
        return null;
    }
    // </editor-fold>
    
    
    /*
    « Get Facture » idFacture       idFacture, date, montant, payé, Liste<article>          Permettrait de récupérer l’ensemble des articles 
                                                                                            concernant une facture dont on fournirait l’id au serveur.
    */
    
    /*
    « Get Factures » idClient       Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                       factures du client dans la table
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
        connectedClients.put(loginRequest.getLogin(), true);
        
        return new LoginResponse(true);
    }

    
    
    
    /***
     * « Logout »
     * @param logoutRequest
     * @param socket
     * @return 
     */
    private Response LogoutRequestTreatment(LogoutRequest logoutRequest, Socket socket) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
