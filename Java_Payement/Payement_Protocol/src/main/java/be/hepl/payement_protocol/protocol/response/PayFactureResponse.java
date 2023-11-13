/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response;

import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.model.Facture;
import java.util.ArrayList;

/*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                            (carte VISA invalide)           la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
*/
public class PayFactureResponse implements Response{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final boolean success;
    private final String  cause;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public PayFactureResponse(boolean success)
    {
        this.success = success;
        this.cause = "";
    }
    
    public PayFactureResponse(boolean success, String cause)
    {
        this.success = success;
        this.cause = cause;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public boolean isSuccess() {
        return success;
    }

    public String getCause() {
        return cause;
    }
    // </editor-fold>
}
