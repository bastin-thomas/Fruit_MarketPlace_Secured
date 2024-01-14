/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package be.hepl.payement_protocol.protocol.Secured;

import be.hepl.cryptolibrary.CryptoConsts;
import be.hepl.generic_server_tcp.Exceptions.EndConnectionException;
import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.Protocol;
import be.hepl.generic_server_tcp.Request;
import be.hepl.generic_server_tcp.Response;
import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.cryptolibrary.CryptoUtils;
import be.hepl.payement_protocol.model.*;
import be.hepl.payement_protocol.protocol.request.*;
import be.hepl.payement_protocol.protocol.request.Secured.*;
import be.hepl.payement_protocol.protocol.response.*;
import be.hepl.payement_protocol.protocol.response.Secured.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Arkios
 */
public class Gestion_Protocol_Server_Secured implements Protocol {

    // <editor-fold defaultstate="collapsed" desc="Properties">
    public final DBPayement_Secured db;
    private final Logger logger;
    private HashMap<Socket, String> connectedClients;

    protected final Properties config;
    protected final KeyStore keystore;
    protected final char[] keystorePassword;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Gestion_Protocol_Server_Secured(Logger log, DBPayement_Secured db, Properties config) throws Exception {
        logger = log;
        connectedClients = new HashMap<>();
        this.db = db;
        this.config = config;
        this.keystorePassword = config.getProperty(Consts.ConfigKeyStorePassword).toCharArray();

        Security.addProvider(new BouncyCastleProvider());

        keystore = KeyStore.getInstance(CryptoConsts.KeyStoreInstanceType); // object

        File filestore = new File(config.getProperty(Consts.ConfigKeyStorePath)); //load Meta-data

        if (!filestore.exists()) {
            throw new KeyStoreException();
        }
        FileInputStream filestorestream = new FileInputStream(filestore.getPath()); //init flux

        keystore.load(filestorestream, keystorePassword); //read Keystore
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    @Override
    public String getNom() {
        return "Payement_Secured";
    }
    // </editor-fold>

    @Override
    public synchronized Response RequestTreatment(Request requete, Socket socket) throws EndConnectionException {
        Response rep = null;
        if (connectedClients.containsKey(socket)) {

            if (requete instanceof GetSalesRequest_Secured request) {
                rep = GetSalesRequestTreatment(request, socket);
                logger.Trace(request.getClass().getName() + " Reçue et traitée");
            }

            if (requete instanceof GetFacturesRequest_Secured request) {
                rep = GetFacturesRequestTreatment(request, socket);
                logger.Trace(request.getClass().getName() + " Reçue et traitée");
            }

            if (requete instanceof PayFactureRequest_Secured request) {
                rep = PayFactureRequestTreatment(request, socket);
                logger.Trace(request.getClass().getName() + " Reçue et traitée");
            }

            if (requete instanceof GetClientsRequest_Secured request) {
                rep = GetClientsRequestTreatment(request, socket);
                logger.Trace(request.getClass().getName() + " Reçue et traitée");
            }

            if (requete instanceof LogoutRequest_Secured request) {
                LogoutRequestTreatment(request, socket);
                logger.Trace(request.getClass().getName() + " Reçue et traitée");
            }
        }

        if (requete instanceof LoginRequest_Secured request) {
            rep = LoginRequestTreatment(request, socket);
            logger.Trace(request.getClass().getName() + " Reçue et traitée");
        }

        if (!socket.isConnected()) {
            throw new EndConnectionException(rep);
        }

        return rep;
    }

    // <editor-fold defaultstate="collapsed" desc="Protocol Treatment">
    /**
     * *
     * « Login » Login, password Oui ou non Vérification du login et du mot
     * (d’un employé) passe dans la table des employés
     *
     * @param loginRequest
     * @param socket
     * @return
     */
    private Response LoginRequestTreatment(LoginRequest_Secured loginRequest, Socket socket) {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + loginRequest.getLogin();
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + loginRequest.getLogin();
        boolean logged = false;
        String response = "";

        try {
            logged = db.Login(loginRequest);
        } catch (Exception ex) {
            response = ex.getMessage();
        }

        if (!logged) {
            return new LoginResponse_Secured(false, response);
        }

        //Verify the Client certificate
        logger.Trace("Vérification Certificat du Client");
        Certificate rootCert = null;
        try {
            rootCert = keystore.getCertificate(CryptoConsts.RootCertificateName); // TrustC CA in Server KeyS
            loginRequest.getClientCertificate().verify(rootCert.getPublicKey()); // verif certif
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            return new LoginResponse_Secured(false, "Error during KeyStore Reading: " + ex.getMessage());
        } catch (CertificateException | SignatureException | InvalidKeyException ex) {
            return new LoginResponse_Secured(false, "Your Certificate got a problem: " + ex.getMessage());
        }

        //Manage Client Certificate
        try {
            boolean refreshCertificate = false;

            if (keystore.isCertificateEntry(KeyPairEntryName)) {
                X509Certificate a = (X509Certificate) keystore.getCertificate(KeyPairEntryName);
                X509Certificate b = (X509Certificate) loginRequest.getClientCertificate();

                if (!CryptoUtils.CompareCertificate(a, b)) { // verif diff
                    //If there is an entry but outdated
                    refreshCertificate = true;
                }
            } else {
                //If there is no entry
                refreshCertificate = true;
            }

            if (refreshCertificate) {
                //Add the client certificate to the keystore the name will be the client login name
                keystore.setCertificateEntry(KeyPairEntryName,
                        loginRequest.getClientCertificate()); // nouveau tiroir
                SaveKeyStore();

                logger.Trace("Ajout du certificat du client dans le keyStore.");
            } else {
                logger.Trace("Certificat du client déjà enregistré dans le keystore.");
            }
        } catch (Exception ex) {
            return new LoginResponse_Secured(false, ex.getMessage());
        }

        //Create a new SessionKey and encrypt it with public key from the Client.
        byte[] cryptedSessionSecretKey = null;
        try {
            //If there is an old session key it is deleted.
            if (keystore.isKeyEntry(SessionKeyEntryName)) {
                keystore.deleteEntry(SessionKeyEntryName);
            }

            //Create a SecretKey for this session
            SecretKey sessionSecretKey = CryptoUtils.CreateSecretKey();

            //Add the new SessionKey to the keystore
            keystore.setKeyEntry(SessionKeyEntryName, sessionSecretKey, keystorePassword, null);
            SaveKeyStore();
            logger.Trace("Ajout d'une clé de session au KeyStore");

            //Encrypt the secret key, using the public key of Client
            cryptedSessionSecretKey = CryptoUtils.AsymetricalEncrypt(sessionSecretKey.getEncoded(),
                    loginRequest.getClientCertificate().getPublicKey());
            logger.Trace("Encryption de la clé de session pour l'envoi au client.");

        } catch (Exception ex) {
            return new LoginResponse_Secured(false, ex.getMessage());
        }

        connectedClients.put(socket, loginRequest.getLogin());

        return new LoginResponse_Secured(true, cryptedSessionSecretKey);
    }

    /**
     * *
     * « Logout »
     *
     * @param logoutRequest
     * @param socket
     * @return
     */
    private void LogoutRequestTreatment(LogoutRequest logoutRequest, Socket socket) throws EndConnectionException {
        if (connectedClients.containsKey(socket) == true) {
            try {
                String EntryName = CryptoConsts.SessionKeyName + "-" + connectedClients.get(socket);
                keystore.deleteEntry(EntryName);
                SaveKeyStore();
                logger.Trace("Suppression de la clé de session: " + EntryName);
            } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
                logger.Trace("Impossible de retirer la clé de session du keystore: " + ex.getMessage());
            }

            connectedClients.remove(socket);
            throw new EndConnectionException(null);
        }
    }

    private Response GetClientsRequestTreatment(GetClientsRequest_Secured request, Socket socket) {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + connectedClients.get(socket);
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + connectedClients.get(socket);
        ArrayList<String> clients = new ArrayList<>();

        //Check signature:
        logger.Trace("Check Signature");
        boolean isValid = false;
        try {
            ArrayList<Object> toVerify = new ArrayList<>();
            toVerify.add(request.getUser());

            Certificate clientCertificate = keystore.getCertificate(KeyPairEntryName);
            isValid = CryptoUtils.VerifySignature(toVerify, request.getClientSignature(), clientCertificate);

        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(Gestion_Protocol_Server_Secured.class.getName()).log(Level.SEVERE, null, ex);
            isValid = false;
        }

        //If Valid do the Request, else return void function
        if (isValid) {
            //GetClients and send to Client
            try {
                logger.Trace("Récupération des données");
                clients = db.GetClientList();
            } catch (Exception ex) {
                clients = new ArrayList<>();
            }
        }

        //Encrypt the client list
        logger.Trace("Encryption des données");
        byte[] encryptedClients = null;
        IvParameterSpec ivparameter = null;

        try {
            SecretKey sessionKey = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
            ivparameter = CryptoUtils.CreateIvParameter();

            encryptedClients = CryptoUtils.SymetricalEncrypt(clients, sessionKey, ivparameter); // Crypte clients list
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            encryptedClients = null;
        }

        return new GetClientsResponse_Secured(encryptedClients, ivparameter);
    }

    /*
    « Get Factures » idClient         Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                  factures du client dans la table
                                                                                       factures (sans le contenu détaillé
                                                                                       de la commande donc)
     */
    private Response GetFacturesRequestTreatment(GetFacturesRequest_Secured request, Socket socket) {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + connectedClients.get(socket);
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + connectedClients.get(socket);

        ArrayList<Facture> bills = new ArrayList<>();

        String message = "";
        String response = "";

        //Check signature:
        logger.Trace("Check Signature");
        boolean isValid = false;
        try {
            ArrayList<Object> toVerify = new ArrayList<>();
            toVerify.add(request.getIdClient());

            Certificate clientCertificate = keystore.getCertificate(KeyPairEntryName);
            isValid = CryptoUtils.VerifySignature(toVerify, request.getIdClientSignature(), clientCertificate);

        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(Gestion_Protocol_Server_Secured.class.getName()).log(Level.SEVERE, null, ex);
            isValid = false;
        }

        //If Valid do the Request, else return void function
        if (isValid) {
            try {
                logger.Trace("Récupération des données");
                bills = db.GetFactures(request.getIdClient());
            } catch (Exception ex) {
                switch (ex.getMessage()) {
                    case "SQL_ERROR" -> {
                        response = "SQL_ERROR";
                        message = ex.getMessage();
                    }

                    default -> {
                        response = "UNKOWN";
                        message = ex.getMessage();
                    }
                }
            }
        }

        //Encrypt the bills:
        logger.Trace("Encryption des données");
        byte[] encryptedBills = null;
        IvParameterSpec ivparameter = null;

        try {
            SecretKey sessionKey = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword); // get sess key
            ivparameter = CryptoUtils.CreateIvParameter();
            encryptedBills = CryptoUtils.SymetricalEncrypt(bills, sessionKey, ivparameter); // crypt
            
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            encryptedBills = null;
        }

        return new GetFacturesResponse_Secured(encryptedBills, ivparameter);
    }

    /*
    « Get Sales » idFacture                 Liste<article>      Permettrait de récupérer l’ensemble des articles 
                                                                concernant une facture dont on fournirait l’id au serveur.
     */
    private Response GetSalesRequestTreatment(GetSalesRequest_Secured request, Socket socket) {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + connectedClients.get(socket);
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + connectedClients.get(socket);

        ArrayList<Sale> Sales = new ArrayList<>();

        String message = "";
        String response = "";

        //Check signature:
        logger.Trace("Check Signature");
        boolean isValid = false;
        try {
            ArrayList<Object> toVerify = new ArrayList<>();
            toVerify.add(request.getIdFacture());

            Certificate clientCertificate = keystore.getCertificate(KeyPairEntryName);
            isValid = CryptoUtils.VerifySignature(toVerify, request.getIdBillsSignature(), clientCertificate);

        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(Gestion_Protocol_Server_Secured.class.getName()).log(Level.SEVERE, null, ex);
            isValid = false;
        }

        //If Valid do the Request, else return void function
        if (isValid) {
            try {
                logger.Trace("Récupération des données");
                Sales = db.GetSales(request.getIdFacture());
            } catch (Exception ex) {
                switch (ex.getMessage()) {
                    case "SQL_ERROR" -> {
                        response = "SQL_ERROR";
                        message = ex.getMessage();
                    }

                    default -> {
                        response = "UNKOWN";
                        message = ex.getMessage();
                    }
                }
            }
        }

        //Encrypt the Sales:
        logger.Trace("Encryption des données");
        byte[] encryptedSales = null;
        IvParameterSpec ivparameter = null;

        try {
            SecretKey sessionKey = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
            ivparameter = CryptoUtils.CreateIvParameter(); // random crypto
            encryptedSales = CryptoUtils.SymetricalEncrypt(Sales, sessionKey, ivparameter);
            
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            encryptedSales = null;
        }

        return new GetSalesResponse_Secured(encryptedSales, ivparameter);
    }

    /*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                                   (carte VISA invalide)    la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
     */
    private Response PayFactureRequestTreatment(PayFactureRequest_Secured request, Socket socket) {
        String KeyPairEntryName = CryptoConsts.ClientCertificateName + "-" + connectedClients.get(socket);
        String SessionKeyEntryName = CryptoConsts.SessionKeyName + "-" + connectedClients.get(socket);
        
        ArrayList<Object> data = new ArrayList<>();
        SecretKey secretKey = null;
        
        //DecryptClientSecured with session
        logger.Trace("Decryptage des données");
        try {
            secretKey = (SecretKey) keystore.getKey(SessionKeyEntryName, keystorePassword);
            data = (ArrayList<Object>) CryptoUtils.SymetricalDecrypt(request.getEncryptedPayFacture(), secretKey, new IvParameterSpec(request.getIvparameter()));
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            logger.Trace("Impossible de décrypté les données");
        }
        
        
        
        int idBills = (int) data.get(0);
        String Name = (String) data.get(1);
        String VISA = (String) data.get(2);
        
        logger.Trace("Check Signature");
        //Check signature:
        boolean isCertificateValid = false;
        try {
            ArrayList<Object> toVerify = new ArrayList<>();
            toVerify.add(idBills);
            toVerify.add(Name);
            toVerify.add(VISA);

            Certificate clientCertificate = keystore.getCertificate(KeyPairEntryName);
            isCertificateValid = CryptoUtils.VerifySignature(toVerify, request.getPayFactureSignature(), clientCertificate);

        } catch (KeyStoreException ex) {
            java.util.logging.Logger.getLogger(Gestion_Protocol_Server_Secured.class.getName()).log(Level.SEVERE, null, ex);
            isCertificateValid = false;
        }
        
        boolean isVisaCardValid = false;
        String cause = "";
        if(!isCertificateValid){
            cause = "SIGN_INVALID";
        }
        else{
            //Check Visa Card
            logger.Trace("Check Visa Card");

            isVisaCardValid = CheckVisaCard(VISA);

            if (isVisaCardValid) {
                try {
                    db.PayFacture(idBills);
                } catch (Exception ex) {
                    isVisaCardValid = false;
                    cause = ex.getMessage();
                }
            } else {
                cause = "CARD_INVALID";
            }
        }
        
        logger.Trace("Generation HMAC Response");
        ArrayList<Object> toHash = new ArrayList<>();
        toHash.add(isVisaCardValid);
        toHash.add(cause);
        
        byte[] hmac = CryptoUtils.CreateHMAC(toHash, secretKey);
        
        return new PayFactureResponse_Secured(isVisaCardValid, cause, hmac);
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="Utils Methods">
    /**
     * *
     * Check Visa Card using Lungth Algoritm
     * https://www.ibm.com/docs/en/order-management-sw/9.3.0?topic=cpms-handling-credit-cards
     *
     * @param visa
     * @return
     */
    private boolean CheckVisaCard(String visa) {
        long num = -1;

        try {
            try {
                num = Long.parseLong(visa);
            } catch (NumberFormatException ex) {
                String newVisa = "";
                String[] split = visa.split(" ");

                for (String s : split) {
                    newVisa += s;
                }
                
                num = Long.parseLong(newVisa);
            }
            logger.Trace("NumCard: " + num);

            ArrayList<Long> digits = new ArrayList<>();

            while (num > 0) {
                digits.add(num % 10);
                num = num / 10;
            }

            //Double ODD index
            for (int i = 0; i < digits.size(); i++) {
                //IF ODD index
                if (i % 2 != 0) {
                    Long doubled = digits.get(i) * 2;

                    digits.set(i, doubled);
                }
            }

            //sum all even doubled values or odd values
            int checksum = 0;
            for (Long digit : digits) {
                if (digit > 9) {
                    checksum += digit - 9;
                } else {
                    checksum += digit;
                }
            }

            //Check if equals to 0 on modulo 10
            return (checksum % 10 == 0);

        } catch (Exception ex) {
            logger.Trace("ERROR[" + ex.getMessage() + "] - " + ex.getCause());
            return false;
        }
    }

    protected void SaveKeyStore() throws FileNotFoundException, FileNotFoundException, KeyStoreException, IOException, IOException, NoSuchAlgorithmException, NoSuchAlgorithmException, CertificateException {
        File filestore = new File(config.getProperty(Consts.ConfigKeyStorePath));

        if (!filestore.exists()) {
            throw new KeyStoreException();
        }

        this.keystore.store(new FileOutputStream(filestore.getPath()), config.getProperty(Consts.ConfigKeyStorePassword).toCharArray());
    }
    // </editor-fold>
}
