/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.TLSUtils;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Arkios
 */
public class TLSUtils {
    
    public static SSLContext getTLSContext(String sslVersion, String provider, KeyStore store, String keystorePassword) throws Exception {
        try {
            Security.addProvider(new BouncyCastleProvider());
            
            SSLContext SslCtx = SSLContext.getInstance(sslVersion);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(provider);
            kmf.init(store, keystorePassword.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(provider);
            tmf.init(store);
            SslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            
            return SslCtx;
            
            
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException ex) {
            throw new Exception("SSLContext Innitialisation Error: " + ex.getMessage(), ex);
        }
    }
    
}
