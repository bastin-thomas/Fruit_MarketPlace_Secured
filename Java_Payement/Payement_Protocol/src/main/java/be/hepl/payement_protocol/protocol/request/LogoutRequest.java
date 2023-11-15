/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request;

import be.hepl.generic_server_tcp.Request;

/**
 * « Logout »
 * @author Arkios
 */
public class LogoutRequest implements Request {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final String login;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public LogoutRequest(String login)
    {
        this.login = login;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getLogin()
    {
        return login;
    }
    // </editor-fold>
}
