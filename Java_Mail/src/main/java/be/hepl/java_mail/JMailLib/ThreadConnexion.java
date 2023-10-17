/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_mail.JMailLib;

import be.hepl.java_mail.GUI.LoginPage;

/**
 *
 * @author Arkios
 */
public class ThreadConnexion extends Thread {
    
    private final LoginPage login;
    
    public ThreadConnexion(LoginPage login){
        this.login = login;
    }
    
    @Override
    public void run() {
        login.doConnexionLogic();
    }
}
