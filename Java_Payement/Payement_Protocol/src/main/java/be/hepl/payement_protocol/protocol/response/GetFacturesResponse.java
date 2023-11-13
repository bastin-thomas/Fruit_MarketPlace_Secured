/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response;

import be.hepl.generic_server_tcp.Response;

/**
 *
 * @author Sirac
 */
public class GetFacturesResponse implements Response{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private String idClient;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GetFacturesResponse(String idClient)
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
