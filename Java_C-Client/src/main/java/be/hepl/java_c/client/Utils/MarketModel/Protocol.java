/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_c.client.Utils.MarketModel;

import be.hepl.java_c.client.Utils.Consts;
import be.hepl.java_c.client.Utils.SocketClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arkios
 */
public class Protocol {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private SocketClient csocket;
    
    // </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Protocol(SocketClient sock){
        csocket = sock;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Get the value of csocket
     *
     * @return the value of csocket
     */
    public SocketClient getCsocket() {
        return csocket;
    }

    /**
     * Set the value of csocket
     *
     * @param csocket new value of csocket
     */
    public void setCsocket(SocketClient csocket) {
        this.csocket = csocket;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Send the login command to the server, and treat the received message
     * @param nom
     * @param mdp
     * @throws Exception
     */
    public void SendLogin(String nom, String mdp) throws Exception{
        String message = "";
        
        //Send and wait for response
        try{
            csocket.SendCpp("LOGIN" + Consts.SplitCommand + nom + Consts.SplitParameters + mdp);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand) || CommandsToken.length == 1){
            throw new Exception("ENDCONNEXION");
        }
        
        //Check if OK, then do nothing
        if(CommandsToken[1].equals("OK")){
            return;
        }
        
        //If not Ok check params
        String[] ParamsToken = CommandsToken[1].split("" + Consts.SplitParameters);
        
        //If KO Throw an exceptions based on type of params
        if(ParamsToken[0].equals("KO")){
            throw new Exception(ParamsToken[1]);
        }
    }
    
    /**
     *
     * @param nom
     * @param mdp
     * @throws Exception
     */
    public void SendCreateLogin(String nom, String mdp) throws Exception{
        String message = "";
        
        //Send and wait for response
        try{
            csocket.SendCpp("CREATELOGIN" + Consts.SplitCommand + nom + Consts.SplitParameters + mdp);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand) || CommandsToken.length == 1){
            throw new Exception("ENDCONNEXION");
        }
        
        //Check if not OK, then throw error
        if(!CommandsToken[1].equals("OK"))
        {
            throw new Exception("new Account creation Error");
        }
    }
    
    /**
     * Send a request to the server to get Consult one article data
     * @param idArticle
     * @return
     * @throws Exception
     */
    public Articles SendConsult(int idArticle) throws Exception{
        String message = "";
        Articles article = null;
        
        
        //Send and wait for response
        try{
            csocket.SendCpp("CONSULT" + Consts.SplitCommand + idArticle + Consts.SplitParameters);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand) || CommandsToken.length == 1){
            throw new Exception("ENDCONNEXION");
        }
        
        if(CommandsToken[1].equals("-1")){
            throw new Exception("NO_ARTICLE_FOUND");
        }
        
        try
        {
            article = new Articles(CommandsToken[1]);
        }
        catch(NumberFormatException ex)
        {
            throw new Exception("PARAMS_FORMAT_ERROR");
        }
        
        System.out.println(article);
        return article;
    }
    
    /**
     * Send a request to the server to put a product into the Caddie
     * @param idArticle
     * @param quantitee
     * @return
     * @throws Exception
     */
    public Achats SendAchat(int idArticle, int quantitee) throws Exception{
        String message = "";
        Achats achat = null;
        
        //Send and wait for response
        try{
            csocket.SendCpp("ACHAT" + Consts.SplitCommand + idArticle + Consts.SplitParameters + quantitee);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand) || CommandsToken.length == 1){
            throw new Exception("ENDCONNEXION");
        }
        
        if(CommandsToken[1].equals("-1")){
            throw new Exception("NO_ARTICLE_FOUND");
        }
        
        try
        {
            achat = new Achats(CommandsToken[1]);
        }
        catch(NumberFormatException ex)
        {
            throw new Exception("PARAMS_FORMAT_ERROR");
        }
        
        System.out.println(achat);
        return achat;
    }
    
    /**
     * Send a request to the server to get Caddie data
     * @return
     * @throws Exception
     */
    public ArrayList<CaddieRows> SendCaddie() throws Exception{
        String message = "";
        ArrayList<CaddieRows> caddie = new ArrayList<>();
        
        
        //Send and wait for response
        try{
            csocket.SendCpp("CADDIE" + Consts.SplitCommand);
            message = csocket.ReceiveCpp();
            System.out.println("Message Received: " + message);
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand)){
            throw new Exception("ENDCONNEXION");
        }
        
        if(message.equals("CADDIE" + Consts.SplitCommand)){
            return caddie;
        }
        
        if(CommandsToken[1].equals("-1")){
            throw new Exception("ERROR_OPERATION");
        }
        
        
        
        try
        {
            String[] rowList = CommandsToken[1].split("" + Consts.SplitList);
            
            for(String tmp : rowList){                
                caddie.add(new CaddieRows(tmp));
            }
        }
        catch(NumberFormatException ex)
        {
            throw new Exception("PARAMS_FORMAT_ERROR");
        }
        
        System.out.println("Caddie:");
        for(CaddieRows row : caddie){
            System.out.println(row);
        }
        
        return caddie;
    }
    
    /**
     * Send a request to the server to cancel a specific item from caddie
     * @param idArticle
     * @throws Exception
     */
    public void SendCancel(int idArticle) throws Exception{
        String message = "";
        
        //Send and wait for response
        try{
            csocket.SendCpp("CANCEL" + Consts.SplitCommand + idArticle + Consts.SplitParameters);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand)){
            throw new Exception("ENDCONNEXION");
        }
        
        if(!CommandsToken[1].equals("OK")){
            throw new Exception("CANCEL_ERROR");
        }
    }
    
    /**
     * Send a request to the server to remove all item from caddie
     * @throws Exception
     */
    public void SendCancelAll() throws Exception{
        String message = "";
        
        //Send and wait for response
        try{
            csocket.SendCpp("CANCELALL" + Consts.SplitCommand);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand)){
            throw new Exception("ENDCONNEXION");
        }
        
        if(!CommandsToken[1].equals("OK")){
            throw new Exception("CANCEL_ERROR");
        }
    }
    
    public void SendConfirmer(String nom) throws Exception{
        String message = "";
        
        //Send and wait for response
        try{
            csocket.SendCpp("CONFIRMER" + Consts.SplitCommand + nom + Consts.SplitParameters);
            message = csocket.ReceiveCpp();
        }catch(IOException ex){
            throw new Exception("ENDCONNEXION");
        }
        
        //Split Commands name and parameters
        String[] CommandsToken = message.split("" + Consts.SplitCommand);
        if(message.equals("CRITICAL" + Consts.SplitCommand) || CommandsToken.length == 1){
            throw new Exception("ENDCONNEXION");
        }
        
        if(CommandsToken[1].equals("-1")){
            throw new Exception("ERROR_BILL");
        }
    }
    
    public void SendLogout() throws IOException{
        csocket.SendCpp("LOGOUT" + Consts.SplitCommand);
    }
    
    /**
     *
     */
    public void Close()
    {
        try {
            this.csocket.Close();
        } catch (IOException ex) {
            System.out.println("Cannot Close the ClientSocket");
            System.exit(120);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
