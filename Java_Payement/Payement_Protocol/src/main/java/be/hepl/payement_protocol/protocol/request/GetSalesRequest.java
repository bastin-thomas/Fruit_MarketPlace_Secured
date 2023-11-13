/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request;

import be.hepl.generic_server_tcp.Request;
import be.hepl.payement_protocol.model.Sale;
import java.util.ArrayList;

/**
 *
 * @author Sirac
 */

    /*
    
« Get Facture » idFacture    idFacture, date, montant, payé, Liste<article> Permettrait de récupérer l’ensemble des articles 
                                                                            concernant une facture dont on fournirait l’id au serveur.
    */
public class GetSalesRequest implements Request{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int idFacture;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GetSalesRequest(int idFacture)
    {
        this.idFacture = idFacture;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getIdFacture()
    {
        return idFacture;
    }
    // </editor-fold>
}
