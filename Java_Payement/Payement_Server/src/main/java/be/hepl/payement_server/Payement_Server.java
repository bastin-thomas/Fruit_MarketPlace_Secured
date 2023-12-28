/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package be.hepl.payement_server;

import be.hepl.cryptolibrary.CryptoConsts;
import be.hepl.generic_server_tcp.OnDemandServer.ListenThreadOnDemand;
import be.hepl.generic_server_tcp.OnDemandServer.ListenThreadOnDemand_TLS;
import be.hepl.generic_server_tcp.ListenThread;
import be.hepl.payement_server.Utils.ConfigFolderManager;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.PooledServer.ListenThreadPooled;

import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.payement_protocol.protocol.DBPayement;
import be.hepl.payement_protocol.protocol.Payement;
import be.hepl.payement_protocol.protocol.Secured.DBPayement_Secured;
import be.hepl.payement_protocol.protocol.Secured.Payement_Secured;

import com.formdev.flatlaf.FlatLightLaf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 *
 * @author Arkios
 */
public class Payement_Server extends javax.swing.JFrame implements Logger {
    
    // <editor-fold defaultstate="collapsed" desc="My Properties">
    private boolean isLaunched;
    private ListenThread socket_secured;
    private ListenThread socket_TLS;
    private ListenThread socket_unsecured;
    private final Properties config;
    
    private KeyStore store;
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form Payement_Server2
     */
    public Payement_Server() {
        initComponents();
        isLaunched = false;
        
        ((DefaultTableModel) logs_table.getModel()).setRowCount(0);
        
        //Get Config From File
        config = ConfigFolderManager.LoadProperties();
        
        //Set UI Port-Unsecured
        Port_Spinner.getModel().setValue(Integer.valueOf(config.getProperty(Consts.ConfigPort)));
        
        //Set UI PoolSize
        Pool_Spinner.getModel().setValue(Integer.valueOf(config.getProperty(Consts.ConfigPoolSize)));
        
        //Set UI Port-Secured
        Port_Secured_Spinner.getModel().setValue(Integer.valueOf(config.getProperty(Consts.ConfigPortSecured)));
        
        Port_TLS_Spinner.getModel().setValue(Integer.valueOf(config.getProperty(CryptoConsts.ConfigPortTLS)));
        
        //Set DB URL
        DBurl_TextField.setText(config.getProperty(Consts.ConfigDBString));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     *
     * @param message
     */
    @Override
    public synchronized void Trace(String message) {
        DefaultTableModel modele = (DefaultTableModel) logs_table.getModel();
        Vector<String> ligne = new Vector<>();
        
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");       
        ligne.add(format.format(new Date()));
        ligne.add(Thread.currentThread().getName());
        ligne.add(message);
        modele.insertRow(modele.getRowCount(),ligne);
        
        //Put the scroll at bottom
        logs_table.scrollRectToVisible(
                logs_table.getCellRect(modele.getRowCount() - 1, 0, true)
        );
    }
    
    /**
     *
     */
    public synchronized void RemoveLogs()
    {
        DefaultTableModel modele = (DefaultTableModel) logs_table.getModel();
        modele.setRowCount(0);
    }
    
    /**
     *
     */
    public synchronized void FreezeUI()
    {
        this.Start_Stop_Button.setText("STOP");
        this.DBurl_TextField.setEnabled(false);
        this.Port_Spinner.setEnabled(false);
        this.AbortButton.setEnabled(true);
        this.Pool_Spinner.setEnabled(false);
        this.Port_Secured_Spinner.setEnabled(false);
        this.Port_TLS_Spinner.setEnabled(false);
        this.RemoveLogs();
    }
    
    /**
     *
     */
    public synchronized void UnFreezeUI()
    {
        this.Start_Stop_Button.setText("START");
        this.DBurl_TextField.setEnabled(true);
        this.Port_Spinner.setEnabled(true);
        this.AbortButton.setEnabled(false);
        this.Pool_Spinner.setEnabled(true);
        this.Port_Secured_Spinner.setEnabled(true);
        this.Port_TLS_Spinner.setEnabled(true);
        this.RemoveLogs();
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

        jLabel1 = new javax.swing.JLabel();
        AbortButton = new javax.swing.JButton();
        Start_Stop_Button = new javax.swing.JButton();
        Logs = new javax.swing.JLabel();
        Logs2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logs_table = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        DBurl_TextField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        Pool_Spinner = new javax.swing.JSpinner();
        Logs3 = new javax.swing.JLabel();
        Port_Spinner = new javax.swing.JSpinner();
        Logs1 = new javax.swing.JLabel();
        Logs4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Port_Secured_Spinner = new javax.swing.JSpinner();
        Logs6 = new javax.swing.JLabel();
        Logs7 = new javax.swing.JLabel();
        LogsTLS = new javax.swing.JLabel();
        Port_TLS_Spinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(808, 493));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setText("Payement Server Manager");

        AbortButton.setBackground(new java.awt.Color(204, 0, 0));
        AbortButton.setText("ABORT");
        AbortButton.setEnabled(false);
        AbortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbortButtonActionPerformed(evt);
            }
        });

        Start_Stop_Button.setBackground(new java.awt.Color(51, 204, 0));
        Start_Stop_Button.setText("START");
        Start_Stop_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start_Stop_ButtonActionPerformed(evt);
            }
        });

        Logs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs.setText("Logs :");

        Logs2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs2.setText("DB URL :");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        logs_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Horodateur", "Thread", "Message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        logs_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        logs_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        logs_table.setShowVerticalLines(true);
        logs_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(logs_table);
        if (logs_table.getColumnModel().getColumnCount() > 0) {
            logs_table.getColumnModel().getColumn(0).setMinWidth(115);
            logs_table.getColumnModel().getColumn(0).setPreferredWidth(115);
            logs_table.getColumnModel().getColumn(0).setMaxWidth(115);
            logs_table.getColumnModel().getColumn(1).setMinWidth(50);
            logs_table.getColumnModel().getColumn(1).setPreferredWidth(150);
            logs_table.getColumnModel().getColumn(1).setMaxWidth(300);
            logs_table.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(6, 16));
        jScrollPane1.setName(""); // NOI18N

        DBurl_TextField.setText("url db");
        DBurl_TextField.setMaximumSize(new java.awt.Dimension(64, 22));
        jScrollPane1.setViewportView(DBurl_TextField);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        Pool_Spinner.setModel(new javax.swing.SpinnerNumberModel(5, 1, 500, 1));

        Logs3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs3.setText("Pool Size :");

        Port_Spinner.setModel(new javax.swing.SpinnerNumberModel(50002, 50000, 50100, 1));

        Logs1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs1.setText("Socket Unsecured");

        Logs4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs4.setText("Port :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Logs4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(Port_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Logs1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Logs3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(Pool_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logs1)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Port_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Logs4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logs3)
                    .addComponent(Pool_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        Port_Secured_Spinner.setModel(new javax.swing.SpinnerNumberModel(50052, 50000, 50102, 1));

        Logs6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs6.setText("Socket Secured");

        Logs7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs7.setText("Port Self:");

        LogsTLS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LogsTLS.setText("Port TLS:");

        Port_TLS_Spinner.setModel(new javax.swing.SpinnerNumberModel(50062, 50000, 50100, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Logs6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Logs7)
                            .addComponent(LogsTLS))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Port_TLS_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Port_Secured_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logs6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logs7)
                    .addComponent(Port_Secured_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Port_TLS_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(LogsTLS, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(Logs, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Start_Stop_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Logs2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AbortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(Logs)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Start_Stop_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(Logs2)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(AbortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    private void AbortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbortButtonActionPerformed
        socket_unsecured.close();
        System.exit(127);
    }//GEN-LAST:event_AbortButtonActionPerformed
    
    private void Start_Stop_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Start_Stop_ButtonActionPerformed
        if(!isLaunched)
        {     
            //Start the Server      
            DBPayement db = null;
            try
            {
                db = new DBPayement_Secured(DBurl_TextField.getText(), this);
            }
            catch(Exception ex)
            {
                this.Trace("Impossible to connect to DB server: " + ex.getMessage());
                return;
            }
            
            
            int port_unsecured = (int) this.Port_Spinner.getModel().getValue();
            int port_secured = (int) this.Port_Secured_Spinner.getModel().getValue();
            int port_tls = (int) this.Port_TLS_Spinner.getModel().getValue();
            int poolSize = (int) this.Pool_Spinner.getModel().getValue();
            
            try {
                FreezeUI();
                //Start Unsecured Server
                socket_unsecured = new ListenThreadPooled(port_unsecured, new Payement(this, db), this, poolSize);
                socket_unsecured.start();
                
                //Start Secured Server
                socket_secured = new ListenThreadOnDemand(port_secured, new Payement_Secured(this, (DBPayement_Secured) db, config), this);
                socket_secured.start();
                
                
                
                //Start TLS Server
                String storePath = config.getProperty(Consts.ConfigKeyStorePath);
                String keyStorePassword = config.getProperty(Consts.ConfigKeyStorePassword);
                store = KeyStore.getInstance(new File(storePath), keyStorePassword.toCharArray());
                
                socket_TLS = new ListenThreadOnDemand_TLS(port_tls, new Payement(this, db), this, CryptoConsts.TLSCypherSuit,
                        CryptoConsts.TLSVersion, CryptoConsts.SecurityTLSProvider, store, keyStorePassword);
                socket_TLS.start();
                
                isLaunched = true;
            } catch (Exception ex) {
                this.Trace("Error creating ServerThread: " + ex.getMessage());
            }
        }
        else
        {
            //Stop the Server
            socket_unsecured.interrupt();
            socket_secured.interrupt();
            socket_TLS.interrupt();
            
            isLaunched = false;
            UnFreezeUI();
        }
    }//GEN-LAST:event_Start_Stop_ButtonActionPerformed
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int port = (int) this.Port_Spinner.getModel().getValue();
        config.setProperty(Consts.ConfigPort, ""+port);
        
        int port_secured = (int) this.Port_Secured_Spinner.getModel().getValue();
        config.setProperty(Consts.ConfigPortSecured, ""+port_secured);
        
        int port_tls = (int) this.Port_TLS_Spinner.getModel().getValue();
        config.setProperty(CryptoConsts.ConfigPortTLS, ""+port_tls);
        
        int poolSize = (int) this.Pool_Spinner.getModel().getValue();
        config.setProperty(Consts.ConfigPoolSize, ""+poolSize);
        
        String url = DBurl_TextField.getText();
        config.setProperty(Consts.ConfigDBString, ""+url);
                
        ConfigFolderManager.SaveProperties(config);
        
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Main">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Security.addProvider(new BouncyCastleProvider());
        
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
                new Payement_Server().setVisible(true);
            }
        });
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Properties">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AbortButton;
    private javax.swing.JTextField DBurl_TextField;
    private javax.swing.JLabel Logs;
    private javax.swing.JLabel Logs1;
    private javax.swing.JLabel Logs2;
    private javax.swing.JLabel Logs3;
    private javax.swing.JLabel Logs4;
    private javax.swing.JLabel Logs6;
    private javax.swing.JLabel Logs7;
    private javax.swing.JLabel LogsTLS;
    private javax.swing.JSpinner Pool_Spinner;
    private javax.swing.JSpinner Port_Secured_Spinner;
    private javax.swing.JSpinner Port_Spinner;
    private javax.swing.JSpinner Port_TLS_Spinner;
    private javax.swing.JButton Start_Stop_Button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable logs_table;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
