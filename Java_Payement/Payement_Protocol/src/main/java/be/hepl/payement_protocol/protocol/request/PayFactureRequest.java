/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request;

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
public class PayFactureRequest implements Request{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final int idFacture;
    private final String name;
    private final String visa;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public PayFactureRequest(int idfacture, String name, String visa)
    {
        this.idFacture = idfacture;
        this.name = name;
        this.visa = visa;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getIdFacture()
    {
        return idFacture;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getVISA()
    {
        return visa;
    }
    // </editor-fold>
}
