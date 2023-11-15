/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.Utils;

import be.hepl.payement_protocol.model.Facture;
import be.hepl.payement_protocol.model.Sale;
import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.payement_protocol.protocol.response.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
        
        try{
            oos.writeObject(new LoginRequest(Login,Password));
            response = ois.readObject();
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }

        
        // Login validity
        if(response instanceof LoginResponse Lresponse){
            
            if(Lresponse.isValid()){
                return true;
                
            } else {
                socket.close();
                throw new Exception(Lresponse.getCause());
            }
            
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    
    
    public void SendLogout(String Login) throws Exception{
        try{
            oos.writeObject(new LogoutRequest(Login));
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
    }
    
    
    
    public ArrayList<String> SendGetClientsRequest() throws Exception{
        Object object = null;
        
        try{
            oos.writeObject(new GetClientsRequest());
            object = ois.readObject();
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Login validity
        if(object instanceof GetClientsResponse response){
            return response.getClients();
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    
    
    public ArrayList<Facture> SendGetFacturesRequest(String idClient) throws Exception{
        Object object = null;
        
        try{
            oos.writeObject(new GetFacturesRequest(idClient));
            object = ois.readObject();
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Login validity
        if(object instanceof GetFacturesResponse response){
            return response.getBills();
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    
    
    public ArrayList<Sale> SendGetSalesRequest(int idBills) throws Exception{
        Object object = null;
        
        try{
            oos.writeObject(new GetSalesRequest(idBills));
            object = ois.readObject();
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Login validity
        if(object instanceof GetSalesResponse response){
            return response.getSales();
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    
    
    
    public boolean SendPayFactureRequest(int idBills, String Name, String VISA) throws Exception{
        Object object = null;
        
        try{
            oos.writeObject(new PayFactureRequest(idBills, Name, VISA));
            object = ois.readObject();
        }
        catch(Exception ex){
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Login validity
        if(object instanceof PayFactureResponse response){
            if(response.isSuccess())
            {
                return true;
            }
            else
            {
                //CARD_INVALID or other things
                throw new Exception(response.getCause());
            }
        } else {
            socket.close();
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>

}