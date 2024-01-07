/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.Secured;

import be.hepl.cryptolibrary.CryptoConsts;
import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.cryptolibrary.CryptoUtils;
import be.hepl.payement_protocol.protocol.Gestion_Protocol_Client;
import be.hepl.payement_protocol.model.Facture;
import be.hepl.payement_protocol.model.Sale;
import be.hepl.payement_protocol.protocol.request.Secured.*;
import be.hepl.payement_protocol.protocol.response.Secured.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Sirac
 */
public class Gestion_Protocol_Client_Secured extends Gestion_Protocol_Client {

    // <editor-fold defaultstate="collapsed" desc="Properties">
    private KeyStore keystore;
    private char[] keystorePassword = config.getProperty(Consts.ConfigKeyStorePassword).toCharArray();
    private String currentUser = null;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Gestion_Protocol_Client_Secured(Socket sock, Properties config) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        super(sock, config);

        Security.addProvider(new BouncyCastleProvider());
        
        keystore = KeyStore.getInstance(CryptoConsts.KeyStoreInstanceType);

        File filestore = new File(config.getProperty(Consts.ConfigKeyStorePath));

        if (!filestore.exists()) {
            throw new KeyStoreException();
        }
        FileInputStream filestorestream = new FileInputStream(filestore.getPath());
        
        keystore.load(filestorestream, config.getProperty(Consts.ConfigKeyStorePassword).toCharArray());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    
    // <editor-fold defaultstate="collapsed" desc="SendLogin Request">
    @Override
    public boolean SendLogin(String Login, String Password) throws Exception {
        String ClientKeypairEntryName = CryptoConsts.ClientCertificateName + "-" + Login;
        String SessionSecretKeyEntryName = CryptoConsts.SessionKeyName + "-" + Login;
        
        Certificate RootCert = keystore.getCertificate(CryptoConsts.RootCertificateName);
        Object response = null;
        
        //Digest Creation         
        long temps = new Date().getTime();
        double alea = Math.random();
        
        ArrayList<Object> objects = new ArrayList<>();
        
        objects.add(Login);
        objects.add(Password);
        objects.add(temps);
        objects.add(alea);
        
        byte[] digest = CryptoUtils.CreateDigest(objects);
        
        
        //Check if certificate already exist on the client
        Certificate clientCertificate = null;
        
        try {
           clientCertificate = keystore.getCertificate(ClientKeypairEntryName);
           clientCertificate.verify(RootCert.getPublicKey());
        } catch (Exception ex) {
            
            System.out.println("Exception Catch, Deletion of the current Certificate: " + ex.getMessage());
            
            //If entry exist, clear it
            if (keystore.isKeyEntry(ClientKeypairEntryName)) {
                keystore.deleteEntry(ClientKeypairEntryName);
            }
            
            //KeyPair Generation
            KeyPair clientKeyPair = CryptoUtils.CreateKeyPair();
            
            String CertName = ClientKeypairEntryName;
            //Certificate Generation
            Certificate[] chain = CryptoUtils.CreateCertificate(CertName, clientKeyPair, 
                                                             keystorePassword.toString(), config);
            clientCertificate = chain[0];

            //Storing in the keystore
            keystore.setKeyEntry(ClientKeypairEntryName, clientKeyPair.getPrivate(),
                               config.getProperty(Consts.ConfigKeyStorePassword).toCharArray(), chain);
            SaveStore();
        }
        
        
        //Send request
        try {
            oos.writeObject(new LoginRequest_Secured(Login, clientCertificate, digest, temps, alea));
            response = ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new Exception("ENDCONNEXION", ex);
        }
        
        
        
        // Login validity
        if (response instanceof LoginResponse_Secured Lresponse) {
            
            //If login is valid, retrieve the session key to put it in store and set current user;
            if (Lresponse.isValid()) {
                PrivateKey clientPrivateKey = (PrivateKey) keystore.getKey(ClientKeypairEntryName, keystorePassword);
            
                // Decrypt SessionKey
                byte[] encodedSecretKey = CryptoUtils.AsymetricalDecrypt(Lresponse.getCryptedSessionSecretKey(), clientPrivateKey);
                SecretKey sessionSecretKey = CryptoUtils.DecodeSecretKey(encodedSecretKey);

                // Put SessionKey into the keystore
                if(keystore.isKeyEntry(SessionSecretKeyEntryName)){
                    keystore.deleteEntry(SessionSecretKeyEntryName);
                }

                keystore.setKeyEntry(SessionSecretKeyEntryName, sessionSecretKey, keystorePassword, null);
                SaveStore();
                currentUser = Login;
                return true;
                
            } else {
                socket.close();
                throw new Exception(Lresponse.getCause());
            }
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    // </editor-fold>

    @Override
    public void SendLogout(String Login) throws Exception {
        try {
            oos.writeObject(new LogoutRequest_Secured(Login));
            try {
                String SessionSecretKeyEntryName = CryptoConsts.SessionKeyName + "-" + currentUser;
                if (keystore.isKeyEntry(SessionSecretKeyEntryName)) {
                    keystore.deleteEntry(SessionSecretKeyEntryName);
                }
                SaveStore();
            } catch (KeyStoreException ex) {
                Logger.getLogger(Gestion_Protocol_Client_Secured.class.getName()).log(Level.SEVERE, null, ex);
            }
            currentUser = null;
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }
    }

    @Override
    public ArrayList<String> SendGetClientsRequest() throws Exception {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + currentUser;
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + currentUser;
        Object object = null;

        
        
        //Create a signature:
        PrivateKey clientPrivateKey = (PrivateKey) keystore.getKey(KeyPairEntryName, keystorePassword);
        
        ArrayList<Object> toSign = new ArrayList<>();
        toSign.add(currentUser);
        
        byte[] clientSignature = CryptoUtils.CreateSignature(toSign, clientPrivateKey);
        
        //No need to encrypt data
        
        try {
            GetClientsRequest_Secured clientsRequest_Secured = new GetClientsRequest_Secured(clientSignature, currentUser);
            oos.writeObject(clientsRequest_Secured);
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Decode the response
        if (object instanceof GetClientsResponse_Secured response) {
            ArrayList<String> decodedClients = new ArrayList<>();
            
            //DecryptClientSecured with session
            try{
                SecretKey key = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
                
                decodedClients = (ArrayList<String>) CryptoUtils.SymetricalDecrypt(response.getEncryptedClients(), key, new IvParameterSpec(response.getIvparameter()));
            }
            catch(Exception ex){
                throw new Exception("Impossible to decrypt the ClientList: " + ex.getMessage());
            }
            
            return decodedClients;
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }

    @Override
    public ArrayList<Facture> SendGetFacturesRequest(String idClient) throws Exception {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + currentUser;
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + currentUser;
        Object object = null;

        //Create a signature:
        PrivateKey clientPrivateKey = (PrivateKey) keystore.getKey(KeyPairEntryName, keystorePassword);
        
        ArrayList<Object> toSign = new ArrayList<>();
        toSign.add(idClient);
        
        byte[] idClientSignature = CryptoUtils.CreateSignature(toSign, clientPrivateKey);
        //No need to encrypt data
        
        
        try {
            oos.writeObject(new GetFacturesRequest_Secured(idClient, idClientSignature));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }

        // Decode response
        if (object instanceof GetFacturesResponse_Secured response) {
            
            ArrayList<Facture> decodedBills = new ArrayList<>();
            
            //DecryptClientSecured with session
            try{
                SecretKey key = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
                
                decodedBills = (ArrayList<Facture>) CryptoUtils.SymetricalDecrypt(response.getEncryptedBills(), key, new IvParameterSpec(response.getIvparameter()));
            }
            catch(Exception ex){
                throw new Exception("Impossible to decrypt the ClientList: " + ex.getMessage());
            }
            
            return decodedBills;
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }

    @Override
    public ArrayList<Sale> SendGetSalesRequest(int idBills) throws Exception {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + currentUser;
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + currentUser;
        Object object = null;
        
        //Create a signature:
        PrivateKey clientPrivateKey = (PrivateKey) keystore.getKey(KeyPairEntryName, keystorePassword);
        
        ArrayList<Object> toSign = new ArrayList<>();
        toSign.add(idBills);
        
        byte[] idBillsSignature = CryptoUtils.CreateSignature(toSign, clientPrivateKey);
        //No need to encrypt data
        
        try {
            oos.writeObject(new GetSalesRequest_Secured(idBills, idBillsSignature));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }
        
        // Decode response
        if (object instanceof GetSalesResponse_Secured response) {
            ArrayList<Sale> decodedSales = new ArrayList<>();
            
            //DecryptClientSecured with session
            try{
                SecretKey key = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
                
                decodedSales = (ArrayList<Sale>) CryptoUtils.SymetricalDecrypt(response.getEncryptedSales(), key, new IvParameterSpec(response.getIvparameter()));
            }
            catch(Exception ex){
                throw new Exception("Impossible to decrypt the ClientList: " + ex.getMessage());
            }
            
            return decodedSales;
            
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }

    @Override
    public boolean SendPayFactureRequest(int idBills, String Name, String VISA) throws Exception {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + currentUser;
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + currentUser;
        SecretKey sessionKey = null;
        
        Object object = null;

        ArrayList<Object> data = new ArrayList<>();
        data.add(idBills);
        data.add(Name);
        data.add(VISA);
        
        //Create a signature:
        PrivateKey clientPrivateKey = (PrivateKey) keystore.getKey(KeyPairEntryName, keystorePassword);
        
        byte[] payFactureSignature = CryptoUtils.CreateSignature(data, clientPrivateKey);
        
        //Encrypt the data:
        //Encrypt the Sales:
        byte[] encryptedPayFacture = null;
        IvParameterSpec ivparameter = null;

        try {
            sessionKey = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
            ivparameter = CryptoUtils.CreateIvParameter();
            encryptedPayFacture = CryptoUtils.SymetricalEncrypt(data, sessionKey, ivparameter);
            
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            encryptedPayFacture = null;
        }
        
        
        try {
            oos.writeObject(new PayFactureRequest_Secured(payFactureSignature, encryptedPayFacture, ivparameter));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }

        // Decode Response
        if (object instanceof PayFactureResponse_Secured response) {
            
            //Regen a new HashMac
            ArrayList<Object> toHash = new ArrayList<>();
            toHash.add(response.isSuccess());
            toHash.add(response.getCause());
            byte[] hmac = CryptoUtils.CreateHMAC(toHash, sessionKey);
            
            //Si hmac est valide
            if(MessageDigest.isEqual(hmac, response.getHmac())){
                if (response.isSuccess()) {
                    return true;
                } else {
                    //CARD_INVALID or other things
                    throw new Exception(response.getCause());
                }
            }
            else{
                throw new Exception("HMAC_INVALID");
            }
        } else {
            socket.close();
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Utils Methods">
    public void SaveStore() {
        try {
            File filestore = new File(config.getProperty(Consts.ConfigKeyStorePath));

            if (!filestore.exists()) {
                throw new KeyStoreException();
            }

            FileOutputStream filestorestream = new FileOutputStream(filestore.getPath());
            this.keystore.store(filestorestream, config.getProperty(Consts.ConfigKeyStorePassword).toCharArray());
            System.out.println("Client Saved the KEYSTORE");

            filestorestream.close();

        } catch (Exception ex) {
            Logger.getLogger(Gestion_Protocol_Client_Secured.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Close() {
        this.SaveStore();
    }
    // </editor-fold>
}
