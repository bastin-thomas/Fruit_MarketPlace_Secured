/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.cryptolibrary;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Arkios
 */
public class TLSUtils {
    
    public static SSLSocket createClientSocket(String ip, String port, String CypherSuit, String TLSVersion, String SecurityTLSProvider, KeyStore store, String keyStorePassword) throws Exception{
        SSLContext Ss1C = TLSUtils.getTLSContext(TLSVersion, SecurityTLSProvider, store, keyStorePassword);
        
        int portServeur = Integer.parseInt(port);
        SSLSocketFactory SslSFac= Ss1C.getSocketFactory();
        SSLSocket socket = (SSLSocket) SslSFac.createSocket(ip, portServeur);
        socket.setEnabledProtocols(new String[] {TLSVersion});
        socket.setEnabledCipherSuites(new String[]{CypherSuit});
        
        return socket;
    }
    
    public static SSLServerSocket createServerSocket(int port, String CypherSuit, String TLSVersion, String SecurityTLSProvider, KeyStore store, String keyStorePassword) throws Exception{
        SSLServerSocket socket;
        
        SSLContext tlsContext = TLSUtils.getTLSContext(TLSVersion, SecurityTLSProvider, store, keyStorePassword);
        SSLServerSocketFactory SslSFac= tlsContext.getServerSocketFactory();        
        socket = (SSLServerSocket) SslSFac.createServerSocket(port);
        
        socket.setEnabledProtocols(new String[]{TLSVersion});
        socket.setEnabledCipherSuites(new String[]{CypherSuit});
        
        socket.setNeedClientAuth(true);
        
        return socket;
    }
    
    public static HttpsServer createWebServerSocket(int port, int maxPending, String CypherSuit, String TLSVersion, String SecurityTLSProvider, KeyStore store, String keyStorePassword) throws Exception{
        InetSocketAddress addr = new InetSocketAddress("0.0.0.0", port);
        HttpsServer https = HttpsServer.create(addr, maxPending);
        SSLContext tlsContext = TLSUtils.getTLSContext(TLSVersion, SecurityTLSProvider, store, keyStorePassword);
                
        https.setHttpsConfigurator(new HttpsConfigurator(tlsContext){
            public void configure(HttpsParameters params) {
                params.setProtocols(new String[]{TLSVersion});
                params.setCipherSuites(new String[]{CypherSuit});
                params.setWantClientAuth(true);
            }    
        });
        
        return https;
    }
    
    
    
    
    
    
    
    
    
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
