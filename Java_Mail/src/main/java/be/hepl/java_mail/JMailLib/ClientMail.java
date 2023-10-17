/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_mail.JMailLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Arkios
 */
public class ClientMail {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    static String charset = "iso-8859-1";
    
    private String ident;
    private Session _session;
    private Store _store;
    private Folder _folder;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public ClientMail(String serverHost, String protocol, String ident, String password) throws NoSuchProviderException, MessagingException{
        String ReceptionServer = "";
        String port = "-1";
        
        //Initialisation de la configuration de la connexion avec le serveur mail
        Properties props = System.getProperties();
        
        // <editor-fold defaultstate="collapsed" desc="Initialisation de la configuration">
        //Set the smtp Host
        props.put("mail.smtp.host", serverHost);
        
        //set the Auth properties
        props.put("mail.smtp.auth", "true");
        
        //Spécification du type de socket utilisé (TLSv1.2);
        props.put("mail." + protocol + ".ssl.protocols", "TLSv1.2");
        props.put("mail." + protocol + ".socketFactory.port", port);
        props.put("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail." + protocol + ".socketFactory.fallback", "false");

        //Si Imap config for an ssl imap;
        if(protocol.equalsIgnoreCase("imap")){
            props.put("mail.imap.ssl.enable", "true");
        }
        
        //Set Encoding used for the encoding
        props.put("file.encoding", charset);
        
        //Set charset for mimeparts
        props.put("mail.mime.charset", "utf-8");
        // </editor-fold>   
        
        //SI GMAIL SELECTIONNER
        // <editor-fold defaultstate="collapsed" desc="Gmail Config (pop3/Imap)">
        if(serverHost.equalsIgnoreCase("smtp.gmail.com")){
            props.put("mail.smtp.port", "465");
            
            if(protocol.equalsIgnoreCase("pop3")){
                ReceptionServer = "pop.gmail.com";
                port = "995";
                              
                ident = "recent:" + ident;
            }
            else if(protocol.equalsIgnoreCase("imap")){
                ReceptionServer = "imap.gmail.com";
                port = "993";
            }
            else{
                System.out.println("Erreur, choix du serveur inconnu.");
                System.exit(0);
            }
        }
        // </editor-fold>

        
        //SI OUTLOOK SELECTIONNER
        // <editor-fold defaultstate="collapsed" desc="Outlook Config (pop3/Imap)">
        if(serverHost.equalsIgnoreCase("smtp-mail.outlook.com")){
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.socketFactory.port", "587");
            props.put("mail.host", "outlook.office365.com");
            
            if(protocol.equalsIgnoreCase("pop3")){
                ReceptionServer = "outlook.office365.com";
                port = "995";
            }
            else if(protocol.equalsIgnoreCase("imap")){
                ReceptionServer = "outlook.office365.com";
                port = "993";
            }
            else{
                System.out.println("Erreur, choix du serveur inconnu.");
                System.exit(0);
            }
        }
        // </editor-fold>

        
        //Creation d'un object Authenticator (classe anonyme)
        Authenticator conn = new MyAuthenticator(ident, password);
        
        this.ident = ident;
        
        //Creation d'un objet session basé sur les props et l'authenticator.
        System.out.println("Session Created");
        _session = Session.getInstance(props, conn);
        
        //Recuperation du store
        System.out.println("Store Created");
        _store = _session.getStore(protocol);
        
        //Connexion au store
        _store.connect(serverHost, Integer.decode(port), ident, password);
        System.out.println("Connect on: " + serverHost + ", Port: " + port + ", Id: " + ident + ", Pass: " + password );
        
        //Recuperation du folder INBOX et ouverture de ce dernier.
        _folder = _store.getFolder("INBOX");
        _folder.open(Folder.READ_ONLY);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public static String getCharset() {
        return charset;
    }

    public static void setCharset(String charset) {
        ClientMail.charset = charset;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Session getSession() {
        return _session;
    }

    public void setSession(Session _session) {
        this._session = _session;
    }

    public Store getStore() {
        return _store;
    }

    public void setStore(Store _store) {
        this._store = _store;
    }

    public Folder getFolder() {
        return _folder;
    }

    public void setFolder(Folder _folder) {
        this._folder = _folder;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Retourne la liste des messages chargé et structuré
     * @return
     * @throws MessagingException 
     * @throws java.io.IOException 
     */
    public ArrayList<Email> GetListMail() throws MessagingException, IOException{
        Message[] msg = null;
        ArrayList<Email> list = new ArrayList<>();
        
        _folder.close(true);
        _folder = _store.getFolder("INBOX");
        _folder.open(Folder.READ_ONLY);
                
        msg = _folder.getMessages();
        
        //Loop on array to init new Email();
        for(Message m : msg){
            //Add for each elements a new Email based on message
            Email tmp = new Email((MimeMessage) m);
            list.add(tmp);
        }
        
        Collections.reverse(list);
        
        return list;
    }
    
    public int GetMessageCount() throws MessagingException{
        return _folder.getMessageCount();
    }
    
    public void Close() throws MessagingException{
        _folder.close(true);
        _store.close();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
