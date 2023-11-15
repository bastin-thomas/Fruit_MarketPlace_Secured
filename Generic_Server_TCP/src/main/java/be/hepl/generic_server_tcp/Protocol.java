/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package be.hepl.generic_server_tcp;

import be.hepl.generic_server_tcp.Exceptions.EndConnectionException;
import java.net.Socket;

/**
 *
 * @author Arkios
 */
public interface Protocol {
    String getNom();
    
    /**
     *
     * @param requete
     * @param socket
     * @return
     * @throws be.hepl.generic_server_tcp.Exceptions.EndConnectionException
     */
    Response RequestTreatment(Request requete, Socket socket) throws EndConnectionException;
}
