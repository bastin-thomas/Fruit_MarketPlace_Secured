/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response.Secured;

import be.hepl.payement_protocol.protocol.response.*;
import be.hepl.generic_server_tcp.Response;
import be.hepl.cryptolibrary.CryptoUtils;
import java.security.KeyStoreException;
import java.util.ArrayList;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author Arkios
 */
public class GetClientsResponse_Secured extends GetClientsResponse{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private byte[] encryptedClients;
    private byte[] ivparameter;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">

    /**
     *
     * @param clients
     * @deprecated use {@link #GetClientsResponse_Secured()} instead
     */
    @Deprecated
    public GetClientsResponse_Secured(ArrayList<String> clients)
    {
        super(null);
    }
    
    public GetClientsResponse_Secured(byte[] encryptedClients, IvParameterSpec ivparameter)
    {
        super(null);
        this.encryptedClients = encryptedClients;
        this.ivparameter = ivparameter.getIV();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public byte[] getEncryptedClients() {
        return encryptedClients;
    }

    public byte[] getIvparameter() {
        return ivparameter;
    }
    
    @Deprecated
    @Override
    public ArrayList<String> getClients() {
        return null; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    // </editor-fold>
}
