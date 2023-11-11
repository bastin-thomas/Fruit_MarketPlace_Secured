/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_client.Utils;
import be.hepl.payement_protocol.Utils.Consts;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author Arkios
 */
public class SocketClient {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final Socket csocket;
    
    private final DataOutputStream Dos;
    private final DataInputStream Dis;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public SocketClient(Properties config) throws NumberFormatException, IOException{
        String Ip = config.getProperty(Consts.ConfigIP);
        int Port = Integer.parseInt(config.getProperty(Consts.ConfigPort));
        
        csocket = new Socket(Ip, Port);
        
        Dos = new DataOutputStream(new BufferedOutputStream(csocket.getOutputStream()));
        Dis = new DataInputStream(new BufferedInputStream(csocket.getInputStream()));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">

    /**
     * Check if the connexion is alive
     * @return
     */
    public boolean IsAlive(){
        if(csocket.isClosed() || !csocket.isConnected()){
            return false;
        }
        else{
            return true;
        }
    }
    
    /**
     * Get the server address on wich the client is connected
     * @return 
     */
    public InetAddress GetServerAddress()
    {
        return csocket.getInetAddress();
    }
    
    /**
     * Read a Request
     * @return
     * @throws IOException
     */
    public String ReceiveCpp() throws IOException{
        StringBuffer buffer = new StringBuffer();
        
        
        boolean EOT = false;
        while(!EOT) // Read byte by byte and if reach end of requestion stop the loop
        {
            byte b1 = Dis.readByte();
            
            if (b1 == (byte)Consts.EndOfRequest1)
            {
                byte b2 = Dis.readByte();
                if (b2 == (byte)Consts.EndOfRequest2){
                    EOT = true;
                }
                else{
                   buffer.append((char)b1);
                   buffer.append((char)b2);
                }
            }
            else{
                buffer.append((char)b1);
            }
        }
        
        String msg = buffer.toString();        
        return msg;
    }
    
    /**
     * Send a Request
     * @param msg
     * @throws IOException
     */
    public void SendCpp(String msg) throws IOException{
        String trame = msg + Consts.EndOfRequest1 + Consts.EndOfRequest2;
        Dos.write(trame.getBytes());
        Dos.flush();
    }
    
    /**
     *
     * @throws IOException
     */
    public void Close() throws IOException
    {
        this.csocket.close();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
