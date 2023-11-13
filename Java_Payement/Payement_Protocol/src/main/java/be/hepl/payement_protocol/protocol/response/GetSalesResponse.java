/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response;

import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.model.Sale;
import java.util.ArrayList;


/*
    « Get Sales » idFacture                 Liste<article>      Permettrait de récupérer l’ensemble des articles 
                                                                concernant une facture dont on fournirait l’id au serveur.
*/

/**
 *
 * @author Sirac
 */
public class GetSalesResponse implements Response{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final ArrayList<Sale> sales;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GetSalesResponse(ArrayList<Sale> sales)
    {
        this.sales = sales;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ArrayList<Sale> getSales()
    {
        return sales;
    }
    // </editor-fold>
}
