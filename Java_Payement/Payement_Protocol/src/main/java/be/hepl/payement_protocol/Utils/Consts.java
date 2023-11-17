/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.Utils;

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
    
    
    public final static String ConfigIP = "Server_IP";
    public final static String ConfigDefaultIP = "127.0.0.1";
    
    public final static String ConfigPort = "Server_PORT";
    public final static String ConfigDefaultPort = "50002";
    
    public final static String ConfigPortSecured = "Server_PORT_SECURED";
    public final static String ConfigDefaultPortSecured = "50052";
    
    public final static String ConfigDBString = "DB_URL_CONNEXION";
    public final static String ConfigDefaultDBString = "jdbc:mariadb://192.168.1.19:3306/PourStudent?user=Student&password=PassStudent1_";
    
    public final static String ConfigPoolSize = "POOL_SIZE";
    public final static String ConfigDefaultPoolSize = "10";
    
    public final static char EndOfRequest1 = '&';
    public final static char EndOfRequest2 = ')';
    
    public final static char SplitCommand = '@';
    public final static char SplitParameters = '#';
    public final static char SplitList = '~';
}
