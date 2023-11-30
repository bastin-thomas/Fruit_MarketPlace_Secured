/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response.Secured;

import be.hepl.payement_protocol.protocol.response.*;

/**
 * « Login » Login, password       Oui ou non              Vérification du login et du mot
 *             (d’un employé)                              passe dans la table des employés
 * @author Arkios
 */
public class LoginResponse_Secured extends LoginResponse {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    byte[] cryptedSessionSecretKey;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public LoginResponse_Secured(boolean valid, byte[] cryptedSessionSecretKey)
    {
        super(valid);
        this.cryptedSessionSecretKey = cryptedSessionSecretKey;
    }
    
    public LoginResponse_Secured(boolean valid, String cause)
    {
        super(valid,cause);
        this.cryptedSessionSecretKey = null;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public byte[] getCryptedSessionSecretKey() {
        return cryptedSessionSecretKey;
    }
    // </editor-fold>    
}
