/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request.Secured;

import be.hepl.generic_server_tcp.Request;
import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.payement_protocol.Utils.CryptoUtils;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Date;


/**
 * « Login » Login, password       Oui ou non              Vérification du login et du mot
 *             (d’un employé)                              passe dans la table des employés
 * @author Arkios
 */
public class LoginRequest_Secured implements Request {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected final String login;
    protected final long temps;     
    protected final double alea;    
    protected final byte[] digest;  // digest envoyé
    
    protected final Certificate clientCertificate;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * 
     * @param login
     * @throws java.io.IOException
     */
    public LoginRequest_Secured(String login, Certificate clientCertificate, byte[] digest, long temps, double alea) throws IOException
    {
        this.clientCertificate = clientCertificate;
        this.digest = digest;
        this.temps = temps;
        this.alea = alea;
        this.login = login;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getLogin() {
        return login;
    }

    public long getTemps() {
        return temps;
    }

    public double getAlea() {
        return alea;
    }

    public byte[] getDigest() {
        return digest;
    }

    public Certificate getClientCertificate() {
        return clientCertificate;
    }
    // </editor-fold>
}
