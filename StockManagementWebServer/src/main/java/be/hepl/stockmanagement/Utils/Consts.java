/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.Utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Arkios
 */
public class Consts {

    /**
     * The path for the Config File
     */
    public final static String ConfigFilePathClient = "Client.cfg";
    public final static String ConfigFilePathServer = "Server.cfg";
    
    public final static String ConfigFileComments = "Default configuration File";
    
    public final static String WebSitePath = "./StockManagementWebsite/";
    
    
    public final static String ConfigIP = "Server_IP";
    public final static String ConfigDefaultIP = "0.0.0.0";
    
    public final static String ConfigPort = "Server_PORT";
    public final static String ConfigDefaultPort = "80";
    
    public final static String ConfigPortSecured = "Server_PORT_SECURED";
    public final static String ConfigDefaultPortSecured = "8080";
    
    
    public final static String ConfigDBString = "DB_URL_CONNEXION";
    public final static String ConfigDefaultDBString = "jdbc:mariadb://192.168.1.19:3306/PourStudent?user=Student&password=PassStudent1_";
    
    public final static String ConfigPoolSize = "POOL_SIZE";
    public final static String ConfigDefaultPoolSize = "10";
    
    public final static String ConfigPoolSecuredSize = "POOL_SECURED_SIZE";
    public final static String ConfigDefaultPoolSecuredSize = "10";
    
    public final static String ConfigMaxPendingConnexion = "MAX_PENDING_CONNEXIONS";
    public final static String ConfigDefaultMaxPendingConnexion = "150";
    
    public final static String ConfigKeyStorePath = "KEYSTORE_PATH";
    public final static String ConfigDefaultServerKeyStorePath = "../JAVA_CRYPTO/WebServerKeyStore.p12";
    
    public final static String ConfigKeyStorePassword = "KEYSTORE_PASSWORD";
    public final static String ConfigDefaultClientKeyStorePassword = "1234";
    public final static String ConfigDefaultServerKeyStorePassword = "1234";
    
    
    public final static String CharsetName = "UTF-8";
    
    public final static String SecurityProvider = BouncyCastleProvider.PROVIDER_NAME;
    public final static String KeyStoreInstanceType = "PKCS12";
    
    public final static String ClientCertificateName = "authkey";
    public final static String RootCertificateName = "myrootkey";
    public final static String SessionKeyName = "sessionkey";
    
    
    public final static String DigestAlgorythm = "SHA512";
    
    public final static String SymetricAlgorythm = "AES";
    public final static String SymetricCipherMode = "CBC";
    public final static String SymetricPaddingMode = "PKCS7Padding";
    
    public final static String AsymetricAlgorythm = "RSA";
    public final static String AsymetricCipherMode = "ECB";
    public final static String AsymetricPaddingMode = "OAEPWITHSHA-512ANDMGF1PADDING";
    
    public final static String SignatureAlgorythm = "SHA512WithRSA";
    
    public final static String HMACAlgorythm = "HmacSHA512";
}
