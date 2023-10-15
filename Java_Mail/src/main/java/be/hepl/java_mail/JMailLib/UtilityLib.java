/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_mail.JMailLib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Arkios
 */
public class UtilityLib {
    
    /**
     * Get the Sender and the object of all Message 
     * @param messagesList The Message List.
     * @return ArrayList wich containe multiple ArrayList paired From & Subject
     * @throws MessagingException 
     */
    public static ArrayList GetHeadSender(Message[] messagesList) throws MessagingException{
        
        ArrayList messagesData = new ArrayList();
        
        for (Message msg : messagesList) {
            ArrayList<String> vec = new ArrayList<>();
            String From = convertAddress(msg.getFrom());
            String Subject = msg.getSubject();
            vec.add(From);
            vec.add(Subject);
            messagesData.add(vec.clone());
        }
        
        return messagesData;
    }
    
    /**
     * 
     * @param fromAdd
     * @return
     * @throws MessagingException 
     */
    public static String convertAddress(Address[] fromAdd) throws MessagingException{
        String From = "";
        int i = 0;
        
        if(fromAdd == null) return "";
        
        //Creation d'une String sur base d'adresse
        for (Address Add : fromAdd) {
            if (fromAdd != null) {
                if (i == fromAdd.length-1) {
                    if (Add.getClass().equals(InternetAddress.class)) {
                        InternetAddress iad = (InternetAddress) Add;
                        From += iad.getAddress();
                    } else {
                        From += fromAdd[0].toString();
                    }
                } else {
                    if (Add.getClass().equals(InternetAddress.class)) {
                        InternetAddress iad = (InternetAddress) Add;
                        From += iad.getAddress() + ", ";
                    } else {
                        From += fromAdd[0].toString() + ", ";
                    }
                }
            }
            i++;
        }
        return From;
    }
    
    
    /**
     * 
     * @param index
     * @param msg
     * @return
     * @throws MessagingException 
     */
    public static Enumeration getAllHeadersFrom(int index, Message[] msg) throws MessagingException{
        Enumeration e = null;
        
        e = msg[index].getAllHeaders();
        
        return e;
    }
    
    
    /**
     * 
     * @param initialArray
     * @param newValue
     * @return 
     */
    public static Message[] AddToArray(Message[] initialArray , Message newValue) {
        Message[] newArray = new Message[initialArray.length + 1];
        for (int index = 0; index < initialArray.length; index++) {
            newArray[index] = initialArray[index];
        }

        newArray[newArray.length - 1] = newValue;
        return newArray;
    }
    
    
    
    /**
     * 
     * @param msg
     * @param fichier
     * @return
     * @throws MessagingException
     * @throws IOException 
     */
    public static String getTextFromMessage(Message msg, List<String> fichier) throws MessagingException, IOException{
        String txt = "";
        
        if(msg.isMimeType("multipart/*")) {
            Multipart MultiMsg = (Multipart) msg.getContent();
            int n = MultiMsg.getCount();
            System.out.println("PartCount: " + n);
            
            for (int j = 0; j < n; j++) {
                Part part = MultiMsg.getBodyPart(j);
                String disposition = part.getDisposition();
                
                //Message Principal si en Alternative (message avec des embeds)
                Multipart MultiMsg2;
                if(part.isMimeType("multipart/alternative")){
                    
                    MultiMsg2 = (Multipart) part.getContent();
                    int n2 = MultiMsg2.getCount();
                    Part part2 = MultiMsg2.getBodyPart(0);
                    
                    if(part2.isMimeType("text/plain")){
                        txt += MimeUtility.decodeText((String) part2.getContent());
                    }
                    else{
                        txt = "Error Message Not Found";
                    }
                }
                
                //Message Principal
                if (part.isMimeType("text/plain")) { //&& part.getFileName() == null
                    System.out.println("This is the message" + part.getContentType());
                    txt += MimeUtility.decodeText((String) part.getContent());
                }
                
                //Attachment
                if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                    String FileName = MimeUtility.decodeText(part.getFileName());
                    
                    fichier.add(FileName);
                    System.out.println("Nom fichier : " + FileName);
                }
            }
        } 
        
        //SimplePart Message:
        else {
            txt =  MimeUtility.decodeText((String) msg.getContent());
        }    
        return txt;
    }
    
    /**
     * 
     * @param directory
     * @param FileName
     * @param m
     * @throws MessagingException
     * @throws IOException 
     */
    public static void saveFileTo(String directory, String FileName, Message m) throws MessagingException, IOException{
        if (m.isMimeType("multipart/*")) {
            Multipart msgMP = (Multipart) m.getContent();
            int np = msgMP.getCount();

            //Passage dans les BodyParts:
            for (int j = 0; j < np; j++) {

                Part part = msgMP.getBodyPart(j);
                String disposition = part.getDisposition();

                if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                    if(part.getFileName().equals(FileName)){
                        System.out.println("Fichier a télécharger : " + part.getFileName());
                        int c;
                        
                        InputStream readStream = part.getInputStream();
                        FileOutputStream writeStream = new FileOutputStream(directory);
                        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                        
                        while ((c = readStream.read()) != -1) {
                            tmp.write(c);
                        }
                        tmp.writeTo(writeStream);
                        writeStream.close();
                    }
                }
            }
        }
    }
    
    
    /*Ajoute les fichier joints au message principal*/
    public static void setFilePart(Multipart msg, String FilePath, ArrayList<String> FileList) throws IOException, MessagingException{
            String[] Names = FilePath.split("/");
            String Name = Names[Names.length - 1];
            MimeBodyPart BodyPart = new MimeBodyPart();
            
            //Idiqué le type de multipart
            BodyPart.attachFile(new File(FilePath));            
            BodyPart.setDataHandler (new DataHandler(new FileDataSource (FilePath)));
            BodyPart.setFileName(Name);
            
            msg.addBodyPart(BodyPart);
            
            FileList.add(Name);
    }
    
    public static Address[] convertAddr(ArrayList<String> vec) throws AddressException{
        ArrayList<Address> Addresses = new ArrayList<>();
        
        for (String tmp : vec) {
            if(!tmp.equals("")){
                Addresses.add(new InternetAddress(tmp));
            }
        }
        
        Object[] tmp = Addresses.toArray();
        return Arrays.copyOf(tmp, tmp.length, Address[].class);
    }
    
    /*Envoi de message texte basique sans piece jointe*/
    public static void createMessageSimple(MimeMessage msg, Address[] To, Address[] Cc, String Subject, String Text) throws MessagingException{
            System.out.println("Création Message Simple");
            msg.setFrom();
            
            msg.setRecipients(Message.RecipientType.TO, To);
            msg.setRecipients(Message.RecipientType.CC, Cc);
            
            msg.setSubject(Subject); 
            msg.setText (Text);
    }

    
    /*Envoi d'un message avec piece jointe multiple et document texte*/
    public static void createMessageMultiPart(MimeMessage msg, Multipart Multip, Address[] To, Address[] Cc, String Subject, String Text) throws MessagingException{
        
        msg.setFrom();
        
        msg.setRecipients(Message.RecipientType.TO, To);
        msg.setRecipients(Message.RecipientType.CC, Cc);

        msg.setSubject(Subject);
        
        System.out.println("Création Message MultiPart");
            //Ajout du Texte comme premier composant
            MimeBodyPart msgBP = new MimeBodyPart(); 
            msgBP.setText(Text);
            Multip.addBodyPart(msgBP);
            
            msg.setContent(Multip);
    }
}
