/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_mail.JMailLib;

import static be.hepl.java_mail.JMailLib.UtilityLib.convertAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Arkios
 */
public class Email {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    static String charset = "iso-8859-1";
    
    private String _id;
    private String _from;
    private ArrayList<String> _to;
    private ArrayList<String> _CC;
    private String _subject;
    private String _message;
    private ArrayList<String> _headers;
    private ArrayList<String> _filePaths;    
    
    private MimeMessage source;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * default constructor
     */
    public Email() {
        _id = "";
        _from = "";
        _to = new ArrayList<>();
        _CC = new ArrayList<>();
        _subject = "";
        _message = "";
        _headers = new ArrayList<>();
        _filePaths = new ArrayList<>();
        
        source = null;
    }
    
    public Email(MimeMessage message) throws MessagingException, IOException {
        setId(message.getMessageID());
        setFrom(message.getFrom());
        setTo(message.getRecipients(RecipientType.TO));
        setCC(message.getRecipients(RecipientType.CC));
        setSubject(message.getSubject());
        setMessage(message);
        setFilePaths(message);
        setHeaders(message.getAllHeaderLines());
        
        source = message;
    }        
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Getters/Setters">
   
    /**
     * Set the value of _from
     * @param from
     * @throws javax.mail.MessagingException
     */
    private void setFrom(Address[] from) throws MessagingException {
        this._from = convertAddress(from);
    }
    
    /**
     * set the To addresses
     * @param _to 
     */
    private void setTo(Address[] _to) {
        ArrayList<String> tmp = new ArrayList<>();
        
        if(_to == null){
            this._to = tmp;
            return;
        }
        
        
        for(Address row : _to){
            tmp.add(row.toString());
        }
        this._to = tmp;
    }
    
    /**
     * set the CC addresses
     * @param _CC 
     */
    private void setCC(Address[] _CC) {
        ArrayList<String> tmp = new ArrayList<>();
        
        if(_CC == null){
            this._CC = tmp;
            return;
        }
        
        for(Address row : _CC){
            tmp.add(row.toString());
        }
        this._CC = tmp;
    }
    
    /**
     * Set the value of _message
     *
     * @param _message new value of _message
     */
    private void setMessage(MimeMessage _message) throws MessagingException, IOException {
        String ReturnedMessage = "";
        
        //RECUPERATION MESSAGE SIMPLEPART
        if(!_message.isMimeType("multipart/*")){
            ReturnedMessage = MimeUtility.decodeText((String) _message.getContent());
            this._message = ReturnedMessage;
            return;
        }
        
        //Else Try to Reach the Text Part:
        Multipart MultiMsg = (Multipart) _message.getContent();
        int n = MultiMsg.getCount();
        
        //RECUPERATION MESSAGE MULTIPART
        for(int i = 0 ; i<n ; i++){
            Part part = MultiMsg.getBodyPart(i);        //get a nested part
            
            //Message Principal en Directe
            if(part.isMimeType("text/plain")) { //&& part.getFileName() == null
                ReturnedMessage = MimeUtility.decodeText((String) part.getContent());
                break;
            }
            //Message Principal encapsulé dans un autre multiparts (outlook)
            else{
                if(part.isMimeType("multipart/alternative")){
                    //Recuperation du nested MultiPart
                    Multipart NestedMP = (Multipart) part.getContent();                    
                    int n2 = NestedMP.getCount();       //Recuperation nombre de part
                    
                    //On loop dans le multipart a la recherche d'un textplain
                    for(int j = 0 ; j<n2 ; j++){
                        Part part2 = NestedMP.getBodyPart(0);
                        if(part2.isMimeType("text/plain")){
                            ReturnedMessage = MimeUtility.decodeText((String) part2.getContent());
                            break;
                        }
                    }
                }
            }
        }
        
        this._message = ReturnedMessage;
    }
    
    /**
     * Set the value of _filePaths
     *
     * @param Message
     * @throws javax.mail.MessagingException
     */
    private void setFilePaths(MimeMessage Message) throws MessagingException, IOException {
        ArrayList<String> ReturnedFilePaths = new ArrayList<>();
        
        //SI SIMPLEPART RETURN
        if(!Message.isMimeType("multipart/*")){
            this._filePaths = ReturnedFilePaths;
            return;
        }
        
        Multipart MultiMsg = (Multipart) Message.getContent();
        int n = MultiMsg.getCount();
        
        //RECUPERATION FILES
        for(int i = 0 ; i<n ; i++){
            Part part = MultiMsg.getBodyPart(i);
            
            //Check if it's a file
            if (part.getDisposition() != null && part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                String FileName = MimeUtility.decodeText(part.getFileName());
                
                ReturnedFilePaths.add(FileName);
            }
        }
        
        this._filePaths = ReturnedFilePaths;
    }
    
    /**
     * 
     * @param _id 
     */
    private void setId(String _id) {
        this._id = _id;
    }

    /**
     * 
     * @param _subject 
     */
    private void setSubject(String _subject) {
        this._subject = _subject;
    }

    /**
     * 
     * @param _headers 
     */
    private void setHeaders(Enumeration _headers) {
        ArrayList<String> tmp = new ArrayList<>();
        
        while (_headers.hasMoreElements()) {
            tmp.add((String) _headers.nextElement());
        }
        
        this._headers = tmp;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Getters">
        public static String getCharset() {
            return charset;
        }

        public String getId() {
            return _id;
        }

        public String getFrom() {
            return _from;
        }

        public ArrayList<String> getTo() {
            return _to;
        }

        public ArrayList<String> getCC() {
            return _CC;
        }

        public String getSubject() {
            return _subject;
        }

        public String getMessage() {
            return _message;
        }

        public ArrayList<String> getHeaders() {
            return _headers;
        }

        public ArrayList<String> getFilePaths() {
            return _filePaths;
        }

        public MimeMessage getSource() {
            return source;
        }
    // </editor-fold>
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public String toString() {
        String toreturn = "";
        
        toreturn += "================================================ EMAIL ==================================================\n";
        toreturn += "      id: " + this._id + "\n";
        toreturn += "      from: " + this._from + "\n";
        
        toreturn += "      to: ";
        for(String s : _to){
            toreturn += ", " + s;
        }
        toreturn += "\n";
        
        toreturn += "      CC: ";
        for(String s : _CC){
            toreturn += ", " + s;
        }
        toreturn += "\n";
        
        toreturn += "      Subject: " + _subject + "\n";
        toreturn += "      Message:\n";
        toreturn += _message + "\n\n\n";
        
        toreturn += "      File: ";
        for(String s : _filePaths){
            toreturn += ", " + s;
        }
        toreturn += "\n";
        
        toreturn += "      Headers: \n";
        for(String s : _headers){
            toreturn += "" + s + "\n";
        }
        toreturn += "================================================ FIN ==================================================\n\n\n";
        
        return toreturn; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    // </editor-fold>    
}
