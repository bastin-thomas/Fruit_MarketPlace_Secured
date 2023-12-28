/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package be.hepl.java_c.client.GUI;

import be.hepl.java_c.client.Utils.Consts;
import be.hepl.java_c.client.Utils.MarketModel.Protocol;
import be.hepl.java_c.client.Utils.SocketClient;
import com.formdev.flatlaf.FlatLightLaf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Arkios
 */
public class LoginPage extends javax.swing.JFrame {
    
    // <editor-fold defaultstate="collapsed" desc="My Properties">
    private Properties Config = null;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates new form Maraicher
     */
    public LoginPage(){       
        initComponents();
        
        //Read the config File and put into props
        try {
            Config = LoadProperties();
        }
        catch (IOException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Impossible to read the Config File, please delete it.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Load Config file into a Properties Object to be used later
     * @return
     * @throws IOException 
     */
    private Properties LoadProperties() throws IOException{
        Properties config = new Properties();
        File configFile = new File(Consts.ConfigFilePath);
        
        //If file not exist create default one
        if(!configFile.exists()){
            //InitDefault Values
            config.setProperty(Consts.ConfigIP,Consts.ConfigDefaultIP);
            config.setProperty(Consts.ConfigPort, Consts.ConfigDefaultPort);
            
            OutputStream output = new FileOutputStream(configFile);
            config.store(output, Consts.ConfigFileComments);
        }
        else{
            //If exist read Properties
            InputStream input = new FileInputStream(configFile);
            config.load(input);
        }
        
        
        return config;
    }
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
        Create_CheckBox = new javax.swing.JCheckBox();
        Login_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();

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

        Create_CheckBox.setText("Create New Account");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Create_CheckBox))
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
                                .addComponent(Login_Button)))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Create_CheckBox)
                .addGap(24, 24, 24)
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
        SocketClient socket;
        Protocol prot;
        
        String Login = this.Login_TextField.getText();
        String Password = this.Password_TextField.getText();
        
        //Connect to Server
        try {
            socket = new SocketClient(Config);
            prot = new Protocol(socket);
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Impossible to reach the Server on: " + Config.getProperty(Consts.ConfigIP) + ":" + Config.getProperty(Consts.ConfigPort), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        
        if(this.Create_CheckBox.isSelected())
        {
            //Try To Create a new Account
            try{
                prot.SendCreateLogin(Login, Password);
                this.Create_CheckBox.setSelected(false);
            }
            catch(Exception ex){
                switch(ex.getMessage())
                {
                    case "ENDCONNEXION":
                        JOptionPane.showMessageDialog(this, "Connexion Error during transmission of Data", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Login Already Exits", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
        else
        {
            //Try To Login
            try{
                prot.SendLogin(Login, Password);
            }
            catch(Exception ex){
                switch(ex.getMessage())
                {
                    case "ENDCONNEXION":
                        JOptionPane.showMessageDialog(this, "Connexion Error during transmission of Data", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "NO_LOGIN":
                        JOptionPane.showMessageDialog(this, "Login Does not exist in our DataBase", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "BAD_LOGIN":
                        JOptionPane.showMessageDialog(this, "You've encoded the wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unkown Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
        
        //Open Market Page, if good Login
        Maraicher window = new Maraicher(this, prot, Login);
        window.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_Login_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void OnWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_OnWindowClosing
        this.dispose();
    }//GEN-LAST:event_OnWindowClosing
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
    private javax.swing.JCheckBox Create_CheckBox;
    private javax.swing.JButton Login_Button;
    private javax.swing.JLabel Login_Label;
    private javax.swing.JTextField Login_TextField;
    private javax.swing.JLabel Password_Label;
    private javax.swing.JPasswordField Password_TextField;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
