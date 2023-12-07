/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Properties;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;

/**
 *
 * @author Arkios
 */
public class CryptoUtils {
    
    public static final String CERT_SIGNATURE_ALG = Consts.SignatureAlgorythm;
    public static final String DIGEST_ALG = Consts.DigestAlgorythm;
    public static final String KEY_ALG = Consts.AsymetricAlgorythm;
    public static final String PROVIDER = Consts.SecurityProvider;
    
       
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
    public static byte[] AsymetricalSerializableEncrypt(Serializable decryptedData, PublicKey publicKey){
        return AsymetricalEncrypt(EncodeObject(decryptedData), publicKey);
    }
    
    public static byte[] AsymetricalEncrypt(byte [] decryptedData, PublicKey publicKey){
        try {
            Cipher cipher = Cipher.getInstance((Consts.AsymetricAlgorythm + "/" + Consts.AsymetricCipherMode + "/" + Consts.AsymetricPaddingMode), Consts.SecurityProvider);
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
    public static Object AsymetricalSerializableDecrypt(byte[] encryptedData, PrivateKey privateKey){
        return DecodeObject(AsymetricalDecrypt(encryptedData, privateKey));
    }
    
    /**
     *
     * @param encryptedData
     * @param privateKey
     * @return decryptedData
     */
    public static byte[] AsymetricalDecrypt(byte[] encryptedData, PrivateKey privateKey){
        try {
            Cipher cipher = Cipher.getInstance((Consts.AsymetricAlgorythm + "/" + Consts.AsymetricCipherMode + "/" + Consts.AsymetricPaddingMode), Consts.SecurityProvider);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException  ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
            sessionKeyGen.init(256, new SecureRandom());
            
            return sessionKeyGen.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     *
     * @return
     */
    public static IvParameterSpec CreateIvParameter(){
        byte[] initVector = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(initVector);
        return new IvParameterSpec(initVector); 
    }
    
    /**
     *
     * @param decryptedData
     * @param publicKey
     * @return encryptedData
     */
    public static byte[] SymetricalEncrypt(Object decryptedData, SecretKey secretKey, IvParameterSpec params){
        try {
            Cipher cipher = Cipher.getInstance((Consts.SymetricAlgorythm + "/" + Consts.SymetricCipherMode + "/" + Consts.SymetricPaddingMode), Consts.SecurityProvider);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
            return cipher.doFinal(EncodeObject(decryptedData));
            
        } catch (NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
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
    public static Object SymetricalDecrypt(byte[] encryptedData, SecretKey secretKey, IvParameterSpec params){
        try {
            
            Cipher cipher = Cipher.getInstance((Consts.SymetricAlgorythm + "/" + Consts.SymetricCipherMode + "/" + Consts.SymetricPaddingMode), Consts.SecurityProvider);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
            return DecodeObject(cipher.doFinal(encryptedData));
            
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Encoders Utils">
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
     * @param secretKey
     * @return byte array
     */
    public static byte[] EncodeKey(SecretKey secretKey){
        return secretKey.getEncoded();
    }
    
    
    
    
    /**
     *
     * @param list
     * @return
     */
    public static byte[] EncodeObject(Object obj){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            out.flush();
            return baos.toByteArray();
            
        } catch (IOException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    /**
     *
     * @param encodedObj
     * @return
     */
    public static Object DecodeObject(byte[] encodedObj){
        Object obj = null;
        try {
            
            final ByteArrayInputStream bais = new ByteArrayInputStream(encodedObj);
            final ObjectInputStream in = new ObjectInputStream(bais);
            
            return in.readObject();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Signature Utils">
    /**
     *
     * @param toSign
     * @param clientPrivateKey
     * @return
     */
    public static byte[] CreateSignature(Serializable toSign, PrivateKey clientPrivateKey) {
        byte[] sig = null;
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            Signature sigBuilder = Signature.getInstance(Consts.SignatureAlgorythm, Consts.SecurityProvider);
            sigBuilder.initSign(clientPrivateKey);
            
            oos.writeObject(toSign);
            oos.flush();
            sigBuilder.update(bos.toByteArray());
            sig = sigBuilder.sign();
        
        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            sig = null;
        }
        
        return sig;
    }
    
    /**
     *
     * @param toVerify
     * @param signature
     * @param clientPublicKey
     * @return
     */
    public static boolean VerifySignature(Serializable toVerify, byte[] signature, PublicKey clientPublicKey){
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            Signature sigBuilder = Signature.getInstance(Consts.SignatureAlgorythm, Consts.SecurityProvider);
            sigBuilder.initVerify(clientPublicKey);
            
            oos.writeObject(toVerify);
            oos.flush();
            sigBuilder.update(bos.toByteArray());            
            return sigBuilder.verify(signature);
            
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     *
     * @param toVerify
     * @param signature
     * @param clientCertificate
     * @return
     */
    public static boolean VerifySignature(ArrayList<Object> toVerify, byte[] signature, Certificate clientCertificate){
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            Signature sigBuilder = Signature.getInstance(Consts.SignatureAlgorythm, Consts.SecurityProvider);
            sigBuilder.initVerify(clientCertificate);
            
            oos.writeObject(toVerify);
            oos.flush();
            sigBuilder.update(bos.toByteArray());            
            return sigBuilder.verify(signature);
            
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="HMAC Utils">
    public static byte[] CreateHMAC(Serializable toHash, SecretKey secretKey){
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            
            Mac hm = Mac.getInstance(Consts.HMACAlgorythm,Consts.SecurityProvider);
            hm.init(secretKey);
            
            oos.writeObject(toHash);
            oos.flush();
            hm.update(bos.toByteArray());
            return hm.doFinal();
             
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | IOException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Digest Utils">
    /**
     *
     * @param Objects
     * @return a Digest
     * @throws IOException
     */
    public static byte[] CreateDigest(Serializable toHash)
    {
        try {
            final MessageDigest md = MessageDigest.getInstance(DIGEST_ALG,PROVIDER);
            md.update(EncodeObject(toHash));
            return md.digest();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    // </editor-fold>
}