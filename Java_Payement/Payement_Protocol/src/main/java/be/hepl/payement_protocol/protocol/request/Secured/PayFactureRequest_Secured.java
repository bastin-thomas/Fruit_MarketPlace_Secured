/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request.Secured;

import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.generic_server_tcp.Request;

/**
 *
 * @author Sirac
 */

/*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                                   (carte VISA invalide)    la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
*/
public class PayFactureRequest_Secured extends PayFactureRequest{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public PayFactureRequest_Secured(int idFacture, String name, String visa)
    {
        super(idFacture, name, visa);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    // </editor-fold>
}
