/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.protocol.Secured;

import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.payement_protocol.Utils.CryptoUtils;
import static be.hepl.payement_protocol.Utils.CryptoUtils.PROVIDER;
import be.hepl.payement_protocol.protocol.Gestion_Protocol_Client;
import be.hepl.payement_protocol.model.Facture;
import be.hepl.payement_protocol.model.Sale;
import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.payement_protocol.protocol.request.Secured.*;
import be.hepl.payement_protocol.protocol.response.*;
import be.hepl.payement_protocol.protocol.response.Secured.LoginResponse_Secured;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
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

        keystore = KeyStore.getInstance(Consts.KeyStoreInstanceType);

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
        String ClientKeypairEntryName = Consts.ClientCertificateName + "-" + Login;
        String SessionSecretKeyEntryName = Consts.SessionKeyName + "-" + Login;
        Certificate RootCert = keystore.getCertificate(Consts.RootCertificateName);
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
            
            //Certificate Generation
            Certificate[] chain = CryptoUtils.CreateCertificate(ClientKeypairEntryName, clientKeyPair, 
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
                SecretKey sessionSecretKey = (SecretKey) CryptoUtils.DecodeSecretKey(
                        CryptoUtils.AsymetricalDecrypt(Lresponse.getCryptedSessionSecretKey(), clientPrivateKey));

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
                String SessionSecretKeyEntryName = Consts.SessionKeyName + "-" + currentUser;
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
        Object object = null;

        try {
            oos.writeObject(new GetClientsRequest());
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }

        // Login validity
        if (object instanceof GetClientsResponse response) {
            return response.getClients();
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }

    @Override
    public ArrayList<Facture> SendGetFacturesRequest(String idClient) throws Exception {
        Object object = null;

        try {
            oos.writeObject(new GetFacturesRequest(idClient));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }

        // Login validity
        if (object instanceof GetFacturesResponse response) {
            return response.getBills();
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }

    @Override
    public ArrayList<Sale> SendGetSalesRequest(int idBills) throws Exception {
        Object object = null;

        try {
            oos.writeObject(new GetSalesRequest(idBills));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }

        // Login validity
        if (object instanceof GetSalesResponse response) {
            return response.getSales();
        } else {
            throw new Exception("UNEXPECTED_RESPONSE");
        }
    }

    @Override
    public boolean SendPayFactureRequest(int idBills, String Name, String VISA) throws Exception {
        Object object = null;

        try {
            oos.writeObject(new PayFactureRequest(idBills, Name, VISA));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new Exception("ENDCONNEXION", ex);
        }

        // Login validity
        if (object instanceof PayFactureResponse response) {
            if (response.isSuccess()) {
                return true;
            } else {
                //CARD_INVALID or other things
                throw new Exception(response.getCause());
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
