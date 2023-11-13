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
import be.hepl.payement_protocol.model.Facture;
import be.hepl.payement_protocol.model.Sale;
import be.hepl.payement_protocol.protocol.request.GetFacturesRequest;
import be.hepl.payement_protocol.protocol.request.GetSalesRequest;
import be.hepl.payement_protocol.protocol.request.LoginRequest;
import be.hepl.payement_protocol.protocol.request.LogoutRequest;
import be.hepl.payement_protocol.protocol.response.GetFacturesResponse;
import be.hepl.payement_protocol.protocol.response.GetSalesResponse;
import be.hepl.payement_protocol.protocol.response.LoginResponse;
import java.net.Socket;
import java.util.ArrayList;
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
        
        if(requete instanceof GetSalesRequest request)
        {
            return GetSalesRequestTreatment(request, socket);
        }
        
        if(requete instanceof GetFacturesRequest request)
        {
            return GetFacturesRequestTreatment(request, socket);
        }
        
        if(requete instanceof LogoutRequest logoutRequest)
        {
            LogoutRequestTreatment(logoutRequest, socket);
        }
        
        return null;
    }
    
    /*
    « Get Sales » idFacture         Liste<article>   Permettrait de récupérer l’ensemble des articles 
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
    
    private Response GetSalesRequestTreatment(GetSalesRequest request, Socket socket) {
        ArrayList<Sale> Sales = new ArrayList<>();
        String message = "";
        String response = "";
        
        try {
            Sales = db.GetSales(request.getIdFacture());
        } catch (Exception ex) {
            switch(ex.getMessage())
            {
                case "SQL_ERROR" -> {
                    response = "SQL_ERROR";
                    message = ex.getMessage();
                }
                
                default ->{
                    response = "UNKOWN";
                    message = ex.getMessage();
                }
            }
        }
        
        return new GetSalesResponse(Sales);
    }
    
    private Response GetFacturesRequestTreatment(GetFacturesRequest request, Socket socket) {
        ArrayList<Facture> bills = new ArrayList<>();
        String message = "";
        String response = "";
         
        try {
           bills  = db.GetFactures(request.getIdClient());
        } catch (Exception ex) {
            switch(ex.getMessage())
            {
                case "SQL_ERROR" -> {
                    response = "SQL_ERROR";
                    message = ex.getMessage();
                }
                
                default ->{
                    response = "UNKOWN";
                    message = ex.getMessage();
                }
            }
        }
        
        return new GetFacturesResponse(bills);
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
