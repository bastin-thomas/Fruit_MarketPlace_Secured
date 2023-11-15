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
import be.hepl.payement_protocol.model.*;
import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.payement_protocol.protocol.response.*;

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
    public final DBPayement db;
    private final Logger logger;
    private HashMap<String, Socket> connectedClients;
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
    
    @Override
    public synchronized Response RequestTreatment(Request requete, Socket socket) throws EndConnectionException 
    {
        Response rep = null;
        if(connectedClients.containsValue(socket))
        {
            
            if(requete instanceof GetSalesRequest request)
            {
                rep = GetSalesRequestTreatment(request, socket);
                logger.Trace( request.getClass().getName() + " Reçue et traitée");
            }

            if(requete instanceof GetFacturesRequest request)
            {
                rep = GetFacturesRequestTreatment(request, socket);
                logger.Trace( request.getClass().getName() + " Reçue et traitée");
            }
            
            if(requete instanceof PayFactureRequest request)
            {
                rep = PayFactureRequestTreatment(request, socket);
                logger.Trace( request.getClass().getName() + " Reçue et traitée");
            }
            
            if(requete instanceof GetClientsRequest request)
            {
                rep = GetClientsRequestTreatment(request, socket);
                logger.Trace( request.getClass().getName() + " Reçue et traitée");
            }

            if(requete instanceof LogoutRequest request)
            {
                LogoutRequestTreatment(request, socket);
                logger.Trace( request.getClass().getName() + " Reçue et traitée");
            }
        }
        
        if(requete instanceof LoginRequest request)
        {
            rep = LoginRequestTreatment(request, socket);
            logger.Trace( request.getClass().getName() + " Reçue et traitée");
        }
        
        if(!socket.isConnected()) throw new EndConnectionException(rep);
        
        return rep;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Protocol Treatment">
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
            connectedClients.put(loginRequest.getLogin(), socket);
            return new LoginResponse(true);
        } else {
            return new LoginResponse(false, (response + ": " + message));
        }   
    }
    
    
    
    /*
    « Get Sales » idFacture                 Liste<article>      Permettrait de récupérer l’ensemble des articles 
                                                                concernant une facture dont on fournirait l’id au serveur.
    */
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
    
    
    
    /*
    « Get Factures » idClient         Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                  factures du client dans la table
                                                                                       factures (sans le contenu détaillé
                                                                                       de la commande donc)
    */
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
    
    
    
    /*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                                   (carte VISA invalide)    la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
    */
    private Response PayFactureRequestTreatment(PayFactureRequest request, Socket socket) {
        boolean valid = false;
        String cause = "";
        
        //Check Visa Card
        valid = CheckVisaCard(request.getVISA());
        
        if(valid)
        {
            try {
                db.PayFacture(request.getIdFacture());
            } catch (Exception ex) {
                valid = false;
                cause = ex.getMessage();
            }
        }
        else
        {
            cause="CARD_INVALID";
        }
        
        return new PayFactureResponse(valid, cause);
    }
    
    
    
    
    /***
     * « Logout »
     * @param logoutRequest
     * @param socket
     * @return 
     */
    private void LogoutRequestTreatment(LogoutRequest logoutRequest, Socket socket) throws EndConnectionException {
        if(connectedClients.containsKey(logoutRequest.getLogin()) == true)
        {
            connectedClients.remove(logoutRequest.getLogin());
            throw new EndConnectionException(null);
        }
    }
    
    
    
    private Response GetClientsRequestTreatment(GetClientsRequest request, Socket socket) {
        ArrayList<String> clients;
        
        try {
           clients  = db.GetClientList();
        } catch (Exception ex) {
            clients = new ArrayList<>();
        }
        
        return new GetClientsResponse(clients);
    }
    // </editor-fold>  
    
    // <editor-fold defaultstate="collapsed" desc="Utils Methods">
    /***
     * Check Visa Card using Lungth Algoritm
     * https://www.ibm.com/docs/en/order-management-sw/9.3.0?topic=cpms-handling-credit-cards
     * @param visa
     * @return 
     */
    private boolean CheckVisaCard(String visa) {
        long num = -1;
        
        try
        {
            try
            {
                num = Long.parseLong(visa);
            }
            catch(NumberFormatException ex)
            {
                String newVisa = "";
                String[] split = visa.split(" ");
                
                for(String s : split)
                {
                    newVisa+=s;
                }
                
                num = Long.parseLong(newVisa);
            }
            
            
            ArrayList<Long> digits = new ArrayList<>();

            while(num > 0)
            {
                digits.add(num%10);
                num = num/10;
            }
            
            //Double ODD index
            for(int i = 0 ; i < digits.size() ; i++)
            {
                //IF ODD index
                if(i%2 != 0)
                {
                    Long doubled = digits.get(i)*2;

                    digits.set(i, doubled);
                }
            }
            
            //sum all even doubled values or odd values
            int checksum = 0;
            for(Long digit : digits)
            {
                if(digit>9)
                {
                    checksum+=digit-9;
                }
                else
                {
                    checksum+=digit;
                }
            }

            //Check if equals to 0 on modulo 10
            return (checksum % 10 == 0);
            
        }
        catch(Exception ex)
        {
            logger.Trace("ERROR[" + ex.getMessage() + "] - " + ex.getCause());
            return false;
        }
    }
    // </editor-fold>
}
