/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_mail.JMailLib;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

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
        String ReceptionServer = null;
        String port = null;
        
        //Initialisation des configurations de classes
        Properties props = InitProperties(serverHost, protocol, ident, password, ReceptionServer, port);
        
        //Creation d'un object Authenticator (classe anonyme)
        Authenticator conn = new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ident, password);
            }
        };
        
        this.ident = ident;
        
        //Creation d'un objet session basé sur les props et l'authenticator.
        System.out.println("Session Created");
        _session = Session.getInstance(props, conn);
        
        //Recuperation du store
        System.out.println("Store Created");
        _store = _session.getStore(protocol);
        
        //Connexion au store
        System.out.println("Try To Connect on: " + serverHost + ", Id: " + ident + ", Pass: " + password );
        _store.connect(serverHost, ident, password);
        
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
     * Initialise en fonction des choix de la pages les bonnes configurations smtp/pop3/imap
     * @param serverHost
     * @param protocol
     * @param ident
     * @param password
     * @param ReceptionServer
     * @param port
     * @return configuration setted
     */
    private Properties InitProperties(String serverHost, String protocol, String ident, String password, String ReceptionServer, String port){
        Properties props = System.getProperties();
        
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
        
        
        //SI GMAIL SELECTIONNER
        // <editor-folder defaultstate="collapsed" desc="Gmail Config (pop3/Imap)">
        if(serverHost.equalsIgnoreCase("smtp.gmail.com")){
            props.put("mail.smtp.port", "465");
            
            if(protocol.equalsIgnoreCase("pop3")){
                ReceptionServer = "pop.gmail.com";
                port = "995";
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
        // </editor-folder>
        
        //SI OUTLOOK SELECTIONNER
        // <editor-folder defaultstate="collapsed" desc="Outlook Config (pop3/Imap)">
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
        // </editor-folder>

        return props;
    }
    
    /**
     * Retourne la liste des messages chargé et structuré
     * @return
     * @throws MessagingException 
     */
    public ArrayList<Email> GetListMail() throws MessagingException{
        Message[] msg = null;
        ArrayList<Email> list = new ArrayList<>();
        
        _folder.close(false);
        _folder = _store.getFolder("INBOX");
        _folder.open(Folder.READ_ONLY);
                
        System.out.println("Obtention des messages");
        msg = _folder.getMessages();
        
        //Loop on array to init new Email();
        for(Message m : msg){
            //Add for each elements a new Email based on message
            list.add(new Email(m));
        }
        
        return list;
    }
    
    public int GetMessageCount() throws MessagingException{
        return _folder.getMessageCount();
    }
    
    public void Close() throws MessagingException{
        _folder.close(false);
        _store.close();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
