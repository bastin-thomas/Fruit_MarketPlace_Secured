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
    « Get Factures » idClient         Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                         factures du client dans la table
                                                                                              factures (sans le contenu détaillé
                                                                                              de la commande donc)
*/

/**
 *
 * @author Sirac
 */
public class GetFacturesResponse_Secured extends GetFacturesResponse{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GetFacturesResponse_Secured(ArrayList<Facture> bills)
    {
        super(bills);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    // </editor-fold>
}
