/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.Utils;

import be.hepl.cryptolibrary.CryptoConsts;
import be.hepl.stockmanagement.Utils.Consts;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Arkios
 */
public class ConfigFolderManager {
    /**
     * Load Config file into a Properties Object to be used later
     * @return 
     */
    public static Properties LoadProperties() {
        Properties config = new Properties();
        File configFile = new File(Consts.ConfigFilePathServer);

        try {
            if(configFile.exists()){
                InputStream input = new FileInputStream(configFile);
                config.load(input);
            }
        } catch(IOException ex) {}
        
        
        
        if(!config.containsKey(Consts.ConfigPoolSize))
        {
            config.setProperty(Consts.ConfigPoolSize,Consts.ConfigDefaultPoolSize);
        }
        
        if(!config.containsKey(Consts.ConfigMaxPendingConnexion))
        {
            config.setProperty(Consts.ConfigMaxPendingConnexion,Consts.ConfigDefaultMaxPendingConnexion);
        }
                
        if(!config.containsKey(Consts.ConfigDBString))
        {
            config.setProperty(Consts.ConfigDBString,Consts.ConfigDefaultDBString);
        }
                
        if(!config.containsKey(Consts.ConfigPort))
        {
            config.setProperty(Consts.ConfigPort, Consts.ConfigDefaultPort);
        }
        
        if(!config.containsKey(Consts.ConfigIP))
        {
            config.setProperty(Consts.ConfigIP, Consts.ConfigDefaultIP);
        }
        
        if(!config.containsKey(Consts.ConfigKeyStorePath))
        {
            config.setProperty(Consts.ConfigKeyStorePath, Consts.ConfigDefaultServerKeyStorePath);
        }
        
        if(!config.containsKey(Consts.ConfigKeyStorePassword))
        {
            config.setProperty(Consts.ConfigKeyStorePassword, Consts.ConfigDefaultServerKeyStorePassword);
        }
        
        if (!config.containsKey(Consts.ConfigPortSecured)) {
            config.setProperty(Consts.ConfigPortSecured, Consts.ConfigDefaultPortSecured);
        }
        
        if (!config.containsKey(Consts.ConfigPoolSecuredSize)) {
            config.setProperty(Consts.ConfigPoolSecuredSize, Consts.ConfigDefaultPoolSecuredSize);
        }
        
        
        if (!config.containsKey(CryptoConsts.ConfigTLSVersion)) {
            config.setProperty(CryptoConsts.ConfigTLSVersion, CryptoConsts.ConfigDefaultTLSVersion);
        }
        
        if (!config.containsKey(CryptoConsts.ConfigTLSCipherSuites)) {
            config.setProperty(CryptoConsts.ConfigTLSCipherSuites, CryptoConsts.ConfigDefaultTLSCipherSuites);
        }
        
        SaveProperties(config);
        
        return config;
    }
    
    
    public static void SaveProperties(Properties prop)
    {
        File configFile = new File(Consts.ConfigFilePathServer);
        OutputStream output = null;
        try {
            output = new FileOutputStream(configFile);
            prop.store(output, "Custom Config File");
        } catch (IOException ex) {
                System.out.println("FATAL ERROR WHILE SAVING TO PROPERTIES FILE: " + ex.getMessage());
        }
    }
}
