/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response;

import be.hepl.generic_server_tcp.Response;

/**
 * « Login » Login, password       Oui ou non              Vérification du login et du mot
 *             (d’un employé)                              passe dans la table des employés
 * @author Arkios
 */
public class LoginResponse implements Response {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final boolean valid;
    private final String cause;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public LoginResponse(boolean valid)
    {
        this.valid = valid;
        this.cause = "";
    }
    
    public LoginResponse(boolean valid, String cause)
    {
        this.valid = valid;
        this.cause = cause;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public boolean isValid()
    {
        return valid;
    }
    
    public String getCause()
    {
        return cause;
    }
    // </editor-fold>
}
