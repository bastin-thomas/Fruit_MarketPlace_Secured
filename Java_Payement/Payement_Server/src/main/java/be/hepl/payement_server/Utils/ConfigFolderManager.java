/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_server.Utils;

import be.hepl.payement_protocol.Utils.Consts;
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
        
        config.setProperty(Consts.ConfigPoolSize,Consts.ConfigDefaultPoolSize);
        config.setProperty(Consts.ConfigDBString,Consts.ConfigDefaultDBString);
        config.setProperty(Consts.ConfigPort, Consts.ConfigDefaultPort);
        
        try {
            //If file not exist create default one
            if(!configFile.exists()){
                OutputStream output = new FileOutputStream(configFile);
                config.store(output, Consts.ConfigFileComments);
            }
            else{
                //If exist read Properties
                InputStream input = new FileInputStream(configFile);
                config.clear();
                config.load(input);
            }
        } catch(IOException ex) {}
        
        if(!config.containsKey(Consts.ConfigPoolSize))
        {
            config.setProperty(Consts.ConfigPoolSize,Consts.ConfigDefaultPoolSize);
        }
                
        if(!config.containsKey(Consts.ConfigDBString))
        {
            config.setProperty(Consts.ConfigDBString,Consts.ConfigDefaultDBString);
        }
                
        if(!config.containsKey(Consts.ConfigPort))
        {
            config.setProperty(Consts.ConfigPort, Consts.ConfigDefaultPort);
        }
        
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
