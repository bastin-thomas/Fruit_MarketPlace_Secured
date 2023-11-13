/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response;

import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.model.Facture;
import java.util.ArrayList;

/**
 *
 * @author Sirac
 */
public class PayFactureResponse implements Response{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private ArrayList<Facture> bills;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public PayFactureResponse(ArrayList<Facture> bills)
    {
        this.bills = bills;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ArrayList<Facture> getBills()
    {
        return bills;
    }
    // </editor-fold>
}
