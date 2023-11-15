/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.Exceptions;

import be.hepl.generic_server_tcp.Response;

/**
 *
 * @author Arkios
 */
public class EndConnectionException extends Exception {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final Response response;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public EndConnectionException(Response response) {
        super("End connexion decided by the protocol");
        this.response = response;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Response getResponse()
    {
        return response;
    }
    // </editor-fold>
}
