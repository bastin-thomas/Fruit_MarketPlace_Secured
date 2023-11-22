/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.Utils;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.payement_protocol.protocol.request.Secured.LoginRequest_Secured;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Arkios
 */
public class DBPayement_Secured extends DBPayement {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * 
     * @param urlconnexion
     * @param logger
     * @throws java.sql.SQLException
     */
    public DBPayement_Secured(String urlconnexion, Logger logger) throws SQLException {
        super(urlconnexion, logger);   
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * « Login » Login, password       Oui ou non              Vérification du login et du mot
     *           (d’un employé)                                passe dans la table des employés
     * @param login
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean Login(LoginRequest_Secured login) throws Exception{
        ResultSet result;
        
        try {
            result = select("password", "employees", "login='"+login.getLogin()+"'");
        } catch (SQLException ex) {
            throw new Exception("SQL_ERROR", ex);
        }
        
        //Check if there is a first row:
        if(!result.next())
        {
            throw new Exception("NO_LOGIN");
        }
        
        String DB_Password = result.getString("password");
        
        //Recreate the Digest based on Login:
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(login.getLogin());
        objects.add(DB_Password);
        objects.add(login.getTemps());
        objects.add(login.getAlea());
        
        byte[] ServerDigest = CryptoUtils.CreateDigest(objects);
        
        //If the two digest are not equel
        if(!MessageDigest.isEqual(login.getDigest(), ServerDigest))
        {
            throw new Exception("BAD_LOGIN");
        }
        
        //else there are equals
        return true;
    }
    // </editor-fold>   
}
