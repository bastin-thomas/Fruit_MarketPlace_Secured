/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_client.Utils;

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
     * @throws IOException 
     */
    public static Properties LoadProperties() throws IOException{
        Properties config = new Properties();
        File configFile = new File(Consts.ConfigFilePathClient);
        
        //InitDefault Values
        config.setProperty(Consts.ConfigIP,Consts.ConfigDefaultIP);
        config.setProperty(Consts.ConfigPort, Consts.ConfigDefaultPort);
                
        try
        {
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
        }
        catch(IOException ex){}
        
        return config;
    }
}
