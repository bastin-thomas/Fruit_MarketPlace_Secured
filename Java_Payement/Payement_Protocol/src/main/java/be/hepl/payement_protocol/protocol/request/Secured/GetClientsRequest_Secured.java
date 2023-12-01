/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.request.Secured;

import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.generic_server_tcp.Request;

/**
 *
 * @author Arkios
 */
public class GetClientsRequest_Secured extends GetClientsRequest {
    private byte[] clientSignature;
    private String user;
    
    @Deprecated
    public GetClientsRequest_Secured(){}
    
    public GetClientsRequest_Secured(byte[] clientSignature, String user)
    {
        this.clientSignature = clientSignature;
        this.user = user;
    }
    
    public byte[] getClientSignature() {
        return clientSignature;
    }
    
    public String getUser() {
        return user;
    }
}
