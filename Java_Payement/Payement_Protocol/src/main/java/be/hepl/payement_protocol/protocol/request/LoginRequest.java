/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request;

import be.hepl.generic_server_tcp.Request;


/**
 * « Login » Login, password       Oui ou non              Vérification du login et du mot
 *             (d’un employé)                              passe dans la table des employés
 * @author Arkios
 * @deprecated use {@link #LoginRequest_Secured} instead.
 */
@Deprecated
public class LoginRequest implements Request {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected final String login;
    
    /**
     * Send in clear password on the network, not secure
     * @deprecated
     */
    @Deprecated
    protected final String password;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public LoginRequest(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getLogin()
    {
        return login;
    }
    
    /**
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public String getPassword()
    {
        return password;
    }
    // </editor-fold>
}
