/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.Utils;

import be.hepl.payement_protocol.protocol.request.LoginRequest;
import be.hepl.payement_protocol.protocol.response.LoginResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Sirac
 */
public class Gestion_Protocol_Client {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final Socket socket;
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Gestion_Protocol_Client(Socket sock) throws IOException{
        socket = sock;
        
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    public boolean SendLogin(String Login, String Password) throws Exception{
        Object response = null;
        boolean bool = false;
        
        try{
            oos.writeObject(new LoginRequest(Login,Password));
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
        
        try{
            response = ois.readObject();
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Login validity
        if(response instanceof LoginResponse Lresponse){
            if(Lresponse.isValid()){
                bool = true;
            }else {
                socket.close();
                throw new Exception(Lresponse.getCause());
            } 
        }else {
            socket.close();
            throw new Exception("UNEXPECTED_RESPONSE");
        }
        
        return bool;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>

}