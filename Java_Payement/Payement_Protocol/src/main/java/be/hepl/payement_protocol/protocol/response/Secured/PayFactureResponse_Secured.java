/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response.Secured;

import be.hepl.payement_protocol.protocol.response.*;
import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.model.Facture;
import java.util.ArrayList;

/*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                            (carte VISA invalide)           la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
*/
public class PayFactureResponse_Secured extends PayFactureResponse{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public PayFactureResponse_Secured(boolean success)
    {
        super(success);
    }
    
    public PayFactureResponse_Secured(boolean success, String cause)
    {
        super(success, cause);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    // </editor-fold>
}
