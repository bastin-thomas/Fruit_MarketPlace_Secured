/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Properties;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Arkios
 */
public class CryptoUtils {
    
    public static final String CERT_SIGNATURE_ALG = Consts.SignatureAlgorythm;
    public static final String DIGEST_ALG = Consts.DigestAlgorythm;
    public static final String KEY_ALG = Consts.AsymetricAlgorythm;
    public static final String PROVIDER = Consts.SecurityProvider;
    
    
    // <editor-fold defaultstate="collapsed" desc="Digest Utils">
    /**
     *
     * @param Objects
     * @return a Digest
     * @throws IOException
     */
    public static byte[] CreateDigest(ArrayList<Object> Objects) throws IOException
    {
        MessageDigest md = null;
        
        try {
            md = MessageDigest.getInstance(DIGEST_ALG,PROVIDER);
            for(Object obj : Objects)
            {
                byte[] toHash = ("" + obj).getBytes(Consts.CharsetName);
                md.update(toHash);
            }
            return md.digest();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Asymetrical Utils">
    /**
     *
     * @return a KeyPair
     */
    public static KeyPair CreateKeyPair()
    {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALG, PROVIDER);
            generator.initialize(2048, new SecureRandom());
            return generator.genKeyPair();
            
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="CreateCertificate">
    /**
     *
     * @param Name
     * @param key
     * @param password
     * @param config
     * @return Certificate Chain
     */
    public static Certificate[] CreateCertificate(String Name, KeyPair key, String password, Properties config) {
        try {
            //Open RootKeyStore
            KeyStore rootkeystore = KeyStore.getInstance(Consts.KeyStoreInstanceType);
            File filestore = new File(config.getProperty(Consts.ConfigRootKeyStorePath));
            
            if(!filestore.exists())
            {
                throw new KeyStoreException();
            }
            
            FileInputStream filestorestream = new FileInputStream(filestore.getPath());
            rootkeystore.load(filestorestream, config.getProperty(Consts.ConfigRootKeyStorePassword).toCharArray());
            
            //Get the rootPriveKey to sign the new certificate
            PrivateKey rootPriKey = (PrivateKey) rootkeystore.getKey(Consts.RootCertificateName, config.getProperty(Consts.ConfigRootKeyStorePassword).toCharArray());
            X509Certificate rootCert = (X509Certificate) rootkeystore.getCertificate(Consts.RootCertificateName);
            
            
            //Generate some dates
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date startDate = calendar.getTime();
            
            calendar.add(Calendar.MONTH, 1);
            Date endDate = calendar.getTime();
            
            
            
            //Create a new certificate Builder
            X500Principal certName = new X500Principal("CN="+Name);
            X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(rootCert.getIssuerX500Principal(), 
                    BigInteger.probablePrime(10, new Random()),startDate, endDate,
                    certName, key.getPublic());
            
            //Add usefull extension:
            JcaX509ExtensionUtils CertUtils = new JcaX509ExtensionUtils();
            // Use BasicConstraints to say that this Cert is not a CA
            certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
            // Add Issuer cert identifier as Extension
            certBuilder.addExtension(Extension.authorityKeyIdentifier, false, CertUtils.createAuthorityKeyIdentifier(rootCert.getPublicKey()));
            
            //Create the signer, sign the certificate and create it
            JcaContentSignerBuilder signer = new JcaContentSignerBuilder(Consts.SignatureAlgorythm);
            X509CertificateHolder certHolder = certBuilder.build(signer.build(rootPriKey));
            X509Certificate certificate  = new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(certHolder);
            
            
            
            try {
                // Verify the issued cert signature against the root (issuer) cert
                certificate.verify(rootCert.getPublicKey());
            } catch (CertificateException | NoSuchAlgorithmException | SignatureException | InvalidKeyException | NoSuchProviderException ex) {
                Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
            return new Certificate[]{certificate, rootCert};
            
        } catch (Exception ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
    
    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean CompareCertificate(X509Certificate a, X509Certificate b){
        return a.getSerialNumber().compareTo(b.getSerialNumber()) == 0;
    }
    
    
    /**
     *
     * @param decryptedData
     * @param publicKey
     * @return encryptedData
     */
    public static byte[] AsymetricalEncrypt(byte[] decryptedData, PublicKey publicKey){
        try {
            Cipher cipher = Cipher.getInstance((Consts.AsymetricAlgorythm + "/" + Consts.CipherMode + "/" + Consts.PaddingMode), Consts.SecurityProvider);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(decryptedData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     *
     * @param encryptedData
     * @param privateKey
     * @return decryptedData
     */
    public static byte[] AsymetricalDecrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception{
        try {
            Cipher cipher = Cipher.getInstance((Consts.AsymetricAlgorythm + "/" + Consts.CipherMode + "/" + Consts.PaddingMode), Consts.SecurityProvider);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException  ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Symetrical Utils">
    /**
     * 
     * @return a SecretKey
     */
    public static SecretKey CreateSecretKey(){
        try {
            KeyGenerator sessionKeyGen = KeyGenerator.getInstance(Consts.SymetricAlgorythm, Consts.SecurityProvider);
            sessionKeyGen.init(256);
            sessionKeyGen.init(new SecureRandom());
            return sessionKeyGen.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Encoders">
    /**
     *
     * @param encodedSecretKey
     * @return SecretKey instance
     */
    public static SecretKey DecodeSecretKey(byte[] encodedSecretKey){
        return new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, Consts.SymetricAlgorythm);
    }
    
    /**
     *
     * @param encodedSecretKey
     * @return SecretKey instance
     */
    public static Key DecodeAsymetricKey(byte[] encodedSecretKey){
        return new SecretKeySpec(encodedSecretKey, 0, encodedSecretKey.length, Consts.AsymetricAlgorythm);
    }
    
    /**
     *
     * @param secretKey
     * @return byte array
     */
    public static byte[] EncodeKey(Key secretKey){
        return secretKey.getEncoded();
    }
    // </editor-fold>
}
