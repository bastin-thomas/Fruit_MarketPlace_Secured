/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response.Secured;

import be.hepl.payement_protocol.protocol.response.*;
import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.model.Sale;
import java.util.ArrayList;
import javax.crypto.spec.IvParameterSpec;


/*
    « Get Sales » idFacture                 Liste<article>      Permettrait de récupérer l’ensemble des articles 
                                                                concernant une facture dont on fournirait l’id au serveur.
*/

/**
 *
 * @author Sirac
 */
public class GetSalesResponse_Secured extends GetSalesResponse{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private byte[] encryptedSales;
    private byte[] ivparameter;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    @Deprecated
    public GetSalesResponse_Secured(ArrayList<Sale> sales)
    {
        super(null);
        this.encryptedSales = null;
        this.ivparameter = null;
    }
    
    public GetSalesResponse_Secured(byte[] encryptedSales, IvParameterSpec ivparameter) {
        super(null);
        this.encryptedSales = encryptedSales;
        this.ivparameter = ivparameter.getIV();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public byte[] getEncryptedSales() {
        return encryptedSales;
    }

    public byte[] getIvparameter() {
        return ivparameter;
    }
    // </editor-fold>
}
