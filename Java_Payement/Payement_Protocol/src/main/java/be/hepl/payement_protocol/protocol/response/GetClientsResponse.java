/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.response;

import be.hepl.generic_server_tcp.Response;
import java.util.ArrayList;

/**
 *
 * @author Arkios
 */
public class GetClientsResponse implements Response{
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final ArrayList<String> clients;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public GetClientsResponse(ArrayList<String> clients)
    {
        this.clients = clients;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ArrayList<String> getClients()
    {
        return clients;
    }
    // </editor-fold>
}
