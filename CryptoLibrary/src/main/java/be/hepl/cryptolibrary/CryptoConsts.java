/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.cryptolibrary;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Arkios
 */
public class CryptoConsts {
    public final static String ConfigRootKeyStorePath = "ROOT_KEYSTORE_PATH";
    public final static String ConfigDefaultRootKeyStorePath = "../../JAVA_CRYPTO/RootKeyStore.p12";
    
    public final static String ConfigRootKeyStorePassword = "ROOT_KEYSTORE_PASSWORD";
    public final static String ConfigDefaultRootKeyStorePassword = "1234";
    
    
    /*CRYPTO - RTI*/
    public final static String SecurityProvider = BouncyCastleProvider.PROVIDER_NAME;
    public final static String SecurityTLSProvider = "SunX509";
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

 
    
    /*SSL - Ecom*/
    public final static String ConfigPortTLS = "Server_PORT_TLS";
    public final static String ConfigDefaultPortTLS = "50062";
    
    public final static String TLSVersion = "TLSv1.3";
    public final static String TLSCypherSuit = "TLS_AES_256_GCM_SHA384";
}
