/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request.Secured;

import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.payement_protocol.Utils.CryptoUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * « Login » Login, password       Oui ou non              Vérification du login et du mot
 *             (d’un employé)                              passe dans la table des employés
 * @author Arkios
 */
public class LoginRequest_Secured extends LoginRequest {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    protected final long temps;     
    protected final double alea;    
    protected final byte[] digest;  // digest envoyé
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * 
     * @param login
     * @param password
     * @throws java.io.IOException
     */
    public LoginRequest_Secured(String login, String password) throws IOException
    {
        super(login,null);
        this.temps = new Date().getTime();
        this.alea = Math.random();
        
        
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(login);
        objects.add(password);
        objects.add(temps);
        objects.add(alea);
        
        this.digest = CryptoUtils.CreateDigest(objects);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public byte[] getDigest()
    {
        return this.digest;
    }
    
    public double getAlea()
    {
        return this.alea;
    }
    
    public long getTemps()
    {
        return temps;
    }
    // </editor-fold>
}
