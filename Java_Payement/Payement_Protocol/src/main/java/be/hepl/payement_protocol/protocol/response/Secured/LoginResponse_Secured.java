/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response.Secured;

import be.hepl.payement_protocol.protocol.response.*;
import be.hepl.generic_server_tcp.Response;

/**
 * « Login » Login, password       Oui ou non              Vérification du login et du mot
 *             (d’un employé)                              passe dans la table des employés
 * @author Arkios
 */
public class LoginResponse_Secured extends LoginResponse {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public LoginResponse_Secured(boolean valid)
    {
        super(valid);
    }
    
    public LoginResponse_Secured(boolean valid, String cause)
    {
        super(valid,cause);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    // </editor-fold>
}
