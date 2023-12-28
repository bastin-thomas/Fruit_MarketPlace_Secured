/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package be.hepl.payement_client.GUI;
import be.hepl.cryptolibrary.CryptoConsts;
import be.hepl.cryptolibrary.TLSUtils;
import be.hepl.payement_client.Utils.ConfigFolderManager;
import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.payement_protocol.protocol.Gestion_Protocol_Client;
import be.hepl.payement_protocol.protocol.Secured.Gestion_Protocol_Client_Secured;
import com.formdev.flatlaf.FlatLightLaf;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Arkios
 */
public class LoginPage extends javax.swing.JFrame {
    
    // <editor-fold defaultstate="collapsed" desc="My Properties">
    private Properties Config = null;
    private Gestion_Protocol_Client GPC;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates new form Maraicher
     */
    public LoginPage(){       
        initComponents();
        //Read the config File and put into props
        try {
            Config = ConfigFolderManager.LoadProperties();
        }
        catch (IOException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Impossible to read the Config File, please delete it.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Login_TextField = new javax.swing.JTextField();
        Password_TextField = new javax.swing.JPasswordField();
        Login_Label = new javax.swing.JLabel();
        Password_Label = new javax.swing.JLabel();
        Secured_CheckBox = new javax.swing.JCheckBox();
        Login_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();
        TLS_CheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setMinimumSize(new java.awt.Dimension(258, 160));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                OnWindowClosing(evt);
            }
        });

        Login_TextField.setText("Thomas");

        Password_TextField.setText("123");

        Login_Label.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Login_Label.setText("Login");

        Password_Label.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Password_Label.setText("Password");

        Secured_CheckBox.setSelected(true);
        Secured_CheckBox.setText("Secure");
        Secured_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Secured_CheckBoxActionPerformed(evt);
            }
        });

        Login_Button.setText("Login");
        Login_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Login_ButtonActionPerformed(evt);
            }
        });

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        TLS_CheckBox.setSelected(true);
        TLS_CheckBox.setText("Using TLS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Password_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Login_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Login_TextField)
                                    .addComponent(Password_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Cancel_Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Login_Button))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Secured_CheckBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TLS_CheckBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Login_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Login_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Password_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Password_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(Secured_CheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TLS_CheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Login_Button)
                    .addComponent(Cancel_Button))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    private void Login_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Login_ButtonActionPerformed
        String Login = "";
        String Password = "";
        int portServeur = -1;
        String ipServeur = "-1.-1.-1.-1";
        String securedStatus = "Unsecured";
        
        //Connect to Server
        try { 
            ipServeur = Config.getProperty(Consts.ConfigIP);
            
            if(Secured_CheckBox.isSelected())
            {
                if(TLS_CheckBox.isSelected()){
                    securedStatus = "TLS";
                    
                    String ip = Config.getProperty(Consts.ConfigIP);
                    String port = Config.getProperty(CryptoConsts.ConfigPortTLS);
                    String TLSVersion = CryptoConsts.TLSVersion;
                    String TLSProvider = CryptoConsts.SecurityTLSProvider;
                    String CypherSuit = CryptoConsts.TLSCypherSuit;
                    
                    String storePath = Config.getProperty(Consts.ConfigKeyStorePath);
                    String keyStorePassword = Config.getProperty(Consts.ConfigKeyStorePassword);
                    KeyStore store = KeyStore.getInstance(new File(storePath), keyStorePassword.toCharArray());
                    
                    
                    Socket socket = TLSUtils.createClientSocket(ip, port, CypherSuit, TLSVersion, TLSProvider, store, keyStorePassword);
                    
                    
                    GPC = new Gestion_Protocol_Client(socket, Config);
                }
                else{
                    securedStatus = "SelfSecured";
                    
                    portServeur = Integer.parseInt(Config.getProperty(Consts.ConfigPortSecured));
                    Socket socket = new Socket(ipServeur, portServeur);
                    GPC = new Gestion_Protocol_Client_Secured(socket, Config);
                }
            } else {
                portServeur = Integer.parseInt(Config.getProperty(Consts.ConfigPort));
                Socket socket = new Socket(ipServeur, portServeur);
                GPC = new Gestion_Protocol_Client(socket);
            }
        } catch(SSLHandshakeException ex){
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "SSL Handshake Error (" + ipServeur + ":" + portServeur + "): " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch(SocketException ex){
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Socket Error (" + ipServeur + ":" + portServeur + "): " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Impossible to reach the Server on: " + ipServeur + ":" + portServeur, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Impossible to open the KeyStore[" + Config.getProperty(Consts.ConfigKeyStorePath) + "] with password: " + Config.getProperty(Consts.ConfigKeyStorePassword), "Error", JOptionPane.ERROR_MESSAGE);    
        } catch (Exception ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Impossible to open the SSL Socket: " + ipServeur + ":" + portServeur, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Try To Login
        boolean connected = false;
        try{
            Login = this.Login_TextField.getText();
            Password = this.Password_TextField.getText();
            connected = GPC.SendLogin(Login, Password);
        }
        catch(Exception ex){
            switch(ex.getMessage())
            {
                case "ENDCONNEXION" -> {
                    JOptionPane.showMessageDialog(this, "Connexion Error during transmission of Data", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                case "NO_LOGIN" -> {
                    JOptionPane.showMessageDialog(this, "Login Does not exist in our DataBase", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                case "BAD_LOGIN" -> {
                    JOptionPane.showMessageDialog(this, "You've encoded the wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                case "UNEXPECTED_RESPONSE" -> {
                    JOptionPane.showMessageDialog(this, "The response received was unexpected", "Error", JOptionPane.ERROR_MESSAGE);
                }
                    
                default -> {
                    JOptionPane.showMessageDialog(this, "Unkown Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        
        //Open Facture Page, if good Login
        if(connected){
            try {
                MainPage window = new MainPage(Login, GPC, this, securedStatus);                
                this.setVisible(false);
                window.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
    }//GEN-LAST:event_Login_ButtonActionPerformed
    
    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed
    
    private void OnWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_OnWindowClosing
        ConfigFolderManager.SaveProperties(Config);
        
        System.exit(0);
    }//GEN-LAST:event_OnWindowClosing

    private void Secured_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Secured_CheckBoxActionPerformed
        if(this.Secured_CheckBox.isSelected()){
            this.TLS_CheckBox.setSelected(true);
            this.TLS_CheckBox.setEnabled(true);
        }
        else{
            this.TLS_CheckBox.setSelected(false);
            this.TLS_CheckBox.setEnabled(false);
        }
    }//GEN-LAST:event_Secured_CheckBoxActionPerformed
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Main">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        FlatLightLaf.setup();
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Properties">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Button;
    private javax.swing.JButton Login_Button;
    private javax.swing.JLabel Login_Label;
    private javax.swing.JTextField Login_TextField;
    private javax.swing.JLabel Password_Label;
    private javax.swing.JPasswordField Password_TextField;
    private javax.swing.JCheckBox Secured_CheckBox;
    private javax.swing.JCheckBox TLS_CheckBox;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
