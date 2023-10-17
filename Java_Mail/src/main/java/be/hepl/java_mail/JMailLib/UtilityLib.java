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
}
