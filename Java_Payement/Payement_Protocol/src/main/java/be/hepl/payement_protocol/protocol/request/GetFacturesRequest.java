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
    « Get Factures » idClient         Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                         factures du client dans la table
                                                                                              factures (sans le contenu détaillé
                                                                                              de la commande donc)
*/
public class GetFacturesRequest implements Request{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final String idClient;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GetFacturesRequest(String idClient)
    {
        this.idClient = idClient;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getIdClient()
    {
        return idClient;
    }
    // </editor-fold>
}
