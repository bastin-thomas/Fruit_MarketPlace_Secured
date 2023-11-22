/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.Utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.io.DigestOutputStream;

/**
 *
 * @author Arkios
 */
public class CryptoUtils {
    
    public static byte[] CreateDigest(ArrayList<Object> objects) throws IOException
    {
        ObjectOutputStream oos = null;
        MessageDigest md = null;
        
        try {
            oos = new ObjectOutputStream(new ByteArrayOutputStream());
            md = MessageDigest.getInstance("SHA-512","BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            try{
                md = MessageDigest.getInstance("SHA-256","BC");
            }
            catch(NoSuchAlgorithmException | NoSuchProviderException ex2)
            {
                Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
        
        for(Object obj : objects)
        {
            oos.writeObject(obj);
        }
        
        md.update(oos.toString().getBytes());
            
        return md.digest();
    }
}
